package com.example.ss_day3_ex2.controller.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class TestUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username.equals("bob"))
            return User.builder()
                .username("bob")
                .password("12345")
                .authorities("read")
                .build();

        return User.builder()
                .username("john")
                .password("12345")
                .authorities("write")
                .build();
    }
}
