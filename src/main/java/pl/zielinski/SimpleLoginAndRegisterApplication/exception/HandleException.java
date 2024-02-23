package pl.zielinski.SimpleLoginAndRegisterApplication.exception;

import com.auth0.jwt.exceptions.JWTDecodeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.zielinski.SimpleLoginAndRegisterApplication.domain.HttpResponse;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.LocalTime.now;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

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
        log.error(ex.getMessage());
        return new ResponseEntity<>(HttpResponse.builder()
                .timeStamp(now().toString())
                .reason(ex.getMessage())
                .developerMessage(ex.getMessage())
                .status(HttpStatus.resolve(statusCode.value()))
                .statusCode(statusCode.value())
                .build(), statusCode);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.error(ex.getMessage());
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        String fields = fieldErrors.stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(", "));
        return new ResponseEntity<>(HttpResponse.builder()
                .timeStamp(now().toString())
                .reason(fields)
                .developerMessage(ex.getMessage())
                .status(HttpStatus.resolve(status.value()))
                .statusCode(status.value())
                .build(), status);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<HttpResponse> SQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException exception) {
        log.error(exception.getMessage());
        return createErrorHttpResponse(BAD_REQUEST, exception.getMessage().contains("Duplicate entry") ? "Information already exists" : exception.getMessage(), exception);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<HttpResponse> BadCredentialsException(BadCredentialsException exception) {
        log.error(exception.getMessage());
        return createErrorHttpResponse(BAD_REQUEST, exception.getMessage() + ", Incorrect email or password", exception);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<HttpResponse> apiException(ApiException exception) {
        log.error(exception.getMessage());
        return createErrorHttpResponse(BAD_REQUEST, exception.getMessage(), exception);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<HttpResponse> accessDeniedException(AccessDeniedException exception) {
        log.error(exception.getMessage());
        return createErrorHttpResponse(HttpStatus.FORBIDDEN, "Access denied. You don\'t have access", exception);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<HttpResponse> exception(Exception exception) {
        log.error(exception.getMessage());
        return createErrorHttpResponse(INTERNAL_SERVER_ERROR, exception.getMessage(), exception);
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<HttpResponse> emptyResultDataAccessException(EmptyResultDataAccessException exception) {
        log.error(exception.getMessage());
        return createErrorHttpResponse(BAD_REQUEST, exception.getMessage(), exception);
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<HttpResponse> disableException(DisabledException exception) {
        log.error(exception.getMessage());
        return createErrorHttpResponse(BAD_REQUEST, "User account is currently disabled", exception);
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<HttpResponse> lockedException(LockedException exception) {
        log.error(exception.getMessage());
        return createErrorHttpResponse(BAD_REQUEST, exception.getMessage() + " to many failed attempts.", exception);
    }

    @ExceptionHandler(JWTDecodeException.class)
    public ResponseEntity<HttpResponse> exception(JWTDecodeException exception) {
        log.error(exception.getMessage());
        return createErrorHttpResponse(INTERNAL_SERVER_ERROR, "Could not decode the token", exception);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<HttpResponse> dataAccessException(DataAccessException exception) {
        log.error(exception.getMessage());
        return createErrorHttpResponse(BAD_REQUEST, processErrorMessage(exception.getMessage()), exception);
    }

    private ResponseEntity<HttpResponse> createErrorHttpResponse(HttpStatus status, String reason, Exception exception) {
        log.error(exception.getMessage());
        return new ResponseEntity<>(HttpResponse.builder()
                .timeStamp(LocalTime.now().toString())
                .developerMessage(exception.getMessage())
                .reason(reason)
                .status(status)
                .statusCode(status.value())
                .build(), status);
    }

    private String processErrorMessage(String errorMessage) {
        if (errorMessage != null) {
            if (errorMessage.contains("Duplicate entry") && errorMessage.contains("AccountVerifications")) {
                return "You already verified your account.";
            }
            if (errorMessage.contains("Duplicate entry") && errorMessage.contains("ResetPasswordVerifications")) {
                return "We already sent you an email to reset your password.";
            }
            if (errorMessage.contains("Duplicate entry")) {
                return "Duplicate entry. Please try again.";
            }
        }
        return "Some error occurred";
    }
}
