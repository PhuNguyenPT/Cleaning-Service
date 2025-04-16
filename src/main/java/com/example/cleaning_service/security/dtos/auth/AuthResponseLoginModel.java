package com.example.cleaning_service.security.dtos.auth;

import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

@Getter
public class AuthResponseLoginModel extends RepresentationModel<AuthResponseLoginModel> {

    private final String accessToken;
    private final Long expiresIn;

    public AuthResponseLoginModel(String accessToken, Long expiresIn) {
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
    }

}

