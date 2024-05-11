package pl.zielinski.SimpleLoginAndRegisterApplication.utils;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * @author rafek
 * @version 1.0
 * @licence ask rafekzielinski@wp.pl
 * @since 06/05/2024
 */
public class PathProvider {
    public static String getVerificationUrl(String key, String type) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path("/users/verify/" + type + "/" + key).toUriString();
    }
}
