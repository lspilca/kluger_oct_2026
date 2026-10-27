package com.example.ss_authorization_exercise2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class UserConfig {

    static final String PASSWORD = "password";

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        String encoded = passwordEncoder.encode(PASSWORD);
        return new InMemoryUserDetailsManager(
                User.withUsername("alice").password(encoded).authorities("ROLE_EMPLOYEE").build(),
                User.withUsername("bob").password(encoded).authorities("ROLE_EMPLOYEE").build(),
                User.withUsername("emma").password(encoded).authorities("ROLE_EMPLOYEE", "ROOM_APPROVE").build(),
                User.withUsername("carl").password(encoded).authorities("ROLE_COORDINATOR").build(),
                User.withUsername("cora").password(encoded).authorities("ROLE_COORDINATOR", "ROOM_APPROVE").build(),
                User.withUsername("admin").password(encoded).authorities("ROLE_ADMIN").build());
    }
}
