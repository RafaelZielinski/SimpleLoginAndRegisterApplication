package pl.zielinski.SimpleLoginAndRegisterApplication.resource;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.zielinski.SimpleLoginAndRegisterApplication.dto.UserDTO;
import pl.zielinski.SimpleLoginAndRegisterApplication.exception.ApiException;
import pl.zielinski.SimpleLoginAndRegisterApplication.provider.TokenProvider;
import pl.zielinski.SimpleLoginAndRegisterApplication.service.RoleService;
import pl.zielinski.SimpleLoginAndRegisterApplication.service.UserService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * @author rafek
 * @version 1.0
 * @licence ask rafekzielinski@wp.pl
 * @since 16/02/2024
 */
@WebMvcTest
@AutoConfigureMockMvc()
@ActiveProfiles("test")
class UserControllerTest implements UserDTOProvider {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    private RoleService roleService;

    @MockBean
    private UserService userService;

    @MockBean
    private TokenProvider tokenProvider;

    @MockBean
    private AuthenticationManager authenticationManager;


    @DisplayName("Testing method getUser(Long id)")
    @Test
    void itShould_return_one_UserDTO() throws Exception {
        //given
        UserDTO expected = first();
        //when
        when(userService.getUser(any())).thenReturn(first());
        //then
        mockMvc.perform(MockMvcRequestBuilders.get("/users/user/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.authentication(new UsernamePasswordAuthenticationToken(1L, null, List.of(new SimpleGrantedAuthority("USER"))))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("User retrieved"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.user.firstName").value("Rafał"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.size()").value(1));
    }
    @DisplayName("Testing method getUser(Long id)")
    @Test
    void it_should_throw_exception_when_there_is_no_such_user() throws Exception {
        //given
        //when
        when(userService.getUser(any())).thenThrow(new ApiException("There is no such an user at database exists"));
        //then
        mockMvc.perform(MockMvcRequestBuilders.get("/users/user/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.authentication(new UsernamePasswordAuthenticationToken(1L, null, List.of(new SimpleGrantedAuthority("USER"))))))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.reason").value("There is no such an user at database exists"));

    }

}