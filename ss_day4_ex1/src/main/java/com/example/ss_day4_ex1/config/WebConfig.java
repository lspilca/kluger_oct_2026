package com.example.ss_day4_ex1.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.client.OAuth2ClientHttpRequestInterceptor;
import org.springframework.web.client.RestClient;

@Configuration
public class WebConfig {

    @Bean
    @Qualifier("RC1")
    public RestClient restClient(OAuth2AuthorizedClientManager auth2AuthorizedClientManager) {
        OAuth2ClientHttpRequestInterceptor interceptor = new OAuth2ClientHttpRequestInterceptor(auth2AuthorizedClientManager);

        interceptor.setClientRegistrationIdResolver(c -> "client1");

        return RestClient.builder()
                .requestInterceptor(interceptor)
                .baseUrl("http://localhost:9090")
                .build();
    }

    @Bean
    @Qualifier("RC2")
    public RestClient restClient2(OAuth2AuthorizedClientManager auth2AuthorizedClientManager) {
        OAuth2ClientHttpRequestInterceptor interceptor = new OAuth2ClientHttpRequestInterceptor(auth2AuthorizedClientManager);

        interceptor.setClientRegistrationIdResolver(c -> "client2");

        return RestClient.builder()
                .requestInterceptor(interceptor)
                .baseUrl("http://localhost:2020")
                .build();
    }

    @Bean
    OAuth2AuthorizedClientManager auth2AuthorizedClientManager(
        ClientRegistrationRepository clientRegistrationRepository,
        OAuth2AuthorizedClientService auth2AuthorizedClientService
    ) {
        OAuth2AuthorizedClientProvider provider = OAuth2AuthorizedClientProviderBuilder
                .builder()
                .clientCredentials() // grant type
                .build();

        AuthorizedClientServiceOAuth2AuthorizedClientManager manager =
                new AuthorizedClientServiceOAuth2AuthorizedClientManager(
                        clientRegistrationRepository,
                        auth2AuthorizedClientService);

        manager.setAuthorizedClientProvider(provider);

        return manager;
    }
}
