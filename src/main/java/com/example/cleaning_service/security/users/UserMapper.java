package com.example.cleaning_service.security.users;

import com.example.cleaning_service.security.auth.AuthRequest;

public class UserMapper {
    public static UserResponse fromUserToUserResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getRole().getName(),
                user.getPermissions()
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
