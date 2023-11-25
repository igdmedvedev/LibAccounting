package com.springtest.crudrest.config;

import com.springtest.crudrest.security.AuthProviderImpl;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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
    private final JwtFilter jwtFilter;

    @Autowired
    public SecurityConfig(AuthProviderImpl authProvider, JwtFilter jwtFilter) {
        this.authProvider = authProvider;
        this.jwtFilter = jwtFilter;
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
    @Order(1)
    public SecurityFilterChain filterChainForApi(HttpSecurity http) throws Exception {
        http.securityMatcher("/api/**")
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/jwt").permitAll()
                        .requestMatchers(antMatcher(HttpMethod.POST, "/api/books/**")).hasRole("ADMIN")
                        .requestMatchers(antMatcher(HttpMethod.DELETE, "/api/books/**")).hasRole("ADMIN")
                        .requestMatchers(antMatcher(HttpMethod.POST, "/api/people/**")).hasRole("ADMIN")
                        .requestMatchers(antMatcher(HttpMethod.DELETE, "/api/people/**")).hasRole("ADMIN")
                        .requestMatchers("/api/**").hasAnyRole("USER", "ADMIN"))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain defaultFilterChain(HttpSecurity http) throws Exception {
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
                        .logoutSuccessUrl("/auth/login"))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));
        return http.build();
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        //static method helps to avoid cycling injections
        return new BCryptPasswordEncoder();
    }
}
