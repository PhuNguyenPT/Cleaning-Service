package com.example.cleaning_service.security.assemblers;

import com.example.cleaning_service.security.dtos.auth.AuthResponseRegisterModel;
import com.example.cleaning_service.security.entities.user.User;
import com.example.cleaning_service.security.mapper.AuthMapper;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class AuthResponseRegisterModelAssembler extends RepresentationModelAssemblerSupport<User, AuthResponseRegisterModel> {

    /**
     * Creates a new {@link RepresentationModelAssemblerSupport} using the given controller class and resource type.
     *
     * @param controllerClass must not be {@literal null}.
     * @param resourceType    must not be {@literal null}.
     */
    public AuthResponseRegisterModelAssembler(Class<?> controllerClass, Class<AuthResponseRegisterModel> resourceType) {
        super(controllerClass, resourceType);
    }

    @Override
    protected @NonNull AuthResponseRegisterModel instantiateModel(@NonNull User user) {
        return AuthMapper.fromUserToAuthResponseRegisterModel(user);
    }

    @Override
    public @NonNull AuthResponseRegisterModel toModel(@NonNull User user) {
        return createModelWithId("login", user);
    }
}