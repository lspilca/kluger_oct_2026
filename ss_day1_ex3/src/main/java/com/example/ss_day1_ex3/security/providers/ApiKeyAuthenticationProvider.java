package com.example.ss_day1_ex3.security.providers;

import com.example.ss_day1_ex3.security.authentication.ApiKeyAuthentication;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class ApiKeyAuthenticationProvider implements AuthenticationProvider {

    @Value("${api.key}")
    private String apiKey;

    @Override
    public @Nullable Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String apiKey = String.valueOf(authentication.getPrincipal());

        if (this.apiKey.equals(apiKey)) {
            authentication.setAuthenticated(true);
            return authentication;
        } else {
            throw new AuthenticationServiceException("Authentication error.");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return ApiKeyAuthentication.class.equals(authentication);
    }
}
