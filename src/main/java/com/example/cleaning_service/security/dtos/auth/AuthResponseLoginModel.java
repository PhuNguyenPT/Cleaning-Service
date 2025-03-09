package com.example.cleaning_service.security.dtos.auth;

import org.springframework.hateoas.RepresentationModel;

public class AuthResponseLoginModel extends RepresentationModel<AuthResponseLoginModel> {

    private final String accessToken;
    private final Long expiresIn;

    public AuthResponseLoginModel(String accessToken, Long expiresIn) {
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public String getAccessToken() {
        return accessToken;
    }
}

