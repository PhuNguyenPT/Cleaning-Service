package com.example.cleaning_service.security.assemblers;

import com.example.cleaning_service.security.controllers.AuthController;
import com.example.cleaning_service.security.dtos.auth.AuthResponse;
import com.example.cleaning_service.security.dtos.auth.AuthResponseModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Component
public class AuthResponseModelAssembler extends RepresentationModelAssemblerSupport<AuthResponse, AuthResponseModel> {
    public AuthResponseModelAssembler() {
        super(AuthController.class, AuthResponseModel.class);
    }

    @Override
    protected @NonNull AuthResponseModel instantiateModel(AuthResponse authResponse) {
        return new AuthResponseModel(authResponse.accessToken(), authResponse.expiresIn());
    }

    @Override
    public @NonNull AuthResponseModel toModel(@NonNull AuthResponse authResponse) {
        AuthResponseModel authResponseModel = createModelWithId("me", authResponse);

        authResponseModel.add(linkTo(AuthController.class).slash("register").withRel("register"));

        return authResponseModel;
    }
}
