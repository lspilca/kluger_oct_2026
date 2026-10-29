package com.example.ss_day3_ex3.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "clients")
@Getter @Setter
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "client_id")
    private String clientId;

    @Column(name = "client_secret")
    private String secret;

    @Column(name = "grant_type")
    private String grantType;

    @Column(name = "redirect_uri")
    private String redirectUri;

    private String scope;

}
