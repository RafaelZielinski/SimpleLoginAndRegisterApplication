package pl.zielinski.SimpleLoginAndRegisterApplication.repository.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import pl.zielinski.SimpleLoginAndRegisterApplication.domain.Role;
import pl.zielinski.SimpleLoginAndRegisterApplication.exception.ApiException;
import pl.zielinski.SimpleLoginAndRegisterApplication.repository.RoleRepository;
import pl.zielinski.SimpleLoginAndRegisterApplication.rowmapper.RoleRowMapper;

import java.util.Collection;
import java.util.Map;

import static java.util.Objects.requireNonNull;
import static pl.zielinski.SimpleLoginAndRegisterApplication.enumeration.RoleType.ROLE_USER;
import static pl.zielinski.SimpleLoginAndRegisterApplication.query.RoleQuery.*;

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
        try {
            return jdbc.queryForObject(SELECT_ROLE_BY_ID, Map.of("id", id), new RoleRowMapper());
        } catch (EmptyResultDataAccessException exception) {
            throw new ApiException("There is no such a role");
        } catch (Exception exception) {
            throw new ApiException("An error occurred. Please try again");
        }
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
        log.info("Adding role {} to user id: {} ", roleName, userId);
        try {
            Role role = jdbc.queryForObject(SELECT_ROLE_BY_NAME_QUERY, Map.of("roleName", roleName), new RoleRowMapper());
            jdbc.update(INSERT_ROLE_TO_USER_QUERY, Map.of("userId", userId, "roleId", requireNonNull(role).getId()));
        } catch (EmptyResultDataAccessException exception) {
            log.error("No role found by name: {} ", roleName);
            throw new ApiException("No role found by name: " + roleName);
        } catch (
                Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("An error occured. Please try again");
        }
    }

    @Override
    public Role getRoleByUserId(Long userId) {
        log.info("Fetching role for user id: {} ", userId);
        try {
            return jdbc.queryForObject(SELECT_ROLE_BY_USER_ID, Map.of("id", userId), new RoleRowMapper());
        } catch (EmptyResultDataAccessException exception) {
            log.error(exception.getMessage());
            //repair that
            throw new ApiException("No role found by name: " + ROLE_USER.name());
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("An error occurred. Please try again");
        }
    }
}

