package com.example.ss_day3_ex1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.authorization.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;

import java.time.Duration;
import java.util.UUID;

@Configuration
public class SecurityConfig {

    @Bean
    @Order(1)
    public SecurityFilterChain asFilterChain(HttpSecurity http) {
        OAuth2AuthorizationServerConfigurer configurer = new OAuth2AuthorizationServerConfigurer();

        http.securityMatcher(configurer.getEndpointsMatcher())
                .with(configurer, as -> as.oidc(Customizer.withDefaults()));

        http.authorizeHttpRequests(a -> a.anyRequest().authenticated());

        // 401
        http.exceptionHandling(e -> e.defaultAuthenticationEntryPointFor(
                new LoginUrlAuthenticationEntryPoint("/login"), new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
        ));

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain appFilterChain(HttpSecurity http) {
        http.formLogin(Customizer.withDefaults()); //  /login

        http.authorizeHttpRequests(a -> a.anyRequest().authenticated());

        return http.build();
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().build();
    }

    // client
    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        RegisteredClient c1 = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("client")
                .clientSecret("secret")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .redirectUri("http://localhost:4040/code")
                .scope("OPENID")
                .tokenSettings(TokenSettings.builder().accessTokenTimeToLive(Duration.ofDays(1)).build())
                .clientSettings(ClientSettings.builder().requireProofKey(true).build())
                .build();

        return new InMemoryRegisteredClientRepository(c1);
    }

    // users
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails u1 = User.builder()
                .username("bob")
                .password("12345")
                .authorities("read")
                .build();

        return new InMemoryUserDetailsManager(u1);
    }


    // password encoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
