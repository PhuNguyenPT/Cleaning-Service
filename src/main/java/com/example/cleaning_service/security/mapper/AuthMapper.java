package com.example.cleaning_service.security.mapper;

import com.example.cleaning_service.security.dtos.auth.*;
import com.example.cleaning_service.security.entities.user.User;

public class AuthMapper {
    public static AuthResponseProfileModel fromAuthResponseProfileToModel(AuthResponseProfile authResponseProfile) {
        return new AuthResponseProfileModel(
                authResponseProfile.id(),
                authResponseProfile.username(),
                authResponseProfile.role(),
                authResponseProfile.permissions()
        );
    }

    public static AuthResponseProfile fromUserToAuthResponseProfile(User user) {
        return new AuthResponseProfile(
                user.getId(),
                user.getUsername(),
                RoleMapper.fromRoleToRoleResponse(user.getRole()),
                PermissionMapper.fromPermissionSetToPermissionResponseSet(user.getPermissions())
        );
    }

    public static User fromAuthRequestToUser(AuthRequest authRequest) {
        return new User(
                authRequest.username(),
                authRequest.password()
        );
    }

    public static AuthResponseRegister fromUserToAuthResponseRegister(User user) {
        return new AuthResponseRegister(
                user.getId(),
                user.getUsername()
        );
    }

    public static AuthResponseRegisterModel fromAuthResponseRegisterToModel(AuthResponseRegister authResponseRegister) {
        return new AuthResponseRegisterModel(
                authResponseRegister.id(),
                authResponseRegister.username()
        );
    }
}
