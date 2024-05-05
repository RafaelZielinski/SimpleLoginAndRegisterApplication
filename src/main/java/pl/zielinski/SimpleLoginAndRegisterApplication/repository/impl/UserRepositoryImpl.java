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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.zielinski.SimpleLoginAndRegisterApplication.domain.Role;
import pl.zielinski.SimpleLoginAndRegisterApplication.domain.User;
import pl.zielinski.SimpleLoginAndRegisterApplication.domain.UserPrincipal;
import pl.zielinski.SimpleLoginAndRegisterApplication.dto.UserDTO;
import pl.zielinski.SimpleLoginAndRegisterApplication.exception.ApiException;
import pl.zielinski.SimpleLoginAndRegisterApplication.repository.RoleRepository;
import pl.zielinski.SimpleLoginAndRegisterApplication.repository.UserRepository;
import pl.zielinski.SimpleLoginAndRegisterApplication.rowmapper.UserRowMapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import static java.util.Map.of;
import static java.util.Objects.requireNonNull;
import static org.apache.commons.lang3.time.DateFormatUtils.format;
import static org.apache.commons.lang3.time.DateUtils.addDays;
import static pl.zielinski.SimpleLoginAndRegisterApplication.enumeration.RoleType.ROLE_USER;
import static pl.zielinski.SimpleLoginAndRegisterApplication.enumeration.VerificationType.ACCOUNT;
import static pl.zielinski.SimpleLoginAndRegisterApplication.query.RoleQuery.UPDATE_USER_ENABLED_QUERY;
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
    public User create(User user, String verificationUrl) {
        if (getEmailCount(user.getEmail()) > 0) {
            throw new ApiException("There is already taken that email");
        }

        KeyHolder key = new GeneratedKeyHolder();
        SqlParameterSource parameters = getSqlParametersInsertUserSource(user);
        jdbc.update(INSERT_USER_QUERY, parameters, key, new String[]{"id"});
        user.setId(requireNonNull(key.getKey()).longValue());
        log.info("Adding user {} ", user);
        roleRepository.addRoleToUser(user.getId(), ROLE_USER.name());
        jdbc.update(INSERT_ACCOUNT_VERIFICATION_URL_QUERY, Map.of("userId", user.getId(), "url", verificationUrl));
        user.setEnabled(false);
        user.setNotLocked(true);
        return user;
    }

    private String getVerificationUrl(String key, String type) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path("/users/verify/" + type + "/" + key).toUriString();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = getUserByEmail(email);
        if (user == null) {
            log.error("User not found in the database");
            throw new UsernameNotFoundException("User not found exception");
        } else {
            log.info("User found in database : {} ", email);
            return new UserPrincipal(user, roleRepository.getRoleByUserId(user.getId()));
        }
    }

    private Integer getEmailCount(String email) {
        return jdbc.queryForObject(COUNT_USER_EMAIL_QUERY, Map.of("email", email), Integer.class);
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
        } catch (Exception exception) {
            log.error("There is problem with deriving users from database");
            throw new ApiException("There is problem with list of users from database");
        }
    }

    @Override
    public User get(Long id) {
        try {
            return jdbc.queryForObject(SELECT_USER_BY_ID_QUERY, Map.of("id", id), new UserRowMapper());
        } catch (EmptyResultDataAccessException exception) {
            log.error("There is no such user with id {}", id);
            throw new ApiException("There is no such an user at database exists");
        } catch (Exception exception) {
            log.error("An a problem occurred");
            throw new ApiException("An error occurred");
        }
    }

    @Override
    public User update(User data) {
        try {
            jdbc.update(UPDATE_USER_DATA_QUERY, getSqlParametersUpdateUserSource(data));
            return get(data.getId());
        } catch (EmptyResultDataAccessException exception) {
            log.error("No user found by id" + data.getId());
            throw new ApiException("No user found by id: " + data.getId());
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("An error occurred. Please try again.");
        }

    }

    private SqlParameterSource getSqlParametersUpdateUserSource(User data) {
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
        log.info(email);
        try {
            return jdbc.queryForObject(SELECT_USER_BY_EMAIL_QUERY, Map.of("email", email), new UserRowMapper());
        } catch (EmptyResultDataAccessException exception) {
            log.error("There is no such user with email {}", email);
            throw new ApiException("There is no such an user at database exists");
        } catch (Exception exception) {
            log.error("An a problem occurred");
            throw new ApiException("An error occurred");
        }
    }

    @Override
    public User verifyAccountKey(String key) {
        try {
            User user = jdbc.queryForObject(SELECT_USER_BY_ACCOUNT_URL_QUERY, of("url", getVerificationUrl(key, ACCOUNT.getType())), new UserRowMapper());
            jdbc.update(UPDATE_USER_ENABLED_QUERY, of("enabled", true, "id", user.getId()));
            jdbc.update(DELETE_USER_IN_ACCOUNT_VERIFICATIONS_BY_KEY_QUERY, Map.of("key", key));
            return user;
        } catch (EmptyResultDataAccessException exception) {
            throw new ApiException("This link is not valid.");
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("An error occurred. Please try again.");
        }
    }

    //MFA via login
    @Override
    public void sendVerificationCode(UserDTO user, String verificationCode) {
        String expirationDate = format(addDays(new Date(), 1), "yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.parse(expirationDate, formatter);


        log.info("\n expiration Date {} \n verification Code {} \n id {}", localDateTime, verificationCode, user.getId());

        try {
            jdbc.update(DELETE_VERIFICATION_CODE_BY_USER_ID, of("id", user.getId()));
            jdbc.update(INSERT_TWO_FACTOR_VERIFICATIONS_CODE_QUERY, getSqlParameterSourceForCreatingVerifyMFA(user.getId(), verificationCode, localDateTime));

        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("An error occurred. Please try again.");
        }
    }

    @Override
    public User verifyMfaCode(String email, String code) {
        if (isVerificationMfaCodeExpired(code))
            throw new ApiException("This code has been expired. Please try again later");
        try {
            User userByCode = jdbc.queryForObject(SELECT_USER_BY_USER_MFA_CODE_QUERY, of("code", code), new UserRowMapper());
            User userByEmail = jdbc.queryForObject(SELECT_USER_BY_EMAIL_QUERY, of("email", email), new UserRowMapper());
            if (userByEmail.getEmail().equals(userByCode.getEmail())) {
                jdbc.update(DELETE_VERIFICATION_CODE_BY_USER_ID, of("id", userByCode.getId()));
                return userByCode;
            } else {
                throw new ApiException("Unable to find a record");
            }
        } catch (EmptyResultDataAccessException exception) {
            throw new ApiException("Could not find a record");
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("An error occurred. Please try again");
        }

    }

    private Boolean isVerificationMfaCodeExpired(String code) {
        try {
            return jdbc.queryForObject(SELECT_CODE_MFA_EXPIRATION_DATE, of("code", code), Boolean.class);
        } catch (EmptyResultDataAccessException exception) {
            throw new ApiException("This code is not valid. Please login again.");
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("An error occurred. Please try again");
        }
    }

    private SqlParameterSource getSqlParameterSourceForCreatingVerifyMFA(Long id, String code, LocalDateTime dateTime) {
        return new MapSqlParameterSource()
                .addValue("id", id)
                .addValue("url", code)
                .addValue("expiration", dateTime);
    }
}
