package com.example.cleaning_service.security.assemblers;

import com.example.cleaning_service.security.controllers.AuthController;
import com.example.cleaning_service.security.dtos.auth.AuthResponseRegisterModel;
import com.example.cleaning_service.security.entities.user.User;
import com.example.cleaning_service.security.mapper.AuthMapper;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Component
public class AuthResponseRegisterModelAssembler extends RepresentationModelAssemblerSupport<User, AuthResponseRegisterModel> {

    public AuthResponseRegisterModelAssembler() {
        super(AuthController.class, AuthResponseRegisterModel.class);
    }

    @Override
    protected @NonNull AuthResponseRegisterModel instantiateModel(@NonNull User user) {
        return AuthMapper.fromUserToAuthResponseRegisterModel(user);
    }

    @Override
    public @NonNull AuthResponseRegisterModel toModel(@NonNull User user) {
        AuthResponseRegisterModel model = AuthMapper.fromUserToAuthResponseRegisterModel(user);

        // Add HATEOAS self-link
        Link selfLink =  linkTo(AuthController.class).slash("register").withSelfRel();
        Link loginLink = linkTo(AuthController.class).slash("login").withRel("login");

        model.add(selfLink, loginLink);

        return model;
    }
}