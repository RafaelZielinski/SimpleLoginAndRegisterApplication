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
                INSERT INTO Users (
                id, first_name, last_name, email, age, password, enabled, non_locked, using_mfa)
                VALUES(
                1, 'Rafał', 'Zieliński', 'rafekzielinski@wp.pl', 26,
                '$2a$20$s521GVdPMTPsNU6tF5sISODRuJQH0rw3A5Fx8xjG8MC1QO60VNIXq', true, true, false);             
                """;
    }
}
