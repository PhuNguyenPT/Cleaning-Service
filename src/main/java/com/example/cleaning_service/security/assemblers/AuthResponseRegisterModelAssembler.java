package com.example.cleaning_service.security.assemblers;

import com.example.cleaning_service.security.controllers.AuthController;
import com.example.cleaning_service.security.dtos.auth.AuthResponseRegister;
import com.example.cleaning_service.security.dtos.auth.AuthResponseRegisterModel;
import com.example.cleaning_service.security.mapper.AuthMapper;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Component
public class AuthResponseRegisterModelAssembler extends RepresentationModelAssemblerSupport<AuthResponseRegister, AuthResponseRegisterModel> {

    public AuthResponseRegisterModelAssembler() {
        super(AuthController.class, AuthResponseRegisterModel.class);
    }

    @Override
    public @NonNull AuthResponseRegisterModel toModel(@NonNull AuthResponseRegister authResponseRegister) {
        AuthResponseRegisterModel model = AuthMapper.fromAuthResponseRegisterToModel(authResponseRegister);

        // Add HATEOAS self-link
        Link loginLink = linkTo(AuthController.class).slash("register").withSelfRel();
        Link selfLink = linkTo(AuthController.class).slash("login").withRel("login");

        model.add(selfLink);
        model.add(loginLink);

        return model;
    }
}