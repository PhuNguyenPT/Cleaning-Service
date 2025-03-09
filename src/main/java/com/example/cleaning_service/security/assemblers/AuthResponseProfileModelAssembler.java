package com.example.cleaning_service.security.assemblers;

import com.example.cleaning_service.security.controllers.AuthController;
import com.example.cleaning_service.security.dtos.auth.AuthResponseProfile;
import com.example.cleaning_service.security.dtos.auth.AuthResponseProfileModel;
import com.example.cleaning_service.security.mapper.AuthMapper;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class AuthResponseProfileModelAssembler extends RepresentationModelAssemblerSupport<AuthResponseProfile, AuthResponseProfileModel> {

    public AuthResponseProfileModelAssembler() {
        super(AuthController.class, AuthResponseProfileModel.class);
    }

    @Override
    public @NonNull AuthResponseProfileModel toModel(@NonNull AuthResponseProfile authResponseProfile) {
        AuthResponseProfileModel model = AuthMapper.fromAuthResponseProfileToModel(authResponseProfile);

        // Add HATEOAS self-link
        model.add(linkTo(AuthController.class).slash("me").withSelfRel());

        return model;
    }
}
