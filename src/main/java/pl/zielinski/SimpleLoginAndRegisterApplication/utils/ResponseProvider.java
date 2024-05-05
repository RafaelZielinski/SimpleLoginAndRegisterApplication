package pl.zielinski.SimpleLoginAndRegisterApplication.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.zielinski.SimpleLoginAndRegisterApplication.domain.HttpResponse;

import java.time.LocalTime;

/**
 * @author rafek
 * @version 1.0
 * @licence ask rafekzielinski@wp.pl
 * @since 04/05/2024
 */
public class ResponseProvider {
    public static ResponseEntity<HttpResponse> createResponseEntity(HttpStatus status, String reason, Exception exception) {
        return new ResponseEntity<>(HttpResponse.builder()
                .timeStamp(LocalTime.now().toString())
                .developerMessage(exception.getMessage())
                .reason(reason)
                .status(status)
                .statusCode(status.value())
                .build(), status);
    }
}
