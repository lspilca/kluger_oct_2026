package com.example.ss_day2_ex1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity // @PreAuthorize, @PostAuthorize, @PreFilter, @PostFilter | @Secured | @RollsAllowed
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        http.httpBasic(Customizer.withDefaults());

        http.csrf(c -> c.disable());

        // antMatchers('/admin'), mvcMatchers('/admin'), regexMatchers()
        //
        http.authorizeHttpRequests(
                a -> a
                        .requestMatchers("/read/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers( "/write/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE).hasRole("ADMIN")
                        .requestMatchers("/book").authenticated()
                        .requestMatchers("/products").authenticated()
                        .anyRequest().denyAll()
        );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
