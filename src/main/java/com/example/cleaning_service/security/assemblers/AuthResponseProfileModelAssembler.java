package com.example.cleaning_service.security.assemblers;

import com.example.cleaning_service.security.controllers.AuthController;
import com.example.cleaning_service.security.dtos.auth.AuthResponseProfileModel;
import com.example.cleaning_service.security.entities.user.User;
import com.example.cleaning_service.security.mapper.AuthMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class AuthResponseProfileModelAssembler extends RepresentationModelAssemblerSupport<User, AuthResponseProfileModel> {
    private final HttpServletRequest httpServletRequest;

    public AuthResponseProfileModelAssembler(HttpServletRequest httpServletRequest) {
        super(AuthController.class, AuthResponseProfileModel.class);
        this.httpServletRequest = httpServletRequest;
    }

    @Override
    public @NonNull AuthResponseProfileModel toModel(@NonNull User user) {
        AuthResponseProfileModel model = AuthMapper.fromUserToAuthResponseProfileModel(user);

        // Add HATEOAS self-link
        Link selfLink =  linkTo(methodOn(AuthController.class).getAuthenticatedUser(user)).withSelfRel();
        Link logoutLink = linkTo(methodOn(AuthController.class).logout(httpServletRequest)).withRel("logout");
        model.add(selfLink, logoutLink);

        return model;
    }
}
