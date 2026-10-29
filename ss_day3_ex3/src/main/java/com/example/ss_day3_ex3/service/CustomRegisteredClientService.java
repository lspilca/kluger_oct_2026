package com.example.ss_day3_ex3.service;

import com.example.ss_day3_ex3.entities.Client;
import com.example.ss_day3_ex3.repository.ClientRepository;
import lombok.AllArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@AllArgsConstructor
public class CustomRegisteredClientService implements RegisteredClientRepository {

    private final ClientRepository clientRepository;

    @Override
    public void save(RegisteredClient registeredClient) {}

    @Override
    public @Nullable RegisteredClient findById(String id) {
        return clientRepository.findById(Long.parseLong(id))
                .map(this::entityToRegisteredClient)
                .orElse(null);
    }

    @Override
    public @Nullable RegisteredClient findByClientId(String clientId) {
        return clientRepository.findClientByClientId(clientId)
                .map(this::entityToRegisteredClient)
                .orElse(null);
    }

    private RegisteredClient entityToRegisteredClient(Client client) {
        return RegisteredClient.withId(String.valueOf(client.getId()))
                .clientId(client.getClientId())
                .clientSecret(client.getSecret())
                .redirectUri(client.getRedirectUri())
                .authorizationGrantType(new AuthorizationGrantType(client.getGrantType()))
                .scope(client.getScope())
                .tokenSettings(TokenSettings.builder().accessTokenTimeToLive(Duration.ofDays(1)).build())
                .build();

    }
}
