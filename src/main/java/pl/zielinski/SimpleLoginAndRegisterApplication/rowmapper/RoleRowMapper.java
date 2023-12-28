package pl.zielinski.SimpleLoginAndRegisterApplication.rowmapper;

import org.springframework.jdbc.core.RowMapper;
import pl.zielinski.SimpleLoginAndRegisterApplication.domain.Role;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author rafek
 * @version 1.0
 * @licence ask rafekzielinski@wp.pl
 * @since 25/12/2023
 */
public class RoleRowMapper implements RowMapper<Role> {

    @Override
    public Role mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Role.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .permission(rs.getString("permission"))
                .build();
    }
}
