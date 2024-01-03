package pl.zielinski.SimpleLoginAndRegisterApplication.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author rafek
 * @version 1.0
 * @licence ask rafekzielinski@wp.pl
 * @since 03/01/2024
 */
@Data
@SuperBuilder
@JsonInclude(Include.NON_DEFAULT)
public class HttpResponse {
    protected String timeStamp;
    protected int statusCode;
    protected HttpStatus status;
    protected String reason;
    protected String message;
    protected String developerMessage;
    protected Map<?, ?> data;

}
