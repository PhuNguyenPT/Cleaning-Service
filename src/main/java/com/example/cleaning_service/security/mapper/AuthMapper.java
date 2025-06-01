package com.example.cleaning_service.security.mapper;

import com.example.cleaning_service.security.dtos.auth.*;
import com.example.cleaning_service.security.entities.user.User;
import org.springframework.stereotype.Component;

@Component
public class AuthMapper {
    public AuthResponseProfileModel fromUserToAuthResponseProfileModel(User user) {
        return new AuthResponseProfileModel(
                user.getId(),
                user.getUsername(),
                RoleMapper.fromRolesToRoleResponseSet(user.getRoles()),
                PermissionMapper.fromPermissionSetToPermissionResponseSet(user.getPermissions())
        );
    }

    public User fromAuthRequestToUser(AuthRequest authRequest) {
        return new User(
                authRequest.username(),
                authRequest.password()
        );
    }

    public AuthResponseRegisterModel fromUserToAuthResponseRegisterModel(User user) {
        return new AuthResponseRegisterModel(
                user.getId(),
                user.getUsername()
        );
    }
}
