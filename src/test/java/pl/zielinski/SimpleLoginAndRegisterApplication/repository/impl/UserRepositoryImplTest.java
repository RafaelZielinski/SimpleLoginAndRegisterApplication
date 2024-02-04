package pl.zielinski.SimpleLoginAndRegisterApplication.repository.impl;

import org.assertj.core.api.IntegerAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import pl.zielinski.SimpleLoginAndRegisterApplication.domain.Role;
import pl.zielinski.SimpleLoginAndRegisterApplication.domain.User;
import pl.zielinski.SimpleLoginAndRegisterApplication.exception.ApiException;
import pl.zielinski.SimpleLoginAndRegisterApplication.repository.impl.provider.RoleProvider;
import pl.zielinski.SimpleLoginAndRegisterApplication.repository.impl.provider.UserProvider;
import pl.zielinski.SimpleLoginAndRegisterApplication.rowmapper.UserRowMapper;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * @author rafek
 * @version 1.0
 * @licence ask rafekzielinski@wp.pl
 * @since 30/01/2024
 */
class UserRepositoryImplTest implements UserProvider, RoleProvider {

    @Mock
    NamedParameterJdbcTemplate jdbc;

    @Mock
    RoleRepositoryImpl roleRepository;

    @Mock
    BCryptPasswordEncoder encoder;

    @InjectMocks
    UserRepositoryImpl userRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    //testing Collection<User> list()
    @Test
    void it_should_return_empty_list() {
        //given
        when(jdbc.query(anyString(), any(UserRowMapper.class)))
                .thenReturn(Collections.emptyList());
        //when
        Collection<User> actual = userRepository.list();
        //then
        assertEquals(actual.size(), 0);
    }

    //testing Collection<User> list()
    @Test
    void it_should_return_one_size_list() {
        //given
        User expected = firstUser();
        when(jdbc.query(anyString(), any(UserRowMapper.class)))
                .thenReturn(List.of(expected));
        //when
        Collection<User> actual = userRepository.list();
        //then
        assertEquals(actual.size(), 1);
        assertEquals(actual.stream().findFirst().get().getFirstName(), expected.getFirstName());
        assertThat(actual).containsExactlyInAnyOrderElementsOf(List.of(expected));
    }

    //testing Collection<User> list()
    @Test
    void it_should_return_two_size_list() {
        //given
        User expected1 = firstUser();
        User expected2 = secondUser();

        when(jdbc.query(anyString(), any(UserRowMapper.class)))
                .thenReturn(List.of(expected1, expected2));
        //when
        Collection<User> actual = userRepository.list();
        //then
        assertEquals(actual.size(), 2);
        assertThat(actual).containsExactlyInAnyOrderElementsOf(List.of(expected1, expected2));
    }

    //testing Collection<User> list()
    @Test
    void it_should_throw_exception() {
        //given
        when(jdbc.query(anyString(), any(UserRowMapper.class)))
                .thenThrow(NullPointerException.class);
        //when
        ApiException actual = assertThrows(ApiException.class, () -> userRepository.list());
        //then
        assertEquals("There is problem with list of users from database", actual.getMessage());
    }


    //   testing loadUserByUsername(String email)
    @Test
    void it_should_return_user_principal() {
        //given
        String email = "rafekzielinski@wp.pl";
        User user = firstUser();
        Role role = firstRole();
        when(jdbc.queryForObject(Mockito.anyString(), Mockito.anyMap(), any(UserRowMapper.class)))
                .thenReturn(user);
        when(roleRepository.get(1L))
                .thenReturn(firstRole());
        //when
        UserDetails actual = userRepository.loadUserByUsername(email);
        //then
        assertEquals(actual.getUsername(), email);
    }

    //   testing loadUserByUsername(String email)
    @Test
    void it_should_throw_user_not_found_exception() {
        //given
        String email = "rafekzielinski@wp.pl";
        when(jdbc.queryForObject(Mockito.anyString(), Mockito.anyMap(), any(UserRowMapper.class)))
                .thenReturn(null);
        //when
        Exception actual = assertThrows(UsernameNotFoundException.class, () -> userRepository.loadUserByUsername(email));
        //then
        assertEquals(actual.getMessage(), "User not found exception");
    }


    // testing User get(Long id)
    @Test
    void it_should_get_user_by_get_method() {
        //given
        long id = 1L;
        User expected = firstUser();
        when(jdbc.queryForObject(anyString(), anyMap(), any(UserRowMapper.class)))
                .thenReturn(expected);
        //when
        User actual = userRepository.get(id);
        //then
        assertEquals(expected.toString(), actual.toString());
    }

    // testing User get(Long id)
    @Test
    void it_should_throw_exception_not_found_user_by_get_method() {
        //given
        long id = 1L;
        when(jdbc.queryForObject(anyString(), anyMap(), any(UserRowMapper.class)))
                .thenThrow(EmptyResultDataAccessException.class);
        //when
        ApiException actual = assertThrows(ApiException.class, () -> userRepository.get(id));
        //then
        assertEquals("There is no such an user at database exists", actual.getMessage() );
    }

