package pl.zielinski.SimpleLoginAndRegisterApplication.controller.web_layer_test.provider;

import pl.zielinski.SimpleLoginAndRegisterApplication.dto.RoleDTO;

/**
 * @author rafek
 * @version 1.0
 * @licence ask rafekzielinski@wp.pl
 * @since 05/02/2024
 */
public interface RoleControllerProvider {

    default RoleDTO firstRoleDTO() {
        return new RoleDTO(1L, "ROLE_USER", "READ:USER,READ:CUSTOMER");
    }

    default RoleDTO secondRoleDTO() {
        return new RoleDTO(2L, "ROLE_MANAGER", "READ:USER,READ:CUSTOMER,UPDATE:USER,UPDATE:CUSTOMER");
    }
}
