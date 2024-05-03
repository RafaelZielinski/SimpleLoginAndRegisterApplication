package pl.zielinski.SimpleLoginAndRegisterApplication.repository.impl.database_test;

import com.twilio.Twilio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import pl.zielinski.SimpleLoginAndRegisterApplication.configuration.PasswordConfig;
import pl.zielinski.SimpleLoginAndRegisterApplication.domain.User;
import pl.zielinski.SimpleLoginAndRegisterApplication.dto.UserDTO;
import pl.zielinski.SimpleLoginAndRegisterApplication.exception.ApiException;
import pl.zielinski.SimpleLoginAndRegisterApplication.repository.UserRepository;
import pl.zielinski.SimpleLoginAndRegisterApplication.repository.impl.RoleRepositoryImpl;
import pl.zielinski.SimpleLoginAndRegisterApplication.repository.impl.UserRepositoryImpl;
import pl.zielinski.SimpleLoginAndRegisterApplication.service.SmsService;
import pl.zielinski.SimpleLoginAndRegisterApplication.service.impl.SmsServiceImpl;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
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
@Import({UserRepositoryImpl.class, PasswordConfig.class, RoleRepositoryImpl.class, SmsServiceImpl.class})
class UserRepositoryImplTest implements SQLProvider {

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

    void deleteTwoFactorVerifications() throws SQLException {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(deleteDataTwoFactorVerifications());
        }
    }

    void insertFourUsers() throws SQLException {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(deleteDataUser());
            statement.execute(fillFourUsers());
        }
    }

    void insertThreeAccountVerification() throws SQLException {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(deleteDataAccountVerification());
            statement.execute(fillDataAccountVerifications());
        }

    }

    void insertDataToTwoFactorVerifications() throws SQLException {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(deleteDataTwoFactorVerifications());
            statement.execute(fillDataTwoFactorVerifications());
        }
    }

    int getCountOfTwoFactorVerificationsRecords() {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT COUNT (*) FROM TwoFactorVerifications");
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
        return 0;
    }

    @DisplayName("Testing method getUserByEmail(String email)")
    @Test
    void it_should_return_success_user() throws SQLException {
        //given
        insertFourDataRoles();
        insertFourUsers();
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
        insertFourUsers();
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
        insertFourUsers();
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

    @DisplayName("Testing method updateUser(User user)")
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

    @DisplayName("Testing method get(Long id)")
    @Test
    void it_should_return_user_from_database() throws SQLException {
        //given
        insertFourDataRoles();
        insertFourUsers();
        Long expectedId = 1L;
        //when
        User actual = cut.get(expectedId);
        //then
        assertEquals("rafekzielinski@wp.pl", actual.getEmail());
        assertEquals("Zieliński", actual.getLastName());
        assertEquals(1L, actual.getId());
        assertEquals("Rafał", actual.getFirstName());
        assertEquals("password1", actual.getPassword());
    }

    @DisplayName("Testing method get(Long id)")
    @Test
    void it_should_throw_exception_there_is_no_user_found_from_database() throws SQLException {
        //given
        insertFourDataRoles();
        deleteUsers();
        Long expectedId = 1L;
        //when
        ApiException actual = assertThrows(ApiException.class, () -> cut.get(expectedId));
        //then
        assertEquals("There is no such an user at database exists", actual.getMessage());
    }

    @DisplayName("Testing method list()")
    @Test
    void it_should_return_empty_user_list() throws SQLException {
        //given
        insertFourDataRoles();
        deleteUsers();
        //when
        Collection<User> actual = cut.list();
        //then
        assertTrue(actual.isEmpty());
    }

    @DisplayName("Testing method list()")
    @Test
    void it_should_return_four_users_list() throws SQLException {
        //given
        insertFourDataRoles();
        insertFourUsers();
        //when
        Collection<User> actual = cut.list();
        //then
        assertEquals(4, actual.size());
    }

    @DisplayName("Testing method create(User user)")
    @Test
    void it_should_create_one_user_from_database() throws SQLException {
        //given
        MockHttpServletRequest request = getMockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        insertFourDataRoles();
        deleteUsers();
        User user = beforeUpdating();
        //when
        User actual = cut.create(user);
        //then
        assertEquals("Rafał", actual.getFirstName());
        assertEquals("Zieliński", actual.getLastName());
        assertEquals(27L, actual.getAge());
        assertFalse(actual.isEnabled());
        assertTrue(actual.isNotLocked());
        assertFalse(actual.isUsingMfa());
    }

    @DisplayName("Testing method create(User user)")
    @Test
    void it_should_throw_exception_there_is_email_already_in_database_during_creating_user() throws SQLException {
        //given
        insertFourDataRoles();
        insertFourUsers();
        User user = beforeUpdating();
        //when
        ApiException actual = assertThrows(ApiException.class, () -> cut.create(user));
        //then
        assertEquals("There is already taken that email", actual.getMessage());
    }

    @DisplayName("Testing method verifyAccountKey(String key)")
    @Test
    void it_should_verify_account_with_success() throws SQLException {
        //given
        MockHttpServletRequest request = getMockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        insertFourUsers();
        insertFourDataRoles();
        insertThreeAccountVerification();
        //when
        User actual = cut.verifyAccountKey("key1");
        //then
        assertEquals(actual.getId(), 3L);
    }

    @DisplayName("Testing method verifyAccountKey(String key)")
    @Test
    void it_should_verify_account_with_fail_and_throw_this_link_is_not_valid() throws SQLException {
        //given
        MockHttpServletRequest request = getMockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        insertFourUsers();
        insertFourDataRoles();
        insertThreeAccountVerification();
        //when
        ApiException actual = assertThrows(ApiException.class, () -> cut.verifyAccountKey("key3"));
        //then
        assertEquals("This link is not valid.", actual.getMessage());
    }

    @DisplayName("Testing method sendVerification(UserDto user)")
    @Test
    void it_should_send_notification_and_write_in_database_line_in_two_factor_verification() throws SQLException {
        //given
        insertFourUsers();
        insertFourDataRoles();
        deleteTwoFactorVerifications();
        //when
        int before = getCountOfTwoFactorVerificationsRecords();
        cut.sendVerificationCode(firstUserDTO(), "code4");
        int after = getCountOfTwoFactorVerificationsRecords();
        //then
        assertEquals(0, before);
        assertEquals(1, after);

    }

    @DisplayName("Testing method sendVerification(UserDto user)")
    @Test
    void it_should_throw_error_in_two_factor_verifications() throws SQLException {
        //given
        deleteUsers();
        deleteTwoFactorVerifications();
        //when
        ApiException actual = assertThrows(ApiException.class, () -> cut.sendVerificationCode(firstUserDTO(), "code1234"));
        //then
        assertEquals("An error occurred. Please try again.", actual.getMessage());

    }


    private static MockHttpServletRequest getMockHttpServletRequest() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setScheme("http");
        request.setServerName("localhost");
        request.setServerPort(-1);
        request.setRequestURI("/example.com");
        return request;
    }

}