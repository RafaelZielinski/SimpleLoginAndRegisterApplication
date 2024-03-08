package pl.zielinski.SimpleLoginAndRegisterApplication.repository.impl.database_test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import pl.zielinski.SimpleLoginAndRegisterApplication.domain.Role;
import pl.zielinski.SimpleLoginAndRegisterApplication.exception.ApiException;
import pl.zielinski.SimpleLoginAndRegisterApplication.repository.RoleRepository;
import pl.zielinski.SimpleLoginAndRegisterApplication.repository.impl.RoleRepositoryImpl;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;

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
class RoleRepositoryImplTest implements RoleProvider{

    @Autowired
    private RoleRepository<Role> cut;

    @Autowired
    private DataSource dataSource;


    void insertDataRoles() throws SQLException {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(deleteDataRoles());
            statement.execute(fillDataRoles());
        }
    }

    void insertDataUsers() throws SQLException {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(deleteDataUser());
            statement.execute(fillDataUser());
        }
    }

    void insertDataUserRoles() throws SQLException {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(deleteDataUserRoles());
            statement.execute(fillUserRoles());
        }
    }
    void emptyData() throws SQLException {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            //you can check it out how efficient is way to run sql command via batch method in this statement
            statement.execute(deleteDataUserRoles());
            statement.execute(deleteDataRoles());
            statement.execute(deleteDataUser());

        }
    }

    @DisplayName("testing_method_list()")
    @Test
    void it_should_return_empty_list() throws SQLException {
        //given
        emptyData();
        //when
        Collection<Role> actual = cut.list();
        //then
        assertEquals(0, actual.size());
    }

    @DisplayName("testing_method_list()")
    @Test
    void it_should_return_four_size_list_of_roles() throws SQLException {
        //given
        emptyData();
        insertDataRoles();

        //when
        Collection<Role> actual = cut.list();
        //then
        assertEquals(4, actual.size());
    }

    @DisplayName("testing_method_get(Long id)")
    @Test
    void it_should_return_correct_one_roles() throws SQLException {
        //given
        emptyData();
        insertDataRoles();
        Long expectedId = 1L;
        //when
        Role actual = cut.get(expectedId);
        //then
        assertEquals(expectedId, actual.getId());
        assertEquals("ROLE_USER", actual.getName());
    }

    @DisplayName("testing_method_get(Long id)")
    @Test
    void it_should_throw_exception_there_is_no_such_role() throws SQLException {
        //given
        emptyData();
        Long expectedId = 1L;
        //when
        ApiException actual = assertThrows(ApiException.class, () -> cut.get(expectedId));
        //then
        assertEquals("There is no such a role", actual.getMessage());
    }

    @DisplayName("testing_method_getRoleByUserId(Long id)")
    @Test
    void it_should_return_role_to_user_successfully() throws SQLException {
        //given
        insertDataRoles();
        insertDataUsers();
        insertDataUserRoles();

        Long expectedId = 1L;
        //when
        Role actual = cut.getRoleByUserId(expectedId);
        //then
        assertEquals("ROLE_USER", actual.getName());
        assertEquals("READ:USER,READ:CUSTOMER", actual.getPermission());
        assertEquals(1L, actual.getId());
    }

    @DisplayName("testing_method_getRoleByUserId(Long id)")
    @Test
    void it_should_throw_exception_there_is_no_role() throws SQLException {
        //given
        emptyData();

        Long expectedId = 1L;
        //when
        ApiException actual = assertThrows(ApiException.class, () -> cut.getRoleByUserId(expectedId));
        //then
        assertEquals("No role found by name: ROLE_USER", actual.getMessage());
    }


}