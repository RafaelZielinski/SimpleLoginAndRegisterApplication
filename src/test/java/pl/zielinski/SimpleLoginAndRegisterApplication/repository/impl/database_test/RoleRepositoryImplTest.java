package pl.zielinski.SimpleLoginAndRegisterApplication.repository.impl.database_test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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


    void insertData() throws SQLException {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(deleteData());
            statement.execute(fillData());
        }
    }

    void emptyData() throws SQLException {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(deleteData());

        }
    }

    @DisplayName("it_should_return_empty_list_method_list()")
    @Test
    void testList() throws SQLException {
        //given
        emptyData();
        //when
        Collection<Role> actual = cut.list();
        //then
        assertEquals(0, actual.size());
    }


}