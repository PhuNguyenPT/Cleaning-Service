package com.example.cleaning_service.security.services;

import com.example.cleaning_service.security.dtos.auth.AuthRequest;
import com.example.cleaning_service.security.dtos.user.UserRequest;
import com.example.cleaning_service.security.entities.user.User;

import java.util.UUID;

public interface IUserService {
    User findById(UUID userId);
    User createUser(UserRequest userRequest);
    User register(AuthRequest authRequest);
    void deleteUser(UUID id);
    User updateUser(UUID id, UserRequest userRequest);
    User saveUser(User user);
}