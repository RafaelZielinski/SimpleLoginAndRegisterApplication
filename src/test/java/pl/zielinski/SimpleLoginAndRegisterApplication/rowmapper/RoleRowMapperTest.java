package pl.zielinski.SimpleLoginAndRegisterApplication.rowmapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.zielinski.SimpleLoginAndRegisterApplication.domain.Role;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class RoleRowMapperTest {

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @InjectMocks
    RoleRowMapper roleRowMapper;

    @Mock
    private ResultSet resultSet;

    @Test
    public void it_should_return_valid_map_row() throws SQLException {
        when(resultSet.getLong("id")).thenReturn(1L);
        when(resultSet.getString("name")).thenReturn("ROLE_USER");
        when(resultSet.getString("permission")).thenReturn("READ:USER,READ:CUSTOMER");

        Role role = roleRowMapper.mapRow(resultSet, 1);

        assertEquals(role.getId(), 1L);
        assertEquals(role.getName(), "ROLE_ USER");
        assertEquals(role.getPermission(), "READ:USER,READ:CUSTOMER");
    }


}