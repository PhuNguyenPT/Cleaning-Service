package com.example.cleaning_service.security.assemblers;

import com.example.cleaning_service.security.controllers.AuthController;
import com.example.cleaning_service.security.dtos.auth.AuthResponse;
import com.example.cleaning_service.security.dtos.auth.AuthResponseLoginModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Component
public class AuthResponseModelAssembler extends RepresentationModelAssemblerSupport<AuthResponse, AuthResponseLoginModel> {
    public AuthResponseModelAssembler() {
        super(AuthController.class, AuthResponseLoginModel.class);
    }

    @Override
    protected @NonNull AuthResponseLoginModel instantiateModel(AuthResponse authResponse) {
        return new AuthResponseLoginModel(authResponse.accessToken(), authResponse.expiresIn());
    }

    @Override
    public @NonNull AuthResponseLoginModel toModel(@NonNull AuthResponse authResponse) {
        AuthResponseLoginModel authResponseLoginModel = createModelWithId("me", authResponse);

        Link logoutLink = linkTo(AuthController.class).slash("logout").withRel("logout");
        authResponseLoginModel.add(logoutLink);

        return authResponseLoginModel;
    }
}
