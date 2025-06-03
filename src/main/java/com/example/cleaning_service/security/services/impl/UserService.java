package com.example.cleaning_service.security.services.impl;

import com.example.cleaning_service.security.dtos.auth.AuthRequest;
import com.example.cleaning_service.security.dtos.user.UserRequest;
import com.example.cleaning_service.security.entities.role.ERole;
import com.example.cleaning_service.security.entities.permission.Permission;
import com.example.cleaning_service.security.entities.role.Role;
import com.example.cleaning_service.security.entities.user.User;
import com.example.cleaning_service.security.events.UserDeletedEvent;
import com.example.cleaning_service.security.mapper.AuthMapper;
import com.example.cleaning_service.security.repositories.UserRepository;
import com.example.cleaning_service.security.services.IRoleService;
import com.example.cleaning_service.security.services.IUserService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
class UserService implements IUserService {
    private final UserRepository userRepository;
    private final IRoleService roleService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final AuthMapper authMapper;

    UserService(UserRepository userRepository,
                IRoleService roleService,
                BCryptPasswordEncoder passwordEncoder,
                ApplicationEventPublisher applicationEventPublisher,
                AuthMapper authMapper) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.applicationEventPublisher = applicationEventPublisher;
        this.authMapper = authMapper;
    }

    @Override
    @Transactional
    public User createUser(UserRequest userRequest) {
        User savedUser = saveUser(
                userRequest.username(),
                userRequest.password(),
                userRequest.role()
        );
        log.info("User created: {}", savedUser.getId());
        return savedUser;
    }

    @Transactional
    User saveUser(String username, String password, ERole roleName) {
        Role userRole = roleService.ensureRoleExists(roleName);
        log.info("User role: {}", userRole);

        // ✅ Use existing managed Permission instances
        Set<Permission> existingPermissions = new HashSet<>(userRole.getPermissions());
        log.info("User role permissions: {}", existingPermissions);

        User user = new User(username, passwordEncoder.encode(password), userRole, existingPermissions);
        log.info("Saving user {}", user);

        return userRepository.save(user);
    }

    @Transactional
    @Override
    public User register(AuthRequest authRequest) {
        log.info("Attempting to register a new user with username: {}", authRequest.username());

        if (userRepository.existsByUsername(authRequest.username())) {
            throw new EntityExistsException("Username: '"  + authRequest.username() + "' already exists!");
        }

        User newUserRequest = authMapper.fromAuthRequestToUser(authRequest);

        // 🔹 Ensure the "USER" role exists and fetch it
        Role userRole = roleService.ensureRoleExists(ERole.USER);
        log.info("Fetched role '{}' with {} permissions.", userRole.getName(), userRole.getPermissions().size());

        // 🔹 Create a COPY of the permissions set to avoid shared references
        Set<Permission> copiedPermissions = new HashSet<>(userRole.getPermissions());

        // 🔹 Create User entity with a new set of permissions
        User user = new User(
                newUserRequest.getUsername(),
                passwordEncoder.encode(newUserRequest.getPassword()),
                userRole,
                copiedPermissions // 🔹 Assign the copied set, not the original reference
        );

        User savedUser = userRepository.save(user);
        log.info("User '{}' registered successfully with ID: {}", savedUser.getUsername(), savedUser.getId());

        // 🔹 Save user in the database
        return savedUser;
    }

    @Transactional
    public User findById(UUID id) {
        log.info("Fetching user with ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        log.info("User found: {}", user.getId());
        return user;
    }

    @Transactional
    @Override
    public void deleteUser(UUID id) {
        log.info("Attempting to delete user with ID: {}", id);

        User user = findById(id);
        applicationEventPublisher.publishEvent(new UserDeletedEvent(user));

        userRepository.delete(user);
        log.info("User with ID: {} successfully deleted.", id);

    }

    @Transactional
    @Override
    public User updateUser(UUID id, UserRequest userRequest) {
        log.info("Attempting to update user with ID: {}", id);

        // Find the existing user
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));

        // Update username if provided
        if (userRequest.username() != null && !userRequest.username().isEmpty()) {
            log.info("Updating username for user ID: {} from '{}' to '{}'", id, user.getUsername(), userRequest.username());
            user.setUsername(userRequest.username());
        }

        // Update password if provided
        if (userRequest.password() != null && !userRequest.password().isEmpty()) {
            log.info("Updating password for user ID: {}", id);
            user.setPassword(passwordEncoder.encode(userRequest.password()));
        }

        // Update role if provided
        if (userRequest.role() != null) {
            log.info("Updating role for user ID: {} to '{}'", id, userRequest.role());
            Role newRole = roleService.ensureRoleExists(userRequest.role());

            // 🔹 Create a COPY of the permissions set to avoid shared references
            Set<Permission> copiedPermissions = new HashSet<>(newRole.getPermissions());
            user.addRole(newRole);
            user.setPermissions(copiedPermissions);
        }

        // Save the updated user
        User updatedUser = userRepository.save(user);
        log.info("User with ID: {} successfully updated.", id);
        return updatedUser;
    }

    @Override
    @Transactional
    public User saveUser(User user) {
        return userRepository.saveAndFlush(user);
    }
}
