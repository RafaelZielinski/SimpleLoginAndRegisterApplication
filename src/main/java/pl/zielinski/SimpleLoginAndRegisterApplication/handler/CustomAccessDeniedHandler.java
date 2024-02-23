package pl.zielinski.SimpleLoginAndRegisterApplication.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import pl.zielinski.SimpleLoginAndRegisterApplication.domain.HttpResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;

import static java.time.LocalDateTime.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @author rafek
 * @version 1.0
 * @licence ask rafekzielinski@wp.pl
 * @since 10/01/2024
 */
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        System.out.println(accessDeniedException.getMessage());
        HttpResponse httpResponse = HttpResponse.builder()
                .timeStamp(now().toString())
                .reason("You don't have enough permission")
                .status(FORBIDDEN)
                .statusCode(FORBIDDEN.value())
                .build();
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setStatus(FORBIDDEN.value());
        OutputStream out = response.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(out, httpResponse);
        out.flush();
    }
}
