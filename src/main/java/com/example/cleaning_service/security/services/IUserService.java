package com.example.cleaning_service.security.services;

import com.example.cleaning_service.security.dtos.auth.AuthRequest;
import com.example.cleaning_service.security.dtos.user.UserRequest;
import com.example.cleaning_service.security.dtos.user.UserResponseModel;
import com.example.cleaning_service.security.entities.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;

import java.util.UUID;

public interface IUserService {
    User findById(UUID userId);
    UserResponseModel createUser(UserRequest userRequest);
    PagedModel<UserResponseModel> findAll(Pageable pageable);
    User register(AuthRequest authRequest);
    UserResponseModel getUserResponseModelById(UUID id);
    void deleteUser(UUID id);
    UserResponseModel updateUser(UUID id, UserRequest userRequest);
    User saveUser(User user);
}