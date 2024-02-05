package pl.zielinski.SimpleLoginAndRegisterApplication.configuration;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import pl.zielinski.SimpleLoginAndRegisterApplication.filter.CustomAuthorizationFilter;
import pl.zielinski.SimpleLoginAndRegisterApplication.handler.CustomAccessDeniedHandler;
import pl.zielinski.SimpleLoginAndRegisterApplication.handler.CustomAuthenticationEntryPoint;

/**
 * @author rafek
 * @version 1.0
 * @licence ask rafekzielinski@wp.pl
 * @since 05/02/2024
 */
public class SecurityTestConfiguration extends SecurityConfiguration {

    public SecurityTestConfiguration(CustomAccessDeniedHandler customAccessDeniedHandler, CustomAuthenticationEntryPoint customAuthenticationEntryPoint, UserDetailsService userDetailsService, BCryptPasswordEncoder encoder, CustomAuthorizationFilter customAuthorizationFilter) {
        super(customAccessDeniedHandler, customAuthenticationEntryPoint, userDetailsService, encoder, customAuthorizationFilter);
    }

}