    // testing User get(Long id)
    @Test
    void it_should_throw_exception_other_by_get_method() {
        //given
        long id = 1L;
        when(jdbc.queryForObject(anyString(), anyMap(), any(UserRowMapper.class)))
                .thenThrow(ApiException.class);
        //when
        ApiException actual = assertThrows(ApiException.class, () -> userRepository.get(id));
        //then
        assertEquals( "An error occurred", actual.getMessage() );
    }

    //testing User update(User user)
    @Test
    void it_should_update_user_by_update_method() {
        //given
        User asArgument = firstUser();
        User expected = secondUser();
        when(jdbc.update(anyString(), any(SqlParameterSource.class)))
                .thenReturn(1);
        when(jdbc.queryForObject(anyString(), anyMap(), any(UserRowMapper.class)))
                .thenReturn(expected);
        when(encoder.encode(anyString()))
                .thenReturn("password");
        //when
        User actual = userRepository.update(asArgument);
        //then
        Mockito.verify(jdbc, times(1)).update(anyString(), any(SqlParameterSource.class));
        assertEquals(expected.toString(), actual.toString());
    }

    //testing User update(User user)
    @Test
    void it_should_not_found_user_by_update_method_and_throw_exception() {
        //given
        User asArgument = firstUser();
        User expected = secondUser();
        when(encoder.encode(anyString()))
                .thenReturn("password");
        when(jdbc.update(anyString(), any(SqlParameterSource.class)))
                .thenThrow(EmptyResultDataAccessException.class);
        //when
        ApiException actual = assertThrows(ApiException.class, () -> userRepository.update(asArgument));
        //then
        assertEquals("No user found by id: " + asArgument.getId(), actual.getMessage());
    }
    //testing User update(User user)
    @Test
    void it_should_throw_exception_like_null_pointer_exception() {
        //given
        User asArgument = firstUser();
        User expected = secondUser();
        when(encoder.encode(anyString()))
                .thenReturn(null);
        when(jdbc.update(anyString(), any(SqlParameterSource.class)))
                .thenThrow(EmptyResultDataAccessException.class);
        //when
        ApiException actual = assertThrows(ApiException.class, () -> userRepository.update(asArgument));
        //then
        assertEquals("An error occurred. Please try again.", actual.getMessage());
    }

    //testing User getUserByEmail(String email)
    @Test
    void it_should_get_user_by_email() {
        //given
        String email = "kamilzielinski@wp.pl";
        User expected = secondUser();
        //when
        when(jdbc.queryForObject(anyString(), anyMap(), any(UserRowMapper.class)))
                .thenReturn(expected);

        User actual = userRepository.getUserByEmail(email);
        //then
        assertEquals(expected, actual);
    }

    //testing User getUserByEmail(String email)
    @Test
    void it_should_not_found_user_by_email() {
        //given
        String email = "kamilzielinski@wp.pl";

        //when
        when(jdbc.queryForObject(anyString(), anyMap(), any(UserRowMapper.class)))
                .thenThrow(EmptyResultDataAccessException.class);

        ApiException actual = assertThrows(ApiException.class, () -> userRepository.getUserByEmail(email));
        //then
        assertEquals("There is no such an user at Database exists", actual.getMessage());
    }

    //testing User getUserByEmail(String email)
    @Test
    void it_should_throw_like_nullpointer_exception() {
        //given
        String email = "kamilzielinski@wp.pl";

        //when
        when(jdbc.queryForObject(anyString(), anyMap(), any(UserRowMapper.class)))
                .thenThrow(NullPointerException.class);

        ApiException actual = assertThrows(ApiException.class, () -> userRepository.getUserByEmail(email));
        //then
        assertEquals("An error occured", actual.getMessage());
    }

    // testing User create(User user)
    @Test
    void it_should_create_user() {
        //given
        User expected = firstUser();
        KeyHolder keyHolder = new GeneratedKeyHolder();
        when(jdbc.queryForObject(anyString(), anyMap(), eq(Integer.class)))
                .thenReturn(0);
        when(encoder.encode(any())).thenReturn("password");
        when(jdbc.update(anyString(), any(SqlParameterSource.class),eq(keyHolder) , any(String[].class)))
                .thenAnswer(invocation -> {
                    keyHolder.getKeyList().add(new HashMap<>(Map.of("id", 1)));
                    return 1;
                });
        doNothing().when(roleRepository).addRoleToUser(anyLong(), anyString());

        //when
        User actual = userRepository.create(expected);
        //then
        verify(jdbc, times(1)).update(anyString(), any(SqlParameterSource.class), any(KeyHolder.class), any(String[].class));
        verify(roleRepository, times(1)).addRoleToUser(eq(actual.getId()), eq("ROLE_USER"));
    }
    // testing User create(User user)
    @Test
    void it_should_throw_email_found_in_database() {
        //given
        User user = firstUser();
        when(jdbc.queryForObject(anyString(), anyMap(), eq(Integer.class)))
                .thenReturn(1);

        //when
        ApiException actual = assertThrows(ApiException.class, () -> userRepository.create(user));
        //then
        assertEquals("There is already taken that email", actual.getMessage());
    }



}