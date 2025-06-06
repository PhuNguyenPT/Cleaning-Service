package com.example.cleaning_service.security.mapper;

import com.example.cleaning_service.security.dtos.user.UserResponseModel;
import com.example.cleaning_service.security.entities.user.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponseModel fromUserToUserResponseModel(User user) {
        return new UserResponseModel(
                user.getId(),
                user.getUsername(),
                RoleMapper.fromRolesToRoleResponseSet(user.getRoles()),
                PermissionMapper.fromPermissionSetToPermissionResponseSet(user.getPermissions())
        );
    }
}
