package pl.zielinski.SimpleLoginAndRegisterApplication.utils;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author rafek
 * @version 1.0
 * @licence ask rafekzielinski@wp.pl
 * @since 01/05/2024
 */
public class SmsUtils {

    @Value("${twilio.from.number}")
    public static String FROM_NUMBER;
    @Value("${twilio.sid.key}")
    public static String SID_KEY;
    @Value("${twilio.token.key}")
    public static String TOKEN_KEY;

    public static void sendSMS(String to, String messageBody) {

        Twilio.init(SID_KEY, TOKEN_KEY);
        Message message = Message.creator(
                        new PhoneNumber(to),
                        new PhoneNumber(FROM_NUMBER),
                        messageBody)
                        .create();

        System.out.println(message);
    }
}
