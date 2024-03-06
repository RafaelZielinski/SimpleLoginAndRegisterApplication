package pl.zielinski.SimpleLoginAndRegisterApplication.repository.impl.database_test;

/**
 * @author rafek
 * @version 1.0
 * @licence ask rafekzielinski@wp.pl
 * @since 06/03/2024
 */
public interface RoleProvider {

    default String deleteData() {
        return """
                DELETE FROM Roles;
                """;
    }

    default String fillData() {
        return """
                INSERT INTO ROLES (id, name, permission)
                VALUES (1, 'ROLE_USER', 'READ:USER,READ:CUSTOMER'),
                (2, 'ROLE_MANAGER', 'READ:USER,READ:CUSTOMER,UPDATE:USER,UPDATE:CUSTOMER'),
                (3, 'ROLE_ADMIN', 'READ:USER,READ:CUSTOMER,CREATE:USER,CREATE:CUSTOMER,UPDATE:USER,UPDATE:CUSTOMER'),
                (4, 'ROLE_SYSADMIN',
                'READ:USER:READ:CUSTOMER,CREATE:USER,CREATE:CUSTOMER,UPDATE:USER,UPDATE:CUSTOMER,DELETE:USER,DELETE:CUSTOMER');           
                """;
    }

}
