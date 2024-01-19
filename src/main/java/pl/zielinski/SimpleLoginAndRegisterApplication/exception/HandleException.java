package pl.zielinski.SimpleLoginAndRegisterApplication.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.zielinski.SimpleLoginAndRegisterApplication.domain.HttpResponse;

import static java.time.LocalTime.now;

/**
 * @author rafek
 * @version 1.0
 * @licence ask rafekzielinski@wp.pl
 * @since 19/01/2024
 */
@Slf4j
@RestControllerAdvice
public class HandleException extends ResponseEntityExceptionHandler implements ErrorController {
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        return new ResponseEntity<>(HttpResponse.builder()
                .timeStamp(now().toString())
                .reason(ex.getMessage())
                .developerMessage(ex.getMessage())
                .status(HttpStatus.resolve(statusCode.value()))
                .statusCode(statusCode.value())
                .build(), statusCode);
    }


}
