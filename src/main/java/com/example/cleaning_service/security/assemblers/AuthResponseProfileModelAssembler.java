package com.example.cleaning_service.security.assemblers;

import com.example.cleaning_service.security.dtos.auth.AuthResponseProfileModel;
import com.example.cleaning_service.security.entities.user.User;
import com.example.cleaning_service.security.mapper.AuthMapper;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class AuthResponseProfileModelAssembler extends RepresentationModelAssemblerSupport<User, AuthResponseProfileModel> {

    private final AuthMapper authMapper;

    public AuthResponseProfileModelAssembler(Class<?> controllerClass, Class<AuthResponseProfileModel> resourceType, AuthMapper authMapper) {
        super(controllerClass, resourceType);
        this.authMapper = authMapper;
    }

    @Override
    protected @NonNull AuthResponseProfileModel instantiateModel(@NonNull User user) {
        return authMapper.fromUserToAuthResponseProfileModel(user);
    }

    @Override
    public @NonNull AuthResponseProfileModel toModel(@NonNull User user) {
        return createModelWithId("me", user);
    }
}
