package com.example.cleaning_service.security.dtos.auth;

import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

import java.util.UUID;

@Getter
public class AuthResponseRegisterModel extends RepresentationModel<AuthResponseRegisterModel> {
    private final UUID id;
    private final String username;

    public AuthResponseRegisterModel(UUID id, String username) {
        this.id = id;
        this.username = username;
    }

}
