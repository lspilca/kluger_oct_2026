package com.example.ss_day3_ex3.security;

import com.example.ss_day3_ex3.entities.User;
import lombok.AllArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@AllArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final User adaptee;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return adaptee.getAuthorities()
                .stream()
                .map(a -> new SimpleGrantedAuthority(a.getName()))
                .toList();
    }

    @Override
    public @Nullable String getPassword() {
        return adaptee.getPassword();
    }

    @Override
    public String getUsername() {
        return adaptee.getUsername();
    }
}
