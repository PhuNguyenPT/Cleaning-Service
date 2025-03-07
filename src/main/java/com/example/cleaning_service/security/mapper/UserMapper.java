package com.example.cleaning_service.security.mapper;

import com.example.cleaning_service.security.dtos.auth.AuthRequest;
import com.example.cleaning_service.security.dtos.user.UserRequest;
import com.example.cleaning_service.security.dtos.user.UserResponse;
import com.example.cleaning_service.security.dtos.user.UserResponseLogin;
import com.example.cleaning_service.security.entities.user.User;

public class UserMapper {
    public static UserResponse fromUserToUserResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getRole().getName(),
                PermissionMapper.fromPermissionSetToPermissionResponseSet(user.getPermissions())
        );
    }

    public static UserResponseLogin fromUserToUserResponseLogin(User user) {
        return new UserResponseLogin(
                user.getId(),
                user.getUsername()
        );
    }

    public static User fromAuthRequestToUser(AuthRequest authRequest) {
        return new User(
                authRequest.username(),
                authRequest.password()
        );
    }

    public static User fromUserRequestToUser(UserRequest userRequest) {
        return new User(
                userRequest.username(),
                userRequest.password()
        );
    }
}
