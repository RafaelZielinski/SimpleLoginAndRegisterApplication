package pl.zielinski.SimpleLoginAndRegisterApplication.repository.impl.provider;

import pl.zielinski.SimpleLoginAndRegisterApplication.domain.Role;

/**
 * @author rafek
 * @version 1.0
 * @licence ask rafekzielinski@wp.pl
 * @since 31/01/2024
 */
public interface RoleProvider {
    default Role firstRole() {
        return new Role(1L, "ROLE_USER", "READ:USER,READ:CUSTOMER");
    }

    default Role secondRole() {
        return new Role(2L, "ROLE_MANAGER", "READ:USER,READ:CUSTOMER,UPDATE:USER,UPDATE:CUSTOMER");
    }

    default Role thirdRole() {
        return new Role(3L, "ROLE_ADMIN", "READ:USER,READ:CUSTOMER,CREATE:USER,CREATE:CUSTOMER,UPDATE:USER,UPDATE:CUSTOMER");
    }
}
