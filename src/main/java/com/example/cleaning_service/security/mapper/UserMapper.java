package com.example.cleaning_service.security.mapper;

import com.example.cleaning_service.security.dtos.user.UserResponseModel;
import com.example.cleaning_service.security.entities.user.User;

public class UserMapper {

    public static UserResponseModel fromUserToUserResponseModel(User user) {
        return new UserResponseModel(
                user.getId(),
                user.getUsername(),
                RoleMapper.fromRoleToRoleResponse(user.getRole()),
                PermissionMapper.fromPermissionSetToPermissionResponseSet(user.getPermissions())
        );
    }
}
