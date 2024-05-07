package pl.zielinski.SimpleLoginAndRegisterApplication.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import pl.zielinski.SimpleLoginAndRegisterApplication.exception.ApiException;
import pl.zielinski.SimpleLoginAndRegisterApplication.service.EmailService;
import pl.zielinski.SimpleLoginAndRegisterApplication.utils.EmailUtils;

import static pl.zielinski.SimpleLoginAndRegisterApplication.utils.EmailUtils.getEmailMessage;

/**
 * @author rafek
 * @version 1.0
 * @licence ask rafekzielinski@wp.pl
 * @since 05/05/2024
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {
    @Value("${spring.mail.host}")
    private String host;
    @Value("${spring.mail.username}")
    private String fromEmail;
    private final JavaMailSender mailSender;

    @Async()
    @Override
    public void sendSimpleMailMessage(String to, String name, String verificationUrl) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setSubject("New user account verification");
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setText(getEmailMessage(name, fromEmail, verificationUrl));
            mailSender.send(message);
        } catch (Exception e) {
            throw new ApiException(e.getMessage());
        }
    }
}
