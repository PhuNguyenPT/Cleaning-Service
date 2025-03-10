package com.example.cleaning_service.security.services;

import com.example.cleaning_service.security.dtos.auth.AuthRequest;
import com.example.cleaning_service.security.dtos.user.UserRequest;
import com.example.cleaning_service.security.entities.role.ERole;
import com.example.cleaning_service.security.entities.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IUserService {
    User saveUser(String username, String password, ERole roleName);
    Page<User> findAll(Pageable pageable);
    User register(AuthRequest authRequest);
    User findById(Long id);
    void deleteUser(Long id);
    User updateUser(Long id, UserRequest userRequest);
}