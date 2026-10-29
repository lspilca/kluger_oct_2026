package com.example.ss_day3_ex2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtIssuerAuthenticationManagerResolver;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
//        http.oauth2ResourceServer(
//                c -> c.jwt(
//                        j -> j.jwkSetUri("http://localhost:8080/oauth2/jwks")
//                )
//        );

        http.oauth2ResourceServer(
                a -> a.authenticationManagerResolver(
                        JwtIssuerAuthenticationManagerResolver.fromTrustedIssuers(
                                "http://localhost:8080", "http://localhost:7070/realms/master"
                        )
                )
        );

//        http.oauth2ResourceServer(
//                c -> c.opaqueToken(
//                        i -> i.introspectionUri("http://localhost:8080/oauth2/introspect")
//                                .introspectionClientCredentials("resourceserver", "resourceserversecret")
//
//                )
//        );

        http.authorizeHttpRequests(a -> a
                .requestMatchers("/demo").permitAll()
                .requestMatchers("/test").authenticated()
                .requestMatchers("/hello").hasAuthority("read")
                .anyRequest().authenticated());

        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
        authoritiesConverter.setAuthoritiesClaimName("authorities");
        authoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);
        return converter;
    }

}
