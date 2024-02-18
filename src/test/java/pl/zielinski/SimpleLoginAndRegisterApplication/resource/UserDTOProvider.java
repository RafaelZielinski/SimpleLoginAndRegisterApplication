package pl.zielinski.SimpleLoginAndRegisterApplication.resource;

import pl.zielinski.SimpleLoginAndRegisterApplication.dto.UserDTO;

import java.time.LocalDateTime;

/**
 * @author rafek
 * @version 1.0
 * @licence ask rafekzielinski@wp.pl
 * @since 17/02/2024
 */
public interface UserDTOProvider {
    default UserDTO first() {
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

    default UserDTO second() {
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
}