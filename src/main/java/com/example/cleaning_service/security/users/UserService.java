package com.example.cleaning_service.security.users;

import com.example.cleaning_service.security.auth.AuthRequest;
import com.example.cleaning_service.security.roles.*;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleService roleService, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse saveUser(String username, String password, ERole roleName) {
        Role role = roleService.ensureRoleExists(roleName);

        User user = new User(username, passwordEncoder.encode(password), role, role.getPermissions());

        return UserMapper.fromUserToUserResponse(
                userRepository.save(user)
        );
    }

    public List<UserResponse> findAll() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::fromUserToUserResponse)
                .collect(Collectors.toList());
    }

    public UserResponse register(AuthRequest authRequest) {
        if (userRepository.existsByUsername(authRequest.username())) {
            throw new EntityExistsException(authRequest.username() + " already exists!");
        }
        User newUserRequest = UserMapper.fromAuthRequestToUser(authRequest);

        // 🔹 Ensure the "USER" role exists and fetch it
        Role userRole = roleService.ensureRoleExists(ERole.USER);

        // 🔹 Create User entity to persist
        User dbUser = new User(
                newUserRequest.getUsername(),
                newUserRequest.getPassword(),
                userRole,
                userRole.getPermissions()
        );

        // 🔹 Save user in the database
        User savedUser = userRepository.save(dbUser);

        return UserMapper.fromUserToUserResponse(savedUser);
    }

    public UserResponse findById(Long id) {
        return userRepository.findById(id)
                .map(UserMapper::fromUserToUserResponse)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public UserResponse updateUser(Long id, UserRequest userRequest) {
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
            user.setRole(newRole);
            user.setPermissions(newRole.getPermissions()); // Assign permissions based on role
        }

        // Save the updated user
        User updatedUser = userRepository.save(user);

        return UserMapper.fromUserToUserResponse(updatedUser);
    }
}
