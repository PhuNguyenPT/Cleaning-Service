package com.example.cleaning_service.security.dtos.auth;

import org.springframework.hateoas.RepresentationModel;

public class AuthResponseModel extends RepresentationModel<AuthResponseModel> {

    private final String accessToken;
    private final Long expiresIn;

    public AuthResponseModel(String accessToken, Long expiresIn) {
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

