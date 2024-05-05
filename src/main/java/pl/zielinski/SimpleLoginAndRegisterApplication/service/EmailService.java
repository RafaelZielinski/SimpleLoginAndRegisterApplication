package pl.zielinski.SimpleLoginAndRegisterApplication.service;

/**
 * @author rafek
 * @version 1.0
 * @licence ask rafekzielinski@wp.pl
 * @since 05/05/2024
 */
public interface EmailService {
    void sendSimpleMailMessage(String to, String name, String token);
}
