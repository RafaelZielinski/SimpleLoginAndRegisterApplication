package pl.zielinski.SimpleLoginAndRegisterApplication.repository.impl.unitTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import pl.zielinski.SimpleLoginAndRegisterApplication.domain.User;
import pl.zielinski.SimpleLoginAndRegisterApplication.dto.UserDTO;
import pl.zielinski.SimpleLoginAndRegisterApplication.exception.ApiException;
import pl.zielinski.SimpleLoginAndRegisterApplication.repository.impl.RoleRepositoryImpl;
import pl.zielinski.SimpleLoginAndRegisterApplication.repository.impl.UserRepositoryImpl;
import pl.zielinski.SimpleLoginAndRegisterApplication.repository.impl.provider.RoleProvider;
import pl.zielinski.SimpleLoginAndRegisterApplication.repository.impl.provider.UserProvider;
import pl.zielinski.SimpleLoginAndRegisterApplication.rowmapper.UserRowMapper;

import java.util.*;

import static java.util.Map.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static pl.zielinski.SimpleLoginAndRegisterApplication.query.RoleQuery.UDPATE_USER_ENABLED_QUERY;

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
    UserRepositoryImpl cut;


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
        Collection<User> actual = cut.list();
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
        Collection<User> actual = cut.list();
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
        Collection<User> actual = cut.list();
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
        ApiException actual = assertThrows(ApiException.class, () -> cut.list());
        //then
        assertEquals("There is problem with list of users from database", actual.getMessage());
    }


    //   testing loadUserByUsername(String email)
    @Test
    void it_should_return_user_principal() {
        //given
        String email = "rafekzielinski@wp.pl";
        User user = firstUser();
        when(jdbc.queryForObject(Mockito.anyString(), Mockito.anyMap(), any(UserRowMapper.class)))
                .thenReturn(user);
        when(roleRepository.get(1L))
                .thenReturn(firstRole());
        //when
        UserDetails actual = cut.loadUserByUsername(email);
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
        Exception actual = assertThrows(UsernameNotFoundException.class, () -> cut.loadUserByUsername(email));
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
        User actual = cut.get(id);
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
        ApiException actual = assertThrows(ApiException.class, () -> cut.get(id));
        //then
        assertEquals("There is no such an user at database exists", actual.getMessage());
    }

    // testing User get(Long id)
    @Test
    void it_should_throw_exception_other_by_get_method() {
        //given
        long id = 1L;
        when(jdbc.queryForObject(anyString(), anyMap(), any(UserRowMapper.class)))
                .thenThrow(ApiException.class);
        //when
        ApiException actual = assertThrows(ApiException.class, () -> cut.get(id));
        //then
        assertEquals("An error occurred", actual.getMessage());
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
        User actual = cut.update(asArgument);
        //then
        verify(jdbc, times(1)).update(anyString(), any(SqlParameterSource.class));
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
        ApiException actual = assertThrows(ApiException.class, () -> cut.update(asArgument));
        //then
        assertEquals("No user found by id: " + asArgument.getId(), actual.getMessage());
    }

    //testing User update(User user)
    @Test
    void it_should_throw_exception_like_null_pointer_exception() {
        //given
        User asArgument = firstUser();
        when(encoder.encode(anyString()))
                .thenReturn(null);
        when(jdbc.update(anyString(), any(SqlParameterSource.class)))
                .thenThrow(EmptyResultDataAccessException.class);
        //when
        ApiException actual = assertThrows(ApiException.class, () -> cut.update(asArgument));
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
        User actual = cut.getUserByEmail(email);
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
        ApiException actual = assertThrows(ApiException.class, () -> cut.getUserByEmail(email));
        //then
        assertEquals("There is no such an user at database exists", actual.getMessage());
    }

    //testing User getUserByEmail(String email)
    @Test
    void it_should_throw_like_nullpointer_exception() {
        //given
        String email = "kamilzielinski@wp.pl";

        //when
        when(jdbc.queryForObject(anyString(), anyMap(), any(UserRowMapper.class)))
                .thenThrow(NullPointerException.class);
        ApiException actual = assertThrows(ApiException.class, () -> cut.getUserByEmail(email));
        //then
        assertEquals("An error occured", actual.getMessage());
    }

    // testing User create(User user)
    @Test
    void it_should_create_user() {
        //given
        MockHttpServletRequest request = getMockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        User expected = firstUser();
        when(jdbc.queryForObject(anyString(), anyMap(), eq(Integer.class)))
                .thenReturn(0);
        when(encoder.encode(any())).thenReturn("password");
        when(jdbc.update(anyString(), any(SqlParameterSource.class), any(GeneratedKeyHolder.class), any(String[].class)))
                .thenAnswer(invocation -> {
                    GeneratedKeyHolder keyHolder = invocation.getArgument(2);
                    keyHolder.getKeyList().add(new HashMap<>(Map.of("id", 1)));
                    return 1;
                });
        doNothing().when(roleRepository).addRoleToUser(anyLong(), anyString());
        //when
        User actual = cut.create(expected);
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
        ApiException actual = assertThrows(ApiException.class, () -> cut.create(user));
        //then
        assertEquals("There is already taken that email", actual.getMessage());
    }

    @Test
    void it_should_verify_account_with_success() {
        //given
        MockHttpServletRequest request = getMockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        User expected = thirdUser();
        String expectedUrl = "http://example.com/users/verify/type/key";
        //when
        when(jdbc.queryForObject(any(String.class), any(Map.class), any(RowMapper.class))).thenReturn(expected);
        User actual = cut.verifyAccountKey("key");
        //then
        assertEquals(expected.isEnabled(), actual.isEnabled());
        verify(jdbc).update(eq(UDPATE_USER_ENABLED_QUERY), eq(of("enabled", true, "id", 3L)));
    }

    private static MockHttpServletRequest getMockHttpServletRequest() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setScheme("http");
        request.setServerName("localhost");
        request.setServerPort(-1);
        request.setRequestURI("/example.com");
        return request;
    }

    @DisplayName("testing method sendingVerification(String code, UserDTO user")
    @Test
    void it_should_with_success_save_mfa_code_to_database() {
        //given
        UserDTO userDTO = firstUserDTO();
        when(jdbc.update(any(String.class), any(Map.class))).thenReturn(1);
        when(jdbc.update(any(String.class), any(SqlParameterSource.class))).thenReturn(1);
        //when
        cut.sendVerificationCode(userDTO, "code1");
        //then
        verify(jdbc, times(1)).update(anyString(), any(SqlParameterSource.class));
        verify(jdbc, times(1)).update(anyString(), any(Map.class));

    }

    @DisplayName("testing method sendingVerification(String code, UserDTO user")
    @Test
    void it_should_throw_error_there_is_something_wrong() {
        //given
        UserDTO userDTO = firstUserDTO();
        when(jdbc.update(any(String.class), any(Map.class))).thenThrow(new ApiException("There is an error"));
        when(jdbc.update(any(String.class), any(SqlParameterSource.class))).thenReturn(1);
        //when
        ApiException actual = assertThrows(ApiException.class, () -> cut.sendVerificationCode(userDTO, "code1"));
        //then
        assertEquals("An error occurred. Please try again.", actual.getMessage());
    }

    @DisplayName("testing method verifyMfaCode(String email, String code)")
    @Test
    void it_should_throw_error_code_is_invalid() {
        //given
        String email = "rafekzielinski@wp.pl";
        String code = "SFDSAF";
        UserDTO userDTO = firstUserDTO();
        when(jdbc.queryForObject(anyString(), any(Map.class), eq(Boolean.class))).thenThrow(new EmptyResultDataAccessException("This code is not valid. Please login again.", 0));
        //when
        ApiException actual = assertThrows(ApiException.class, () -> cut.verifyMfaCode("rafekzielinski@wp.pl", "DSFDSAF"));
        //then
        assertEquals("This code is not valid. Please login again.", actual.getMessage());
    }

    @DisplayName("testing method verifyMfaCode(String email, String code)")
    @Test
    void it_should_verify_code_with_success() {
        //given
        User expected = firstUser();
        String email = "rafekzielinski@wp.pl";
        String code = "SFDSAF";
        UserDTO userDTO = firstUserDTO();
        when(jdbc.queryForObject(anyString(), any(Map.class), eq(Boolean.class))).thenReturn(Boolean.FALSE);
        when(jdbc.queryForObject(anyString(), any(Map.class), any(RowMapper.class))).thenReturn(firstUser());
        //when
        User actual=  cut.verifyMfaCode("rafekzielinski@wp.pl", "DSFDSAF");
        //then
        assertEquals(expected.getId(), expected.getId());
        assertEquals(expected.getFirstName(), expected.getFirstName());
        assertEquals(expected.getLastName(), expected.getLastName());
    }


}