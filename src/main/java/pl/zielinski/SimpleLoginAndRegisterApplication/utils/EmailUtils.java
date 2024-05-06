package pl.zielinski.SimpleLoginAndRegisterApplication.utils;

/**
 * @author rafek
 * @version 1.0
 * @licence ask rafekzielinski@wp.pl
 * @since 06/05/2024
 */
public class EmailUtils {

    public static String getEmailMessage(String name, String host, String token) {
        return "Hello " + name + ",\n\nYour new account has been created. Please click the link below to verify your account. \n\n" +
                "code to verify your account";
    }
}
