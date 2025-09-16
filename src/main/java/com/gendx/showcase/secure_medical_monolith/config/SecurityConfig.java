package com.gendx.showcase.secure_medical_monolith.config;

import com.gendx.showcase.secure_medical_monolith.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Security configuration for the application.
 * This class defines the security policies, filter chain, and which endpoints are public or protected.
 */

@Configuration
@EnableWebSecurity
@EnableMethodSecurity //Critical: This enables @PreAuthorize annotations on controllers
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Defines the primary security filter chain bean that governs all HTTP security.
    */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity)
        throws Exception {

        httpSecurity.csrf(csrf -> csrf.disable())
                // 1. Disable CSRF protection, as it's not needed for stateless REST APIs using JWTs

                // 2. Define authorization rules for HTTP requests
                    .authorizeHttpRequests(auth -> auth
                            // The /auth/login endpoint is public and accessible to anyone

                        .requestMatchers("/auth/login").permitAll()

                        .anyRequest().authenticated()) // All other endpoints require authentication

                // 3. Configure session management to be STATELESS.
                // This is critical for JWT-based security, as the server will not create or maintain any user sessions.
                    .sessionManagement(session -> session
                            .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 4. Add our custom JWT filter to the security chain
                // It is placed before the standard UsernamePasswordAuthenticationFilter to process the JWT first
                    .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }
}
