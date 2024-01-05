package pl.zielinski.SimpleLoginAndRegisterApplication.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author rafek
 * @version 1.0
 * @licence ask rafekzielinski@wp.pl
 * @since 02/01/2024
 */
@Getter
@Setter

public class RoleDTO {
        Long id;
        String name;
        String permission;
}
