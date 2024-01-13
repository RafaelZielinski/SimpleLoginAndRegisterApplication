package pl.zielinski.SimpleLoginAndRegisterApplication.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import pl.zielinski.SimpleLoginAndRegisterApplication.exception.ApiException;
import pl.zielinski.SimpleLoginAndRegisterApplication.provider.TokenProvider;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;

/**
 * @author rafek
 * @version 1.0
 * @licence ask rafekzielinski@wp.pl
 * @since 13/01/2024
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class CustomAuthorizationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private final String AUTHORIZATION = "Authorization";
    private final String TOKEN_PREFIX = "Bearer ";
    private final String HTTP_OPTIONS_METHOD = "OPTIONS";
    private final String EMPTY = "'";
    private final String[] PUBLIC_ROUTES = {
            "/user/login/**",
            "/user/register/**"
    };

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = getToken(request);
            Long userId = getUserId(request);
            if(tokenProvider.isTokenValid(userId, token)) {
                List<GrantedAuthority> authorities = tokenProvider.getAuthorities(token);
                Authentication authentication = tokenProvider.getAuthentication(userId, authorities, request);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                SecurityContextHolder.clearContext();
            }
            filterChain.doFilter(request, response);
        }catch(Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("Error");
        }
        }

    private String getToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(AUTHORIZATION))
                .filter(header -> header.startsWith(TOKEN_PREFIX))
                .map(token -> token.replace(TOKEN_PREFIX, EMPTY)).get();
    }

    @Override
        protected boolean shouldNotFilter (HttpServletRequest request) throws ServletException {
            return request.getHeader(AUTHORIZATION) == null ||
                    !request.getHeader(AUTHORIZATION).startsWith(TOKEN_PREFIX) ||
                    request.getMethod().equalsIgnoreCase(HTTP_OPTIONS_METHOD) ||
                    asList(PUBLIC_ROUTES).contains(request.getRequestURI());

        }

    private Long getUserId(HttpServletRequest request) {
        return tokenProvider.getSubject(getToken(request), request);
    }
}
