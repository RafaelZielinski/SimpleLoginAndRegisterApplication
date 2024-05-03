package pl.zielinski.SimpleLoginAndRegisterApplication.dto;

import lombok.*;

import java.time.LocalDateTime;

/**
 * @author rafek
 * @version 1.0
 * @licence ask rafekzielinski@wp.pl
 * @since 02/01/2024
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class UserDTO{
        private Long id;
        private String firstName;
        private String lastName;
        private String email;
        private Long age;
        private boolean enabled;
        private boolean isNotLocked;
        private boolean isUsingMfa;
        private LocalDateTime createdAt;
        private String phone;
        private String roleName;
        private String permissions;
}
