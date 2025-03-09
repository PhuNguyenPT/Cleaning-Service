package com.example.cleaning_service.security.mapper;

import com.example.cleaning_service.security.dtos.user.UserResponse;
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
}
