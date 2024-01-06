package pl.zielinski.SimpleLoginAndRegisterApplication.rowmapper;

import org.springframework.jdbc.core.RowMapper;
import pl.zielinski.SimpleLoginAndRegisterApplication.domain.User;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author rafek
 * @version 1.0
 * @licence ask rafekzielinski@wp.pl
 * @since 06/01/2024
 */
public class UserRowMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return User.builder()
                .id(resultSet.getLong("id"))
                .firstName(resultSet.getString("first_name"))
                .lastName(resultSet.getString("last_name"))
                .email(resultSet.getString("email"))
                .age(resultSet.getLong("age"))
                .password(resultSet.getString("password"))
                .enabled(resultSet.getBoolean("enabled"))
                .isNotLocked(resultSet.getBoolean("non_locked"))
                .isUsingMfa(resultSet.getBoolean("using_mfa"))
                .createdAt(resultSet.getTimestamp("created_date").toLocalDateTime())
                .build();    }
}
