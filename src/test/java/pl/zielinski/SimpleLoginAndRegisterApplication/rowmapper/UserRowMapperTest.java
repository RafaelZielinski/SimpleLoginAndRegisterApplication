package pl.zielinski.SimpleLoginAndRegisterApplication.rowmapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.zielinski.SimpleLoginAndRegisterApplication.domain.Role;
import pl.zielinski.SimpleLoginAndRegisterApplication.domain.User;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class UserRowMapperTest {

    @Mock
    private ResultSet resultSet;

    @InjectMocks
    UserRowMapper userRowMapper;

    @BeforeEach
    public void setUp() {
    MockitoAnnotations.openMocks(this);

    }

    @Test
    public void it_should_return_valid_map_row() throws SQLException {
        when(resultSet.getLong("id")).thenReturn(1L);
        when(resultSet.getString("name")).thenReturn("ROLE_USER");
        when(resultSet.getString("permission")).thenReturn("READ:USER,READ:CUSTOMER");

        User user = userRowMapper.mapRow(resultSet, 1);

//        assertEquals(user.getId(), 1L);
//        assertEquals(user.getName(), "ROLE_ USER");
//        assertEquals(user.getPermission(), "READ:USER,READ:CUSTOMER");
    }



}