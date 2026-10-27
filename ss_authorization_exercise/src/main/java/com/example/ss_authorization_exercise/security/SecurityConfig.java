package com.example.ss_authorization_exercise.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(
                        auth ->
                                auth
                                        .requestMatchers(HttpMethod.GET, "/api/products").permitAll()
                                        .requestMatchers("/api/orders/*").authenticated()
                                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                                        .requestMatchers(HttpMethod.POST, "/api/orders").authenticated()
                                        .requestMatchers("/api/**").authenticated()
                                        .anyRequest().denyAll()
                );

        return http.build();
    }
}
