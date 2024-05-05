package pl.zielinski.SimpleLoginAndRegisterApplication.service.impl;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.zielinski.SimpleLoginAndRegisterApplication.service.SmsService;

/**
 * @author rafek
 * @version 1.0
 * @licence ask rafekzielinski@wp.pl
 * @since 02/05/2024
 */
@Slf4j
@Service
public class SmsServiceImpl implements SmsService {

    @Value(value = "${twilio.from.number}")
    private String FROM_NUMBER;

    @Value(value = "${twilio.sid.key}")
    private String SID_KEY;

    @Value(value = "${twilio.token.key}")
    private String TOKEN_KEY;


    @PostConstruct
    void init() {
        Twilio.init(SID_KEY, TOKEN_KEY);
    }

    @Override
    public void sendSms(String to, String messageBody) {
        Message message = Message.creator(
                        new PhoneNumber(to),
                        new PhoneNumber(FROM_NUMBER),
                        messageBody)
                .create();
        log.info(message.toString());
    }
}
