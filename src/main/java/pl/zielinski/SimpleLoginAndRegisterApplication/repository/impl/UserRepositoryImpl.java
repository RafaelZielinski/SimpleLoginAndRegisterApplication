package pl.zielinski.SimpleLoginAndRegisterApplication.repository.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import pl.zielinski.SimpleLoginAndRegisterApplication.domain.Role;
import pl.zielinski.SimpleLoginAndRegisterApplication.domain.User;
import pl.zielinski.SimpleLoginAndRegisterApplication.domain.UserPrincipal;
import pl.zielinski.SimpleLoginAndRegisterApplication.exception.ApiException;
import pl.zielinski.SimpleLoginAndRegisterApplication.repository.RoleRepository;
import pl.zielinski.SimpleLoginAndRegisterApplication.repository.UserRepository;
import pl.zielinski.SimpleLoginAndRegisterApplication.rowmapper.UserRowMapper;

import java.util.Collection;
import java.util.Map;

import static java.util.Objects.requireNonNull;
import static pl.zielinski.SimpleLoginAndRegisterApplication.query.UserQuery.*;

/**
 * @author rafek
 * @version 1.0
 * @licence free
 * @since 2023}-12-22
 */

@Repository
@RequiredArgsConstructor
@Slf4j
public class UserRepositoryImpl implements UserRepository<User>, UserDetailsService {
    private final NamedParameterJdbcTemplate jdbc;
    private final BCryptPasswordEncoder encoder;
    private final RoleRepository<Role> roleRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = getUserByEmail(email);
        if(user == null) {
            log.error("User not found in the database");
            throw new UsernameNotFoundException("User not found exception");
        } else {
            log.info("User found in database : {} ", email);
            return new UserPrincipal(user, roleRepository.get(user.getId()));
        }
    }

    @Override
    public User create(User user) {
        //first check if address email exists

        KeyHolder key = new GeneratedKeyHolder();
        SqlParameterSource parameters = getSqlParametersInsertUserSource(user);
        jdbc.update(INSERT_USER_QUERY, parameters, key, new String[]{"id"});
        log.info("Adding user {} ", user);
        user.setId(requireNonNull(key.getKey()).longValue());
        return user;
    }

    private SqlParameterSource getSqlParametersInsertUserSource(User data) {
        return new MapSqlParameterSource()
                .addValues(Map.of(
                        "firstName", data.getFirstName(),
                        "lastName", data.getLastName(),
                        "email", data.getEmail(),
                        "password", encoder.encode(data.getPassword()),
                        "age", data.getAge()));
    }

    @Override
    public Collection<User> list() {
        try {
            return jdbc.query(SELECT_USERS_QUERY, new UserRowMapper());
        }catch (Exception exception) {
            log.error("There is problem with derriving users from database");
            throw new ApiException("There is problem with list of users from database");
        }
    }

    @Override
    public User get(Long id) {
        try {
            return jdbc.queryForObject(SELECT_USER_BY_ID_QUERY, Map.of("id", id), new UserRowMapper());
        }catch (EmptyResultDataAccessException exception) {
            log.error("There is no such user with id {}", id);
            throw new ApiException("There is no such an user at database exists");
        }catch (Exception exception) {
            log.error("An a problem occured");
            throw new ApiException("An error occured");
        }
    }

    @Override
    public User update(User data) {
        try {
           jdbc.update(UPDATE_USER_DATA_QUERY, getSqlParametersUpdateUserSource(data));
           return get(data.getId());
        }
        catch (EmptyResultDataAccessException exception) {
            throw new ApiException("No user found by id: " + data.getId());
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("An error occurred. Please try again.");
        }

    }

    private  SqlParameterSource getSqlParametersUpdateUserSource(User data) {
            return new MapSqlParameterSource()
                    .addValues(Map.of(
                            "id", data.getId(),
                            "firstName", data.getFirstName(),
                            "lastName", data.getLastName(),
                            "email", data.getEmail(),
                            "password", encoder.encode(data.getPassword()),
                            "age", data.getAge()));
        }

    @Override
    public User getUserByEmail(String email) {
        try {
            return jdbc.queryForObject(SELECT_USER_BY_EMAIL_QUERY, Map.of("email", email), new UserRowMapper());
        }catch (EmptyResultDataAccessException exception) {
            log.error("There is no such user with email {}", email);
            throw new ApiException("There is no such an user at Database exists");
        }catch (Exception exception) {
            log.error("An a problem occured");
            throw new ApiException("An error occured");
        }
    }
}
