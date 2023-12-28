package pl.zielinski.SimpleLoginAndRegisterApplication.repository.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import pl.zielinski.SimpleLoginAndRegisterApplication.domain.Role;
import pl.zielinski.SimpleLoginAndRegisterApplication.exception.ApiException;
import pl.zielinski.SimpleLoginAndRegisterApplication.repository.RoleRepository;
import pl.zielinski.SimpleLoginAndRegisterApplication.rowmapper.RoleRowMapper;

import java.util.Collection;

import static pl.zielinski.SimpleLoginAndRegisterApplication.query.RoleQuery.SELECT_ROLES_QUERY;

/**
 * @author rafek
 * @version 1.0
 * @licence ask rafekzielinski@wp.pl
 * @since 25/12/2023
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class RoleRepositoryImpl implements RoleRepository<Role> {
    private final NamedParameterJdbcTemplate jdbc;

    @Override
    public Role create(Role data) {
        return null;
    }

    @Override
    public Collection<Role> list() {
        try {
            return jdbc.query(SELECT_ROLES_QUERY, new RoleRowMapper());
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("An error occurred. Please try again");
        }
    }

    @Override
    public Role get(Long id) {
        return null;
    }

    @Override
    public Role update(Long id) {
        return null;
    }

    @Override
    public Boolean delete(Long id) {
        return null;
    }

    @Override
    public void addRoleToUser(Long userId, String roleName) {

    }
}
