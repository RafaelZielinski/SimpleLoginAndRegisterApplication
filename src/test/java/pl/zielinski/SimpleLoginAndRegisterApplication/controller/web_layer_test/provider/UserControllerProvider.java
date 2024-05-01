package pl.zielinski.SimpleLoginAndRegisterApplication.controller.web_layer_test.provider;

import pl.zielinski.SimpleLoginAndRegisterApplication.domain.Role;
import pl.zielinski.SimpleLoginAndRegisterApplication.domain.User;
import pl.zielinski.SimpleLoginAndRegisterApplication.dto.RoleDTO;
import pl.zielinski.SimpleLoginAndRegisterApplication.dto.UserDTO;

import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;

/**
 * @author rafek
 * @version 1.0
 * @licence ask rafekzielinski@wp.pl
 * @since 17/02/2024
 */
public interface UserControllerProvider {
    default UserDTO firstUserDTO() {
        return UserDTO.builder()
                .id(1L)
                .firstName("Rafał")
                .lastName("Zieliński")
                .email("rafekzielinski@wp.pl")
                .age("15")
                .enabled(true)
                .isNotLocked(true)
                .isUsingMfa(false)
                .createdAt(LocalDateTime.now())
                .roleName("ROLE_USER")
                .permissions("READ:USER,READ:CUSTOMER")
                .build();
    }

    default UserDTO secondUserDTO() {
        return UserDTO.builder()
                .id(2L)
                .firstName("Marek")
                .lastName("Zieliński")
                .email("marekzielinski@wp.pl")
                .age("25")
                .enabled(true)
                .isNotLocked(true)
                .isUsingMfa(true)
                .createdAt(LocalDateTime.now())
                .roleName("ROLE_MANAGER")
                .permissions("READ:USER,READ:CUSTOMER,UPDATE:USER,UPDATE:CUSTOMER")
                .build();
    }

    default UserDTO thirdUserDTO() {
        return UserDTO.builder()
                .id(3l)
                .firstName("Edward")
                .lastName("Klepko")
                .email("edwardklepko69@wp.pl")
                .age("44")
                .enabled(false)
                .isNotLocked(false)
                .isUsingMfa(false)
                .createdAt(LocalDateTime.now())
                .roleName("ROLE_SYSADMIN")
                .permissions("READ:USER:READ:CUSTOMER,CREATE:USER,CREATE:CUSTOMER,UPDATE:USER,UPDATE:CUSTOMER,DELETE:USER,DELETE:CUSTOMER")
                .build();
    }

    default RoleDTO firstRoleDTO() {
        return new RoleDTO(1L, "ROLE_USER", "READ:USER,READ:CUSTOMER");
    }

    default User firstUser() {
        return new User(1L, "Rafał", "Zieliński", "rafekzielinski@wp.pl", 15L, "password", true, true, false, now(), null);
    }

    default Role firstRole() {
        return new Role(1L, "ROLE_USER", "READ:USER,READ:CUSTOMER");
    }
}
