package pl.zielinski.SimpleLoginAndRegisterApplication.repository.impl.unitTests;

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
import pl.zielinski.SimpleLoginAndRegisterApplication.repository.impl.RoleRepositoryImpl;
import pl.zielinski.SimpleLoginAndRegisterApplication.repository.impl.provider.RoleProvider;
import pl.zielinski.SimpleLoginAndRegisterApplication.rowmapper.RoleRowMapper;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static pl.zielinski.SimpleLoginAndRegisterApplication.enumeration.RoleType.ROLE_USER;

/**
 * @author rafek
 * @version 1.0
 * @licence ask rafekzielinski@wp.pl
 * @since 27/01/2024
 */
class RoleRepositoryImplTest implements RoleProvider{

    @Mock
    NamedParameterJdbcTemplate jdbc;

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
        Role expected = firstRole();
        when(jdbc.queryForObject(anyString(), anyMap(), any(RoleRowMapper.class)))
                .thenReturn(expected);

        //when
        Role actual = roleRepository.get(roleId);
        //then
        assertEquals(actual.getId(), expected.getId());
        assertEquals(actual.getName(), expected.getName());
        assertEquals(actual.getPermission(), expected.getPermission());
    }

    //testing method get(Long id)
    @Test
    void it_should_throw_main_exception() {
        //given
        long roleId = 1L;
        when(jdbc.queryForObject(anyString(), anyMap(), any(RoleRowMapper.class)))
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
        when(jdbc.queryForObject(anyString(), anyMap(), any(RoleRowMapper.class)))
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
        Role role1 = firstRole();
        List<Role> expected = List.of(role1);
        when(jdbc.query(anyString(), any(RoleRowMapper.class)))
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
        Role demand1 = firstRole();
        Role demand2 = secondRole();
        Role demand3 = thirdRole();
        List<Role> expected = List.of(demand1, demand2, demand3);
        when(jdbc.query(anyString(), any(RoleRowMapper.class)))
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
        when(jdbc.query(anyString(), any(RoleRowMapper.class)))
                .thenThrow(ApiException.class);

        //when
        Exception actual = assertThrows(ApiException.class, () -> roleRepository.list());

        //then
        assertEquals("An error occurred. Please try again", actual.getMessage());
    }

    //testing method getRoleByUserId(Long id)
    @Test
    void it_should_return_role() {
        //given
        long id = 1L;
        Role expected = firstRole();
        when(jdbc.queryForObject(anyString(), anyMap(), any(RoleRowMapper.class)))
                .thenReturn(expected);
        //when
        Role actual = roleRepository.getRoleByUserId(id);
        //then
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getPermission(), actual.getPermission());
    }

    //testing method getRoleByUserId(Long id)
    @Test
    void it_should_throw_empty_result_data_exception() {
        //given
        when(jdbc.queryForObject(anyString(), anyMap(), any(RoleRowMapper.class)))
                .thenThrow(EmptyResultDataAccessException.class);
        //when
        Exception actual = assertThrows(ApiException.class, () -> roleRepository.getRoleByUserId(1L));
        //then
        assertEquals("No role found by name: " + ROLE_USER.name(), actual.getMessage());

    }

    //testing method getRoleByUserId(Long id)
    @Test
    void it_should_throw_main_exception_in_getRoleByUserId() {
        //given
        when(jdbc.queryForObject(anyString(), anyMap(), any(RoleRowMapper.class)))
                .thenThrow(ApiException.class);
        //when
        Exception actual = assertThrows(ApiException.class, () -> roleRepository.getRoleByUserId(1L));
        //then
        assertEquals("An error occurred. Please try again", actual.getMessage());

    }

    //testing method  addRoleToUser(Long userId, String roleName)
    @Test
    void it_should_update_one_user_and_assign_one_role() {
        //given
        Role expected = firstRole();
        when(jdbc.queryForObject(anyString(), anyMap(), any(RoleRowMapper.class)))
                .thenReturn(expected);
        when(jdbc.update(anyString(), anyMap()))
                .thenReturn(1);
        //when
        roleRepository.addRoleToUser(1L, ROLE_USER.name());
        //then
        Mockito.verify(jdbc).queryForObject(anyString(), anyMap(), any(RoleRowMapper.class));
        Mockito.verify(jdbc).update(anyString(), anyMap());
    }

    //testing method  addRoleToUser(Long userId, String roleName)
    @Test
    void it_should_not_update_one_user_and_assign_one_role_and_throw_empty_result_exception() {
        //given
        when(jdbc.queryForObject(anyString(), anyMap(), any(RoleRowMapper.class)))
                .thenThrow(EmptyResultDataAccessException.class);
        //when
        ApiException actual = assertThrows(ApiException.class, () -> roleRepository.addRoleToUser(1L, ROLE_USER.name()));
        //then
        assertEquals("No Role found by name: " + ROLE_USER.name(), actual.getMessage());
        Mockito.verify(jdbc, times(1)).queryForObject(anyString(), anyMap(), any(RoleRowMapper.class));
        Mockito.verify(jdbc, never())
                .update(anyString(), anyMap());
    }

    //testing method  addRoleToUser(Long userId, String roleName)
    @Test
    void it_should_not_update_one_user_and_assign_one_role_and_throw_exception() {
        //given
        when(jdbc.queryForObject(anyString(), anyMap(), any(RoleRowMapper.class)))
                .thenThrow(ApiException.class);
        //when
        ApiException actual = assertThrows(ApiException.class, () -> roleRepository.addRoleToUser(1L, ROLE_USER.name()));
        //then
        assertEquals("An error occured. Please try again", actual.getMessage());
        Mockito.verify(jdbc, times(1)).queryForObject(anyString(), anyMap(), any(RoleRowMapper.class));
        Mockito.verify(jdbc, never())
                .update(anyString(), anyMap());
    }
}