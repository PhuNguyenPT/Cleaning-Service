package com.example.cleaning_service.security.dtos.auth;

import org.springframework.hateoas.RepresentationModel;

public class AuthResponseRegisterModel extends RepresentationModel<AuthResponseRegisterModel> {
    private final Long id;
    private final String username;

    public AuthResponseRegisterModel(Long id, String username) {
        this.id = id;
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }
}
