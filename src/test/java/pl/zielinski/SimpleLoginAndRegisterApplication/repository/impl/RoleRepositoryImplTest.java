package pl.zielinski.SimpleLoginAndRegisterApplication.repository.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import pl.zielinski.SimpleLoginAndRegisterApplication.domain.Role;
import pl.zielinski.SimpleLoginAndRegisterApplication.exception.ApiException;
import pl.zielinski.SimpleLoginAndRegisterApplication.rowmapper.RoleRowMapper;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

/**
 * @author rafek
 * @version 1.0
 * @licence ask rafekzielinski@wp.pl
 * @since 27/01/2024
 */
class RoleRepositoryImplTest {

    @Mock
    NamedParameterJdbcTemplate jdbcTemplate;

    @InjectMocks
    RoleRepositoryImpl roleRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    //testing method get(Long id)
    @Test
    void it_should_return_role_with_success() {
        //given
        long roleId = 1L;
        Role demand = new Role(1L, "ROLE_USER", "READ:USER,READ:CUSTOMER");
        when(jdbcTemplate.queryForObject(Mockito.anyString(), Mockito.anyMap(), Mockito.any(RoleRowMapper.class)))
                .thenReturn(demand);

        //when
        Role actual = roleRepository.get(roleId);
        //then
        assertEquals(actual.getId(), demand.getId());
        assertEquals(actual.getName(), demand.getName());
        assertEquals(actual.getPermission(), demand.getPermission());
    }

    //testing method get(Long id)
    @Test
    void it_should_throw_main_exception() {
        //given
        long roleId = 1L;
        when(jdbcTemplate.queryForObject(Mockito.anyString(), Mockito.anyMap(), Mockito.any(RoleRowMapper.class)))
                .thenThrow(ApiException.class);

        //when
        Exception actual = assertThrows(ApiException.class, () -> roleRepository.get(roleId));
        //then
        assertEquals("An error occurred. Please try again", actual.getMessage());

    }

    //testing method get(Long id)
    @Test
    void it_should_throw_empty_result_data_access_exception() {
        //given
        long roleId = 1L;
        when(jdbcTemplate.queryForObject(Mockito.anyString(), Mockito.anyMap(), Mockito.any(RoleRowMapper.class)))
                .thenThrow(EmptyResultDataAccessException.class);
        //when
        Exception actual = assertThrows(ApiException.class, () -> roleRepository.get(roleId));
        //then
        assertEquals("There is no such a role ", actual.getMessage());
    }

    //testing method list()
    @Test
    void it_should_return_one_role() {
        //given
        Role demand1 = new Role(1L, "ROLE_USER", "READ:USER,READ:CUSTOMER");
        List<Role> expected = List.of(demand1);
        when(jdbcTemplate.query(Mockito.anyString(), Mockito.any(RoleRowMapper.class)))
                .thenReturn(expected);

        //when
        Collection<Role> actual = roleRepository.list();

        //then
        assertThat(actual).containsAll(expected);
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);

    }

    //testing method list()
    @Test
    void it_should_return_three_roles() {
        //given
        Role demand1 = new Role(1L, "ROLE_USER", "READ:USER,READ:CUSTOMER");
        Role demand2 = new Role(2L, "ROLE_MANAGER", "READ:USER,READ:CUSTOMER,UPDATE:USER,UPDATE:CUSTOMER");
        Role demand3 = new Role(3L, "ROLE_ADMIN", "READ:USER,READ:CUSTOMER,CREATE:USER,CREATE:CUSTOMER,UPDATE:USER,UPDATE:CUSTOMER");
        List<Role> expected = List.of(demand1, demand2, demand3);
        when(jdbcTemplate.query(Mockito.anyString(), Mockito.any(RoleRowMapper.class)))
                .thenReturn(expected);

        //when
        Collection<Role> actual = roleRepository.list();

        //then
        assertThat(actual).containsAll(expected);
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }

    //testing method list()
    @Test
    void it_should_throw_exception() {
        //given
        when(jdbcTemplate.query(Mockito.anyString(), Mockito.any(RoleRowMapper.class)))
                .thenThrow(ApiException.class);

        //when
        Exception actual = assertThrows(ApiException.class, () -> roleRepository.list());

        //then
        assertEquals("An error occurred. Please try again", actual.getMessage());
    }
}