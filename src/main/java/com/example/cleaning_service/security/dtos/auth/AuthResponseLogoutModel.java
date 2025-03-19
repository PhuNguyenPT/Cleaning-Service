package com.example.cleaning_service.security.dtos.auth;

import org.springframework.hateoas.RepresentationModel;

public class AuthResponseLogoutModel extends RepresentationModel<AuthResponseLogoutModel> {
    private final String message;

    public String getMessage() {
        return message;
    }

    public AuthResponseLogoutModel(String message) {
        this.message = message;
    }
}
