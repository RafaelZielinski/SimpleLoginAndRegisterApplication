package pl.zielinski.SimpleLoginAndRegisterApplication.repository.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import pl.zielinski.SimpleLoginAndRegisterApplication.domain.Role;
import pl.zielinski.SimpleLoginAndRegisterApplication.domain.User;
import pl.zielinski.SimpleLoginAndRegisterApplication.repository.impl.provider.RoleProvider;
import pl.zielinski.SimpleLoginAndRegisterApplication.repository.impl.provider.UserProvider;
import pl.zielinski.SimpleLoginAndRegisterApplication.rowmapper.UserRowMapper;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

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

//    loadUserByUsername(String email)
    @Test
    void it_should_return_user_principal() {
        //given
        String email = "rafekzielinski@wp.pl";
        User user = firstUser();
        Role role = firstRole();
        when(jdbc.queryForObject(Mockito.anyString(), Mockito.anyMap(), Mockito.any(UserRowMapper.class)))
                .thenReturn(user);
        when(roleRepository.get(1L))
                .thenReturn(firstRole());
        //when
        UserDetails actual = userRepository.loadUserByUsername(email);
        //then
        assertEquals(actual.getUsername(), email);
    }

    //    loadUserByUsername(String email)
    @Test
    void it_should_throw_user_not_found_exception() {
        //given
        String email = "rafekzielinski@wp.pl";
        when(jdbc.queryForObject(Mockito.anyString(), Mockito.anyMap(), Mockito.any(UserRowMapper.class)))
                .thenReturn(null);
        //when
        Exception actual = assertThrows(UsernameNotFoundException.class, () -> userRepository.loadUserByUsername(email));
        //then
        assertEquals(actual.getMessage(), "User not found exception");
    }

}