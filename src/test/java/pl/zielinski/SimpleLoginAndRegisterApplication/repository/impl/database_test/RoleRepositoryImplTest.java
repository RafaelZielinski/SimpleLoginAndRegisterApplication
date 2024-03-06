package pl.zielinski.SimpleLoginAndRegisterApplication.repository.impl.database_test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import pl.zielinski.SimpleLoginAndRegisterApplication.domain.Role;
import pl.zielinski.SimpleLoginAndRegisterApplication.repository.RoleRepository;
import pl.zielinski.SimpleLoginAndRegisterApplication.repository.impl.RoleRepositoryImpl;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.*;

/**
 * @author rafek
 * @version 1.0
 * @licence ask rafekzielinski@wp.pl
 * @since 05/03/2024
 */

@ActiveProfiles("test")
@JdbcTest()
@AutoConfigureTestDatabase(replace = NONE)
@Import(RoleRepositoryImpl.class)
class RoleRepositoryImplTest {

    @Autowired
    private RoleRepository<Role> cut;

    @Autowired
    private DataSource dataSource;


    @BeforeEach
    void setUp() throws SQLException {
        String queryOne = """
                INSERT INTO Users
                    (id, first_name, last_name, email, age, password, enabled, non_locked, using_mfa)
                     VALUES(1, 'Rafał', 'Zieliński', 'rafekzielinski@wp.pl', 26, '$2a$20$s521GVdPMTPsNU6tF5sISODRuJQH0rw3A5Fx8xjG8MC1QO60VNIXq', true, true, false);
                """;
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(queryOne);


        }
    }

    @Test
    void testList() throws SQLException {

    }


}