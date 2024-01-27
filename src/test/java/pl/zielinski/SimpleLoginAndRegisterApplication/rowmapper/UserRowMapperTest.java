package pl.zielinski.SimpleLoginAndRegisterApplication.rowmapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.zielinski.SimpleLoginAndRegisterApplication.domain.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

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
        when(resultSet.getString("first_name")).thenReturn("Rafał");
        when(resultSet.getString("last_name")).thenReturn("Zieliński");
        when(resultSet.getString("email")).thenReturn("rafekzielinski@wp.pl");
        when(resultSet.getLong("age")).thenReturn(26L);
        when(resultSet.getString("password")).thenReturn("password");
        when(resultSet.getBoolean("enabled")).thenReturn(true);
        when(resultSet.getBoolean("non_locked")).thenReturn(true);
        when(resultSet.getBoolean("using_mfa")).thenReturn(false);
        when(resultSet.getTimestamp("created_date"))
                .thenReturn(Timestamp.valueOf
                        (LocalDateTime.of(2023, 12, 1, 11, 30, 23, 508157)));

        User user = userRowMapper.mapRow(resultSet, 1);

        assertEquals(user.getId(), 1L);
        assertEquals(user.getFirstName(), "Rafał");
        assertEquals(user.getLastName(), "Zieliński");
        assertEquals(user.getEmail(), "rafekzielinski@wp.pl");
        assertEquals(user.getAge(), 26L);
        assertEquals(user.getPassword(), "password");
        assertEquals(user.isEnabled(), true);
        assertEquals(user.isNotLocked(), true);
        assertEquals(user.isUsingMfa(), false);
        assertEquals(user.getCreatedAt(),
                (LocalDateTime.of(2023, 12, 1, 11, 30, 23, 508157)));

    }


}