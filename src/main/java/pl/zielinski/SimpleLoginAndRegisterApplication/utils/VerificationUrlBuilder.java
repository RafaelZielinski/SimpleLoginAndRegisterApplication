package pl.zielinski.SimpleLoginAndRegisterApplication.utils;

/**
 * @author rafek
 * @version 1.0
 * @licence ask rafekzielinski@wp.pl
 * @since 24/03/2024
 */
public interface VerificationUrlBuilder {
    String buildVerificationUrl(String key, String type);
}
