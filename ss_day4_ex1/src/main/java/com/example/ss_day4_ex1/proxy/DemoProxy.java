package com.example.ss_day4_ex1.proxy;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class DemoProxy {

    private final RestClient restClient;

    public DemoProxy(@Qualifier("RC1") RestClient restClient) {
        this.restClient = restClient;
    }

    public String demo() {
        return restClient.get()
                .uri("/demo")
                .retrieve()
                .body(String.class);
    }
}
