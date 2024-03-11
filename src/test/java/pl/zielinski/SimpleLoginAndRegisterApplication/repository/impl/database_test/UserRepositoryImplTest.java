package pl.zielinski.SimpleLoginAndRegisterApplication.repository.impl.database_test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import pl.zielinski.SimpleLoginAndRegisterApplication.configuration.PasswordConfig;
import pl.zielinski.SimpleLoginAndRegisterApplication.domain.User;
import pl.zielinski.SimpleLoginAndRegisterApplication.exception.ApiException;
import pl.zielinski.SimpleLoginAndRegisterApplication.repository.UserRepository;
import pl.zielinski.SimpleLoginAndRegisterApplication.repository.impl.RoleRepositoryImpl;
import pl.zielinski.SimpleLoginAndRegisterApplication.repository.impl.UserRepositoryImpl;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

/**
 * @author rafek
 * @version 1.0
 * @licence ask rafekzielinski@wp.pl
 * @since 09/03/2024
 */

@ActiveProfiles("test")
@JdbcTest()
@AutoConfigureTestDatabase(replace = NONE)
@Import({UserRepositoryImpl.class, PasswordConfig.class, RoleRepositoryImpl.class})
class UserRepositoryImplTest implements RoleProvider {

    @Autowired
    private UserRepository<User> cut;

    @Autowired
    BCryptPasswordEncoder encoder;

    @Autowired
    private DataSource dataSource;

    void insertFourDataRoles() throws SQLException {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(deleteDataRoles());
            statement.execute(fillDataRoles());
        }
    }

    void deleteUsers() throws SQLException {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(deleteDataUser());
        }
    }

    void insertFourUserRoles() throws SQLException {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(deleteDataUser());
            statement.execute(fillFourUsers());
        }
    }

    @DisplayName("Testing method getUserByEmail(String email)")
    @Test
    void it_should_return_success_user() throws SQLException {
        //given
        insertFourDataRoles();
        insertFourUserRoles();
        String expectedEmail = "rafekzielinski@wp.pl";
        //when
        User actual = cut.getUserByEmail(expectedEmail);
        //then
        assertEquals(expectedEmail, actual.getEmail());
        assertEquals("Zieliński", actual.getLastName());
        assertEquals(1L, actual.getId());
        assertEquals("Rafał", actual.getFirstName());
    }

    @DisplayName("Testing method getUserByEmail(String email)")
    @Test
    void it_should_throw_api_exception_no_user_found() throws SQLException {
        //given
        insertFourDataRoles();
        insertFourUserRoles();
        String expectedEmail = "karolinazieba@wp.pl";
        //when
        ApiException actual = assertThrows(ApiException.class, () -> cut.getUserByEmail(expectedEmail));
        //then
        assertEquals("There is no such an user at database exists", actual.getMessage());
    }

    @DisplayName("Testing method updateuser(User user)")
    @Test
    void it_should_return_properly_changed_user_in_database() throws SQLException {
        //given
        insertFourDataRoles();
        insertFourUserRoles();
        User afterUpdated = afterUpdating();

        //when
        User actual = cut.update(afterUpdated);
        //then
        assertEquals("czerepachkieronski@wp.pl", actual.getEmail());
        assertEquals("Kieroński", actual.getLastName());
        assertEquals(1L, actual.getId());
        assertEquals("Czerepach", actual.getFirstName());
        assertEquals("password1", actual.getPassword());
    }

    @DisplayName("Testing method updateuser(User user)")
    @Test
    void it_should_throw_exception_not_found_user() throws SQLException {
        //given
        insertFourDataRoles();
        deleteUsers();

        User afterUpdated = afterUpdating();

        //when
        ApiException actual = assertThrows(ApiException.class, () -> cut.update(afterUpdated));
        //then
        assertEquals("An error occurred. Please try again.", actual.getMessage());

    }




}