package com.example.ss_authorization_exercise.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;


@Configuration
public class UserConfig {

    private static final String DEMO_PASSWORD = "password";

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService(PasswordEncoder encoder) {
        String encoded = encoder.encode(DEMO_PASSWORD);
        return new InMemoryUserDetailsManager(
                User.withUsername("alice").password(encoded).authorities("ROLE_CUSTOMER").build(),
                User.withUsername("bob").password(encoded).authorities("ROLE_CUSTOMER").build(),
                User.withUsername("manager").password(encoded).authorities("ROLE_MANAGER").build(),
                User.withUsername("admin").password(encoded).authorities("ROLE_ADMIN").build(),
                User.withUsername("refund-agent").password(encoded)
                        .authorities("ROLE_CUSTOMER", "ORDER_REFUND").build()
        );
    }
}
