package pl.zielinski.SimpleLoginAndRegisterApplication.resource;

import com.google.gson.JsonObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.convert.DataSizeUnit;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.zielinski.SimpleLoginAndRegisterApplication.domain.Role;
import pl.zielinski.SimpleLoginAndRegisterApplication.domain.User;
import pl.zielinski.SimpleLoginAndRegisterApplication.domain.UserPrincipal;
import pl.zielinski.SimpleLoginAndRegisterApplication.dto.UserDTO;
import pl.zielinski.SimpleLoginAndRegisterApplication.exception.ApiException;
import pl.zielinski.SimpleLoginAndRegisterApplication.provider.TokenProvider;
import pl.zielinski.SimpleLoginAndRegisterApplication.service.RoleService;
import pl.zielinski.SimpleLoginAndRegisterApplication.service.UserService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static java.time.LocalDateTime.now;
import static net.sf.jsqlparser.util.validation.metadata.NamedObject.user;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

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
        mockMvc.perform(get("/users/user/1")
                        .contentType(APPLICATION_JSON)
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
        mockMvc.perform(get("/users/user/1")
                        .contentType(APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.authentication(new UsernamePasswordAuthenticationToken(1L, null, List.of(new SimpleGrantedAuthority("USER"))))))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.reason").value("There is no such an user at database exists"));

    }

    @DisplayName("Testing method getUsers()")
    @Test
    void it_should_return_list_of_two_User_DTO() throws Exception {
        //given
        when(userService.getUsers()).thenReturn(List.of(first(), second()));
        //when
        mockMvc.perform(get("/users/list")
                        .contentType(APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.authentication(new UsernamePasswordAuthenticationToken(1L, null, List.of(new SimpleGrantedAuthority("USER"))))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("List of users"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.users[0].firstName").value("Rafał"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.users.size()").value(2));
        //then
    }

    @DisplayName("Testing method getUsers()")
    @Test
    void it_should_return_empty_list_of_User_DTO() throws Exception {
        //given
        when(userService.getUsers()).thenReturn(Collections.emptyList());
        //when
        mockMvc.perform(get("/users/list")
                        .contentType(APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.authentication(new UsernamePasswordAuthenticationToken(1L, null, List.of(new SimpleGrantedAuthority("USER"))))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("List of users"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.users.size()").value(0));
        //then
    }

    @DisplayName("Testing method getUsers()")
    @Test
    void it_should_throw_api_exception_error_with_derriving_users_from_database() throws Exception {
        //given
        when(userService.getUsers()).thenThrow(new ApiException("There is problem with list of users from database"));
        //when
        mockMvc.perform(get("/users/list")
                        .contentType(APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.authentication(new UsernamePasswordAuthenticationToken(1L, null, List.of(new SimpleGrantedAuthority("USER"))))))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.reason").value("There is problem with list of users from database"));
        //then
    }

    @DisplayName("Testing method register(User user)")
    @Test
    void it_should_register_user() throws Exception {
        //given
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("firstName", "Rafał");
        jsonObject.addProperty("lastName", "Zieliński");
        jsonObject.addProperty("email", "rafekzielinski@wp.pl");
        jsonObject.addProperty("password", "password");
        jsonObject.addProperty("age", 44);


        when(userService.createUser(ArgumentMatchers.any(User.class))).thenReturn(first());

        //when
        mockMvc.perform(post("/users/register")
                        .content(jsonObject.toString())
                        .contentType(APPLICATION_JSON)
                        .with(csrf())
                        .with(SecurityMockMvcRequestPostProcessors.authentication(
                                new UsernamePasswordAuthenticationToken(1L, null, List.of(
                                        new SimpleGrantedAuthority("USER"))))))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("User created"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.user.firstName").value("Rafał"));
        //then
    }

    @DisplayName("Testing method register(User user)")
    @Test
    void it_should_throw_exception_that_such_user_email_have_already_existed() throws Exception {
        //given
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("firstName", "Rafał");
        jsonObject.addProperty("lastName", "Zieliński");
        jsonObject.addProperty("email", "rafekzielinski@wp.pl");
        jsonObject.addProperty("password", "password");
        jsonObject.addProperty("age", 44);


        when(userService.createUser(ArgumentMatchers.any(User.class))).thenThrow(new ApiException("There is already taken that email"));

        //when
        mockMvc.perform(post("/users/register")
                        .content(jsonObject.toString())
                        .contentType(APPLICATION_JSON)
                        .with(csrf())
                        .with(SecurityMockMvcRequestPostProcessors.authentication(
                                new UsernamePasswordAuthenticationToken(1L, null, List.of(
                                        new SimpleGrantedAuthority("USER"))))))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.reason").value("There is already taken that email"));
        //then
    }

    @DisplayName("Testing method login(LoginForm loginForm")
    @Test
    void it_should_successfully_login_to_application() throws Exception {
        //given
        Authentication authentication = mockAuthentication();

        UserDTO loggedInUser = first();

        when(authenticationManager.authenticate(any())).thenReturn(authentication);

//        when(tokenProvider.createAccessToken(any())).thenReturn("mockedAccessToken");
//        when(tokenProvider.createRefreshToken(any())).thenReturn("mockedRefreshToken");


        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("email", "rafekzielinski@wp.pl");
        jsonObject.addProperty("password", "password");

        //when
        mockMvc.perform(post("/users/login")
                .content(jsonObject.toString())
                .contentType(APPLICATION_JSON)
                .with(csrf())
                .with(user("rafekzielinski@wp.pl").roles("USER")
                ))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Login Success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.user.email").value("rafekzielinski@wp.pl"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.access_token").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.refresh_token").exists());
        //then
    }

    private Authentication mockAuthentication() {
        User user = new User(1L, "Rafał", "Zieliński", "rafekzielinski@wp.pl", 15L, "password", true, true, false, now());
        Role role = new Role(1L, "ROLE_USER", "READ:USER,READ:CUSTOMER");
        UserPrincipal userPrincipal = new UserPrincipal(user, role);
        return new UsernamePasswordAuthenticationToken(userPrincipal, null, List.of(new SimpleGrantedAuthority("ROLE_USER")));
    }
}