package com.example.cleaning_service.security.assemblers;

import com.example.cleaning_service.security.controllers.UserController;
import com.example.cleaning_service.security.dtos.user.UserResponse;
import com.example.cleaning_service.security.dtos.user.UserResponseModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserResponseModelAssembler extends RepresentationModelAssemblerSupport<UserResponse, UserResponseModel> {

    public UserResponseModelAssembler() {
        super(UserController.class, UserResponseModel.class);
    }

    @Override
    public UserResponseModel toModel(UserResponse userResponse) {
        UserResponseModel model = new UserResponseModel(
                userResponse.id(),
                userResponse.username(),
                userResponse.role(),
                userResponse.permissions()
        );

        // Add HATEOAS self-link
        model.add(linkTo(methodOn(UserController.class)
                .getUserById(userResponse.id())).withSelfRel());

        return model;
    }
}
