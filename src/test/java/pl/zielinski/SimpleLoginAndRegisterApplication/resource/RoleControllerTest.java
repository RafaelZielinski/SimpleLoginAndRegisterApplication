package pl.zielinski.SimpleLoginAndRegisterApplication.resource;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.zielinski.SimpleLoginAndRegisterApplication.configuration.SecurityTestConfiguration;
import pl.zielinski.SimpleLoginAndRegisterApplication.dto.RoleDTO;
import pl.zielinski.SimpleLoginAndRegisterApplication.dto.UserDTO;
import pl.zielinski.SimpleLoginAndRegisterApplication.service.RoleService;

import java.util.List;

import static org.mockito.Mockito.when;

/**
 * @author rafek
 * @version 1.0
 * @licence ask rafekzielinski@wp.pl
 * @since 05/02/2024
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(RoleController.class)
@Import(SecurityTestConfiguration.class)
class RoleControllerTest implements RoleDTOProvider{

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private RoleService roleService;

    @InjectMocks
    private RoleController roleController;


    @Test
    void it_should_return_list_of_two_roles() throws Exception {
        //given
        when(roleService.getRoles()).thenReturn(List.of(firstRoleDTO(), secondRoleDTO()));
        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.get("/list")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("List of roles"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.roles").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.roles[0]").value(firstRoleDTO()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.roles[1]").value(secondRoleDTO()));

    }
}