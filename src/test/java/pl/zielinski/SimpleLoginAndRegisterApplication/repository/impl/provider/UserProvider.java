package pl.zielinski.SimpleLoginAndRegisterApplication.repository.impl.provider;

import pl.zielinski.SimpleLoginAndRegisterApplication.domain.User;
import pl.zielinski.SimpleLoginAndRegisterApplication.dto.UserDTO;

import java.time.LocalDateTime;

/**
 * @author rafek
 * @version 1.0
 * @licence ask rafekzielinski@wp.pl
 * @since 31/01/2024
 */
public interface UserProvider {
    default User firstUser() {
        return new User(
                1L, "Rafał", "Zieliński", "rafekzielinski@wp.pl", 26L,
                "password", true, true, false,
                LocalDateTime.of(2024, 1, 30, 6, 30, 3, 170603900), "+48722145941");
    }

    default User secondUser() {
        return new User(
                2L, "Kamil", "Zieliński", "kamilzielinski@wp.pl", 19L,
                "password", true, true, false,
                LocalDateTime.of(2024, 1, 29, 6, 30, 3, 173333900), null);
    }

    default User thirdUser() {
        return new User(
                3L, "Marek", "Staśko", "marek.stasko69@wp.pl", 15L,
                "password", false, true, false,
                LocalDateTime.of(2024, 1, 29, 6, 30, 3, 173333900), null);
    }

    default UserDTO firstUserDTO() {
        return UserDTO.builder()
                .id(1L)
                .firstName("Rafał")
                .lastName("Zieliński")
                .email("rafekzielinski@wp.pl")
                .age(26L)
                .enabled(true)
                .isNotLocked(true)
                .isUsingMfa(false)
                .phone("+48722145931")
                .build();

    }



}
