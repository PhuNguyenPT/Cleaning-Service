package com.example.cleaning_service.security.services.impl;

import com.example.cleaning_service.security.dtos.auth.AuthRequest;
import com.example.cleaning_service.security.dtos.user.UserRequest;
import com.example.cleaning_service.security.entities.role.ERole;
import com.example.cleaning_service.security.entities.permission.Permission;
import com.example.cleaning_service.security.entities.role.Role;
import com.example.cleaning_service.security.entities.user.User;
import com.example.cleaning_service.security.mapper.AuthMapper;
import com.example.cleaning_service.security.repositories.UserRepository;
import com.example.cleaning_service.security.services.IRoleService;
import com.example.cleaning_service.security.services.IUserService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final IRoleService roleService;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, IRoleService roleService, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @Override
    public User saveUser(String username, String password, ERole roleName) {
        Role userRole = roleService.ensureRoleExists(roleName);

        // ✅ Use existing managed Permission instances
        Set<Permission> existingPermissions = new HashSet<>(userRole.getPermissions());

        User user = new User(username, passwordEncoder.encode(password), userRole, existingPermissions);

        return userRepository.save(user);
    }

    @Transactional
    @Override
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Transactional
    @Override
    public User register(AuthRequest authRequest) {
        if (userRepository.existsByUsername(authRequest.username())) {
            throw new EntityExistsException("Username: '"  + authRequest.username() + "' already exists!");
        }
        User newUserRequest = AuthMapper.fromAuthRequestToUser(authRequest);

        // 🔹 Ensure the "USER" role exists and fetch it
        Role userRole = roleService.ensureRoleExists(ERole.USER);

        // 🔹 Create a COPY of the permissions set to avoid shared references
        Set<Permission> copiedPermissions = new HashSet<>(userRole.getPermissions());

        // 🔹 Create User entity with a new set of permissions
        User dbUser = new User(
                newUserRequest.getUsername(),
                passwordEncoder.encode(newUserRequest.getPassword()),
                userRole,
                copiedPermissions // 🔹 Assign the copied set, not the original reference
        );

        // 🔹 Save user in the database

        return userRepository.save(dbUser);
    }

    @Transactional
    @Override
    public User findById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
    }

    @Transactional
    @Override
    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
    }

    @Transactional
    @Override
    public User updateUser(UUID id, UserRequest userRequest) {
        // Find the existing user
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));

        // Update username if provided
        if (userRequest.username() != null && !userRequest.username().isEmpty()) {
            user.setUsername(userRequest.username());
        }

        // Update password if provided
        if (userRequest.password() != null && !userRequest.password().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userRequest.password()));
        }

        // Update role if provided
        if (userRequest.role() != null) {
            Role newRole = roleService.ensureRoleExists(userRequest.role());
            // 🔹 Create a COPY of the permissions set to avoid shared references
            Set<Permission> copiedPermissions = new HashSet<>(newRole.getPermissions());
            user.setRole(newRole);
            user.setPermissions(copiedPermissions); // Assign permissions based on role
        }

        // Save the updated user
        return userRepository.save(user);
    }
}
