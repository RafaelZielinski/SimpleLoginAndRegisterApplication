package pl.zielinski.SimpleLoginAndRegisterApplication.configuration;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import pl.zielinski.SimpleLoginAndRegisterApplication.filter.CustomAuthorizationFilter;
import pl.zielinski.SimpleLoginAndRegisterApplication.handler.CustomAccessDeniedHandler;
import pl.zielinski.SimpleLoginAndRegisterApplication.handler.CustomAuthenticationEntryPoint;
import pl.zielinski.SimpleLoginAndRegisterApplication.provider.TokenProvider;

/**
 * @author rafek
 * @version 1.0
 * @licence ask rafekzielinski@wp.pl
 * @since 05/02/2024
 */
@TestConfiguration
@EnableWebSecurity
public class SecurityTestConfiguration extends SecurityConfiguration {

    public SecurityTestConfiguration(CustomAccessDeniedHandler customAccessDeniedHandler, CustomAuthenticationEntryPoint customAuthenticationEntryPoint, UserDetailsService userDetailsService, BCryptPasswordEncoder encoder, CustomAuthorizationFilter customAuthorizationFilter) {
        super(customAccessDeniedHandler, customAuthenticationEntryPoint, userDetailsService, encoder, customAuthorizationFilter);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return super.securityFilterChain(http);
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return super.authenticationManager();
    }

//    @Bean
//    public TokenProvider tokenProvider() {
//
//    }

}
