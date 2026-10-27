package com.example.ss_day1_ex3.config;

import com.example.ss_day1_ex3.security.filters.ApiKeyFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@AllArgsConstructor
public class SecurityConfig {

    private final ApiKeyFilter apiKeyFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        http.addFilterAt(apiKeyFilter, BasicAuthenticationFilter.class);

        http.authorizeHttpRequests(a -> a.anyRequest().authenticated());

        return http.build();
    }
}
