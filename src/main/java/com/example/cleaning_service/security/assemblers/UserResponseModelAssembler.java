package com.example.cleaning_service.security.assemblers;

import com.example.cleaning_service.security.controllers.AdminController;
import com.example.cleaning_service.security.dtos.user.UserResponseModel;
import com.example.cleaning_service.security.entities.user.User;
import com.example.cleaning_service.security.mapper.UserMapper;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class UserResponseModelAssembler extends RepresentationModelAssemblerSupport<User, UserResponseModel> {

    private final UserMapper userMapper;

    public UserResponseModelAssembler(UserMapper userMapper) {
        super(AdminController.class, UserResponseModel.class);
        this.userMapper = userMapper;
    }

    @Override
    protected @NonNull UserResponseModel instantiateModel(@NonNull User user) {
        return userMapper.fromUserToUserResponseModel(user);
    }

    @Override
    public @NonNull UserResponseModel toModel(@NonNull User user) {
        return createModelWithId(user.getId(), user);
    }
}
