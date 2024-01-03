package pl.zielinski.SimpleLoginAndRegisterApplication.dto;

import java.time.LocalDateTime;

/**
 * @author rafek
 * @version 1.0
 * @licence ask rafekzielinski@wp.pl
 * @since 02/01/2024
 */

public class UserDTO{
        Long id;
        String firstName;
        String lastName;
        String email;
        String age;
        Boolean enabled;
        Boolean isNotLocked;
        Boolean isUsingMfa;
        LocalDateTime createdAt;
        }
