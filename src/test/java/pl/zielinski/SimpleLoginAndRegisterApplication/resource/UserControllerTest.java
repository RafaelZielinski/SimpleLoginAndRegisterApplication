package pl.zielinski.SimpleLoginAndRegisterApplication.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import pl.zielinski.SimpleLoginAndRegisterApplication.provider.TokenProvider;
import pl.zielinski.SimpleLoginAndRegisterApplication.service.RoleService;
import pl.zielinski.SimpleLoginAndRegisterApplication.service.UserService;

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
}