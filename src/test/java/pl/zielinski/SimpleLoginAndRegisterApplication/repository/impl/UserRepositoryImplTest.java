package pl.zielinski.SimpleLoginAndRegisterApplication.repository.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import pl.zielinski.SimpleLoginAndRegisterApplication.domain.User;
import pl.zielinski.SimpleLoginAndRegisterApplication.rowmapper.UserRowMapper;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * @author rafek
 * @version 1.0
 * @licence ask rafekzielinski@wp.pl
 * @since 30/01/2024
 */
class UserRepositoryImplTest {

    @Mock
    NamedParameterJdbcTemplate jdbc;

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
        User expected = new User(
                1L, "Rafał", "Zieliński", "rafekzielinski@wp.pl", 26L,
                "password", true, true, false,
                LocalDateTime.of(2024, 1, 30, 6, 30, 3, 170603900));
        System.out.println(LocalDateTime.now());
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
        User expected1 = new User(
                1L, "Rafał", "Zieliński", "rafekzielinski@wp.pl", 26L,
                "password", true, true, false,
                LocalDateTime.of(2024, 1, 30, 6, 30, 3, 170603900));
        User expected2 = new User(
                1L, "Kamil", "Zieliński", "kamilzielinski@wp.pl", 19L,
                "password", true, true, false,
                LocalDateTime.of(2024, 1, 29, 6, 30, 3, 173333900));

        when(jdbc.query(anyString(), any(UserRowMapper.class)))
                .thenReturn(List.of(expected1, expected2));
        //when
        Collection<User> actual = userRepository.list();
        //then
        assertEquals(actual.size(), 2);
        assertThat(actual).containsExactlyInAnyOrderElementsOf(List.of(expected1, expected2));

    }

}