package com.example.springboot.config;

import com.example.springboot.feature_users.jwt.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Security configuration class for handling authentication and authorization in the application.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    /**
     * Bean for encoding passwords using BCrypt hashing algorithm.
     *
     * @return PasswordEncoder instance of BCryptPasswordEncoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Bean to provide the AuthenticationManager, which is used for authentication processes.
     *
     * @param authConfig The authentication configuration provided by Spring Security.
     * @return An instance of AuthenticationManager.
     * @throws Exception If an authentication manager cannot be created.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig)
            throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /**
     * Configures the security filter chain to enforce authentication and authorization rules.
     *
     * @param http The HttpSecurity object to configure security settings.
     * @param jwtFilter The custom JWT filter to validate and authenticate users based on JWT
     *     tokens.
     * @return The configured SecurityFilterChain.
     * @throws Exception If there is an error in configuring security.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtFilter jwtFilter)
            throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        auth ->
                                auth.requestMatchers(
                                                "/users/login", "/users/register", "/actuator/**")
                                        .permitAll()
                                        .requestMatchers("/transactions/credit")
                                        .hasAnyRole("ADMIN", "USER")
                                        .requestMatchers("/transactions/debit")
                                        .hasRole("ADMIN")
                                        .anyRequest()
                                        .authenticated())
                .sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin(AbstractHttpConfigurer::disable);

        return http.build();
    }
}
