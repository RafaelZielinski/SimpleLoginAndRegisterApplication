package pl.zielinski.SimpleLoginAndRegisterApplication.exception;

/**
 * @author rafek
 * @version 1.0
 * @licence ask rafekzielinski@wp.pl
 * @since 25/12/2023
 */
public class ApiException extends RuntimeException {
    public ApiException(String message) {
        super(message);
    }
}
