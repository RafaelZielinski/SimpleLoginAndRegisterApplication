package pl.zielinski.SimpleLoginAndRegisterApplication.repository.impl.database_layer_test;

import pl.zielinski.SimpleLoginAndRegisterApplication.domain.User;
import pl.zielinski.SimpleLoginAndRegisterApplication.dto.UserDTO;

/**
 * @author rafek
 * @version 1.0
 * @licence ask rafekzielinski@wp.pl
 * @since 06/03/2024
 */
public interface SQLProvider {

    default String chooseUserByUserIdFromAccountVerifications() {
        return """
                SELECT * FROM Users u JOIN AccountVerifications av ON u.id = av.user_id WHERE u.id = ?;
                """;
    }

    default String deleteDataAccountVerification() {
        return """
                DELETE FROM AccountVerifications
                """;
    }

    default String deleteDataTwoFactorVerifications() {
        return """
                DELETE FROM TwoFactorVerifications;
                """;
    }

    default String fillDataAccountVerifications() {
        return """
                INSERT INTO AccountVerifications(id, user_id, url) VALUES(1, 3, 'http://localhost/users/verify/account/key1');
                INSERT INTO AccountVerifications(id, user_id, url) VALUES(2, 2, 'http://localhost/users/verify/account/key2');
                INSERT INTO AccountVerifications(id, user_id, url) VALUES(3, 1, 'key3');
                """;
    }

    default String deleteDataRoles() {
        return """
                DELETE FROM Roles;
                """;
    }
    default String deleteDataUser() {
        return """
                DELETE FROM Users;
                """;
    }

    default String deleteDataUserRoles() {
        return """
                DELETE FROM UserRoles;
                """;
    }

    default String fillDataRoles() {
        return """
                INSERT INTO ROLES (id, name, permission)
                VALUES (1, 'ROLE_USER', 'READ:USER,READ:CUSTOMER'),
                (2, 'ROLE_MANAGER', 'READ:USER,READ:CUSTOMER,UPDATE:USER,UPDATE:CUSTOMER'),
                (3, 'ROLE_ADMIN', 'READ:USER,READ:CUSTOMER,CREATE:USER,CREATE:CUSTOMER,UPDATE:USER,UPDATE:CUSTOMER'),
                (4, 'ROLE_SYSADMIN',
                'READ:USER:READ:CUSTOMER,CREATE:USER,CREATE:CUSTOMER,UPDATE:USER,UPDATE:CUSTOMER,DELETE:USER,DELETE:CUSTOMER');          
                """;
    }

    default String fillDataUser() {
        return """
                INSERT INTO Users(id, first_name, last_name, email, age, password, enabled, non_locked, using_mfa)
                VALUES(1, 'Rafał', 'Zieliński', 'rafekzielinski@wp.pl', 26, 'password', true, true, false);
                """;
    }

    default String fillUserRoles() {
        return """
                INSERT INTO UserRoles(id, user_id, role_id)
                VALUES(1, 1, 1);
                """;
    }

    default String fillDataTwoFactorVerifications() {
        return """
                 INSERT INTO TwoFactorVerifications(id, user_id,code,expire_date) VALUES(1, 2, 'code1', '2028-05-03T18:12:52');
                 
                 INSERT INTO TwoFactorVerifications(id, user_id,code,expire_date) VALUES(2, 1, 'code2', '2028-05-03T18:17:32');
                 
                 INSERT INTO TwoFactorVerifications(id, user_id,code,expire_date) VALUES(3, 3, 'code3', '2028-05-03T18:12:45');
                """;
    }

    default String fillFourUsers() {
        return """
                INSERT INTO Users(id, first_name, last_name, email, age, password, enabled, non_locked, using_mfa)
                VALUES(1, 'Rafał', 'Zieliński', 'rafekzielinski@wp.pl', 26, 'password1', true, true, false);
                
                INSERT INTO Users(id, first_name, last_name, email, age, password, enabled, non_locked, using_mfa)
                VALUES(2, 'Kamil', 'Górski', 'kamilgorski@wp.pl', 28, 'password2', true, true, false);
                
                INSERT INTO Users(id, first_name, last_name, email, age, password, enabled, non_locked, using_mfa)
                VALUES(3, 'Marek', 'Chytła', 'marekchytla@wp.pl', 33, 'password3', true, true, false);
                
                INSERT INTO Users(id, first_name, last_name, email, age, password, enabled, non_locked, using_mfa)
                VALUES(4, 'Sabina', 'Woźna', 'sabinawozna@wp.pl', 55, 'password4', true, true, false);
                """;
    }


    default User afterUpdating() {
        return User.builder()
                .id(1L)
                .firstName("Czerepach")
                .lastName("Kieroński")
                .email("czerepachkieronski@wp.pl")
                .password("butter")
                .age(28L)
                .enabled(false)
                .isNotLocked(false)
                .isUsingMfa(true)
                .build();
    }

    default User beforeUpdating() {
        return User.builder()
                .id(1L)
                .firstName("Rafał")
                .lastName("Zieliński")
                .email("rafekzielinski@wp.pl")
                .password("password")
                .age(26L)
                .enabled(true)
                .isNotLocked(true)
                .isUsingMfa(false)
                .build();
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
