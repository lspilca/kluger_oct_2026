package com.example.ss_authorization_exercise2.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
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
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
        http.httpBasic(Customizer.withDefaults());

        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.csrf(csrf -> csrf.disable());

        http.authorizeHttpRequests(
                auth -> auth
                        .requestMatchers(HttpMethod.GET, "/api/rooms").permitAll()
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/api/reservations/*").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/reservations").authenticated()
                        .requestMatchers("/api/**").authenticated()
                        .anyRequest().denyAll());

        return http.build();
    }
}
