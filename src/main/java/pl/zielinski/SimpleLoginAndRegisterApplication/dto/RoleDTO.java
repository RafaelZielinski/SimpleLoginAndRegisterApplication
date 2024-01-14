package pl.zielinski.SimpleLoginAndRegisterApplication.dto;

import lombok.*;

/**
 * @author rafek
 * @version 1.0
 * @licence ask rafekzielinski@wp.pl
 * @since 02/01/2024
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO {
        Long id;
        String name;
        String permission;
}
