package pl.zielinski.SimpleLoginAndRegisterApplication.service;

/**
 * @author rafek
 * @version 1.0
 * @licence ask rafekzielinski@wp.pl
 * @since 02/05/2024
 */
public interface SmsService {
    void sendSms(String to, String messageBody);
}
