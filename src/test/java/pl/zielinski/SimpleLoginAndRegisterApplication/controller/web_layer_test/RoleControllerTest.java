package pl.zielinski.SimpleLoginAndRegisterApplication.controller.web_layer_test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.zielinski.SimpleLoginAndRegisterApplication.dto.RoleDTO;
import pl.zielinski.SimpleLoginAndRegisterApplication.exception.ApiException;
import pl.zielinski.SimpleLoginAndRegisterApplication.provider.TokenProvider;
import pl.zielinski.SimpleLoginAndRegisterApplication.controller.RoleController;
import pl.zielinski.SimpleLoginAndRegisterApplication.controller.web_layer_test.provider.RoleControllerProvider;
import pl.zielinski.SimpleLoginAndRegisterApplication.service.RoleService;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

/**
 * @author rafek
 * @version 1.0
 * @licence ask rafekzielinski@wp.pl
 * @since 05/02/2024
 */

@WebMvcTest(RoleController.class)
@AutoConfigureMockMvc()
@ActiveProfiles("test")
class RoleControllerTest implements RoleControllerProvider {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RoleService roleService;

    @MockBean
    private TokenProvider tokenProvider;

    //test for method getRoles()
    @Test
    void it_should_return_list_of_two_roles() throws Exception {
        //given
        when(roleService.getRoles()).thenReturn(List.of(firstRoleDTO(), secondRoleDTO()));
        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.get("/roles/list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.authentication(new UsernamePasswordAuthenticationToken(1L, null, List.of(new SimpleGrantedAuthority("USER"))))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("List of roles"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.roles").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.roles[0].name").value("ROLE_USER"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.roles[1].name").value("ROLE_MANAGER"));
    }

    //test for method getRoles()
    @Test
    void it_should_return_empty_list_of_roles() throws Exception {
        //given
        when(roleService.getRoles()).thenReturn(Collections.emptyList());
        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.get("/roles/list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.authentication(new UsernamePasswordAuthenticationToken(1L, null, List.of(new SimpleGrantedAuthority("USER"))))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("List of roles"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.roles.size()").value(0));
    }

    //test for method getRoles()
    @Test
    void it_should_throw_api_exception() throws Exception {
        //given
        given(roleService.getRoles()).willThrow(new ApiException("An error occurred. Please try again"));
        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.get("/roles/list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.authentication(new UsernamePasswordAuthenticationToken(1L, null, List.of(new SimpleGrantedAuthority("USER"))))))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.developerMessage").value("An error occurred. Please try again"));
    }

    //test for method getRoleById()
    @Test
    void it_should_return_correctly_role_by_id() throws Exception {
        //given
        when(roleService.getRoleById(any())).thenReturn(firstRoleDTO());
        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.get("/roles/role/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.authentication(new UsernamePasswordAuthenticationToken(1L, null, List.of(new SimpleGrantedAuthority("USER"))))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Role retrieved"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.role.name").value("ROLE_USER"));
        ;
    }

    //test for method getRoleById()
    @Test
    void it_should_throw_error_there_is_no_role_by_id() throws Exception {
        //given
        when(roleService.getRoleById(any())).thenThrow(new ApiException("There is no such a role "));
        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.get("/roles/role/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.authentication(new UsernamePasswordAuthenticationToken(1L, null, List.of(new SimpleGrantedAuthority("USER"))))))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.reason").value("There is no such a role "));
    }

    //test for method getRoleByUserId
    @Test
    void it_should_return_correctly_role_by_user_id() throws Exception {
        //given
        when(roleService.getRoleByUserId(any())).thenReturn(firstRoleDTO());
        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.get("/roles/roleByUserId/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.authentication(new UsernamePasswordAuthenticationToken(1L, null, List.of(new SimpleGrantedAuthority("USER"))))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Role by user id retrieved"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.role.name").value("ROLE_USER"));
    }

    //test for method getRoleByUserId
    @Test
    void it_should_throw_error_there_is_no_role_by_user_id() throws Exception {
        //given
        RoleDTO roleDTO = firstRoleDTO();
        when(roleService.getRoleByUserId(any())).thenThrow(new ApiException("No role found by name: " + roleDTO.getName()));
        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.get("/roles/roleByUserId/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.authentication(new UsernamePasswordAuthenticationToken(1L, null, List.of(new SimpleGrantedAuthority("USER"))))))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.reason").value("No role found by name: " + roleDTO.getName()));
    }


}