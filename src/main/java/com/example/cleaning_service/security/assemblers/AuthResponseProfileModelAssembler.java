package com.example.cleaning_service.security.assemblers;

import com.example.cleaning_service.security.controllers.AuthController;
import com.example.cleaning_service.security.dtos.auth.AuthResponseProfileModel;
import com.example.cleaning_service.security.entities.user.User;
import com.example.cleaning_service.security.mapper.AuthMapper;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Component
public class AuthResponseProfileModelAssembler extends RepresentationModelAssemblerSupport<User, AuthResponseProfileModel> {

    public AuthResponseProfileModelAssembler() {
        super(AuthController.class, AuthResponseProfileModel.class);
    }

    @Override
    public @NonNull AuthResponseProfileModel toModel(@NonNull User user) {
        AuthResponseProfileModel model = AuthMapper.fromUserToAuthResponseProfileModel(user);

        // Add HATEOAS self-link
        Link selfLink =  linkTo(AuthController.class).slash("me").withSelfRel();
        Link logoutLink = linkTo(AuthController.class).slash("logout").withRel("logout");
        model.add(selfLink, logoutLink);

        return model;
    }
}
