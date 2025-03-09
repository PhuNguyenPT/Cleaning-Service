package com.example.cleaning_service.security.mapper;

import com.example.cleaning_service.security.dtos.auth.AuthRequest;
import com.example.cleaning_service.security.dtos.user.UserResponse;
import com.example.cleaning_service.security.dtos.user.UserResponseLogin;
import com.example.cleaning_service.security.entities.user.User;

public class UserMapper {
    public static UserResponse fromUserToUserResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                RoleMapper.fromRoleToRoleResponse(user.getRole()),
                PermissionMapper.fromPermissionSetToPermissionResponseSet(user.getPermissions())
        );
    }

    public static UserResponseLogin fromUserToUserResponseLogin(User user) {
        return new UserResponseLogin(
                user.getId(),
                user.getUsername()
        );
    }
}
