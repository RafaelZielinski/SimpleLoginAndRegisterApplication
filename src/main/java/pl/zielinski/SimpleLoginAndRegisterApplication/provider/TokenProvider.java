package pl.zielinski.SimpleLoginAndRegisterApplication.provider;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.PortResolverImpl;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import pl.zielinski.SimpleLoginAndRegisterApplication.domain.UserPrincipal;
import pl.zielinski.SimpleLoginAndRegisterApplication.service.UserService;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author rafek
 * @version 1.0
 * @licence ask rafekzielinski@wp.pl
 * @since 11/01/2024
 */
@Component
@RequiredArgsConstructor
public class TokenProvider {
    @Value(value = "${jwt.secret")
    private String secret;
    private static final long REFRESH_TOKEN_EXPIRATION_TIME = 432_000_000;
    private static final String CUSTOMER_OF_MY_SERVICES = "CUSTOMERS_OF_MY_PROJECT_WRITE_BY_RAFEK";
    private static final long ACCESS_TOKEN_EXPIRATION_TIME = 1_800_000;
    private final String RAFAEL_ZIELINSKI_AKA_RAFEK = "RAFAEL_ZIELINSKI_AKA_RAFEK";
    private final String AUTHORITIES = "authorities";
    public static final String TOKEN_CAN_NOT_BE_VERIFIED = "Token cannot be verified";
    private final UserService userService;

    public String createAccessToken(UserPrincipal userPrincipal) {
        return JWT.create().withIssuer(RAFAEL_ZIELINSKI_AKA_RAFEK)
                .withAudience(CUSTOMER_OF_MY_SERVICES)
                .withIssuedAt(new Date())
                .withSubject(String.valueOf(userPrincipal.getId()))
                .withArrayClaim(AUTHORITIES, getClaimsFromUser(userPrincipal))
                .withExpiresAt(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(secret.getBytes()));
    }

    public String createRefreshToken(UserPrincipal userPrincipal) {
        return JWT.create().withIssuer(RAFAEL_ZIELINSKI_AKA_RAFEK)
                .withAudience(CUSTOMER_OF_MY_SERVICES)
                .withIssuedAt(new Date())
                .withSubject(String.valueOf(userPrincipal.getId()))
                .withExpiresAt(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(secret.getBytes()));
    }

    public Long getSubject(String token, HttpServletRequest request) {
        try {
            return Long.valueOf(getJWTVerifier().verify(token).getSubject());
        } catch (TokenExpiredException exception) {
            request.setAttribute("expiredMessage", exception.getMessage());
            throw exception;
        } catch (InvalidClaimException exception) {
            request.setAttribute("invalidClaim", exception.getMessage());
            throw exception;
        } catch (Exception exception) {
            throw exception;
        }
    }

    public List<GrantedAuthority> getAuthorities(String token) {
        String[] claims = getClaimsFromToken(token);
        return Arrays.stream(claims).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    public Authentication getAuthentication(Long userId, List<GrantedAuthority> authorities, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userService.getUser(userId), null, authorities);
        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return usernamePasswordAuthenticationToken;
    }

    public boolean isTokenValid(Long id, String token) {
        JWTVerifier verifier = getJWTVerifier();
        return !Objects.isNull(id) && !isTokenExpired(verifier, token);
    }

    public boolean isTokenExpired(JWTVerifier verifier, String token) {
        Date expiration = verifier.verify(token).getExpiresAt();
        return expiration.before(new Date());
    }

    private String[] getClaimsFromToken(String token) {
        JWTVerifier verifier = getJWTVerifier();
        return verifier.verify(token).getClaim(AUTHORITIES).asArray(String.class);
    }

    private JWTVerifier getJWTVerifier() {
        JWTVerifier verifier;
        try {
            Algorithm algorithm = Algorithm.HMAC512(secret);
            verifier = JWT.require(algorithm).withIssuer(RAFAEL_ZIELINSKI_AKA_RAFEK).build();
        } catch (JWTVerificationException exception) {
            throw new JWTVerificationException(TOKEN_CAN_NOT_BE_VERIFIED);
        }
        return verifier;
    }

    private String[] getClaimsFromUser(UserPrincipal userPrincipal) {
        return userPrincipal.getAuthorities().stream().map(GrantedAuthority::getAuthority).toArray(String[]::new);
    }


}
