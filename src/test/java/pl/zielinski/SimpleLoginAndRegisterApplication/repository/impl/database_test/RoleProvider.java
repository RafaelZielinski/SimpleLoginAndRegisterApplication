package pl.zielinski.SimpleLoginAndRegisterApplication.repository.impl.database_test;

/**
 * @author rafek
 * @version 1.0
 * @licence ask rafekzielinski@wp.pl
 * @since 06/03/2024
 */
public interface RoleProvider {

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

}
