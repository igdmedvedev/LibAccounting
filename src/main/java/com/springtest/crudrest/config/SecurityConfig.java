package com.springtest.crudrest.config;

import com.springtest.crudrest.security.AuthProviderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final static String[] permitedAllUrls = {
            "/auth/login",
            "/error",
            "/css/**",
            "/auth/registration"
    };

    private final AuthProviderImpl authProvider;
    @Autowired
    public SecurityConfig(AuthProviderImpl authProvider) {
        this.authProvider = authProvider;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        /*
        Authentication logic is very simple.
        Provider can be replaced by LibrarianDetailService
        and in this case you may not implement AuthProviderImpl.
        */
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                        .requestMatchers(antMatcher(HttpMethod.POST, "/books/**")).hasRole("ADMIN")
                        .requestMatchers(antMatcher(HttpMethod.PATCH, "/books/**")).hasRole("ADMIN")
                        .requestMatchers(antMatcher(HttpMethod.DELETE, "/books/**")).hasRole("ADMIN")
                        .requestMatchers(antMatcher(HttpMethod.POST, "/people/**")).hasRole("ADMIN")
                        .requestMatchers(antMatcher(HttpMethod.PATCH, "/people/**")).hasRole("ADMIN")
                        .requestMatchers(antMatcher(HttpMethod.DELETE, "/people/**")).hasRole("ADMIN")
                        .requestMatchers("/books/new", "/books/*/edit", "/people/new", "/people/*/edit").hasRole("ADMIN")
                        .requestMatchers(permitedAllUrls).permitAll()
                        .anyRequest().hasAnyRole("USER", "ADMIN"))
                .formLogin(formLogin -> formLogin
                        .loginPage("/auth/login")
                        .loginProcessingUrl("/process_login")
                        .defaultSuccessUrl("/people")
                        .failureUrl("/auth/login?error"))
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/auth/login"));
        return http.build();
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        //static method helps to avoid cycling injections
        return new BCryptPasswordEncoder();
    }
}
