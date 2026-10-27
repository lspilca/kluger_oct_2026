package com.example.ss_day1_ex1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        // AUTHENTICATION
        http.httpBasic(Customizer.withDefaults());

        // AUTHORIZATION
        http.authorizeHttpRequests(
                a -> a.anyRequest().authenticated()
        );

        return http.build();
    }

    // UserDetailsService, PasswordEncoder

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails u1 = User.builder()
                .username("bob")
                .password(passwordEncoder().encode("12345"))
                .authorities("read")
                .build();

        return new InMemoryUserDetailsManager(u1);
    }

    @Bean // encoding, encryption, hashing (I -> O, match(I, O))
    public PasswordEncoder passwordEncoder() { // MD5, SHA1, SHA2, SHA3
        // BCrypt, SCrypt, Argon2 -> I -> O (salt)
        return new BCryptPasswordEncoder();
    }

}
