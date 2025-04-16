package com.example.cleaning_service.security.services.impl;

import com.example.cleaning_service.providers.events.ProviderCreatedEvent;
import com.example.cleaning_service.security.assemblers.UserResponseModelAssembler;
import com.example.cleaning_service.security.controllers.AdminController;
import com.example.cleaning_service.security.dtos.auth.AuthRequest;
import com.example.cleaning_service.security.dtos.user.UserRequest;
import com.example.cleaning_service.security.dtos.user.UserResponseModel;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
class UserService implements IUserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final IRoleService roleService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final PagedResourcesAssembler<User> pagedResourcesAssembler;
    private final UserResponseModelAssembler userResponseModelAssembler;

    UserService(UserRepository userRepository,
                IRoleService roleService,
                BCryptPasswordEncoder passwordEncoder,
                ApplicationEventPublisher applicationEventPublisher,
                UserResponseModelAssembler userResponseModelAssembler,
                PagedResourcesAssembler<User> pagedResourcesAssembler) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.applicationEventPublisher = applicationEventPublisher;
        this.pagedResourcesAssembler = pagedResourcesAssembler; // Injected, not new
        this.userResponseModelAssembler = userResponseModelAssembler;
    }

    @Override
    @Transactional
    public UserResponseModel createUser(UserRequest userRequest) {
        User savedUser = saveUser(
                userRequest.username(),
                userRequest.password(),
                userRequest.role()
        );

        // Convert to UserResponseModel using assembler
        UserResponseModel userResponseModel = userResponseModelAssembler.toModel(savedUser);

        // Add a link for all users
        PageRequest pageRequest = PageRequest.of(0, 20);
        Link allUsersLink = linkTo(methodOn(AdminController.class).getAllUsers(pageRequest)).withRel("users");

        userResponseModel.add(allUsersLink);
        // Return userResponseModel with additional links
        return userResponseModel;
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
    public PagedModel<UserResponseModel> findAll(Pageable pageable) {
        log.info("Fetching all users with pagination - Page: {}, Size: {}, Sort: {}",
                pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());

        Page<User> users = userRepository.findAll(pageable);

        log.info("Fetched {} users on current page out of {} total users.",
                users.getNumberOfElements(), users.getTotalElements());
        pagedResourcesAssembler.setForceFirstAndLastRels(true);
        return pagedResourcesAssembler.toModel(users, userResponseModelAssembler);
    }


    @Transactional
    @Override
    public User register(AuthRequest authRequest) {
        log.info("Attempting to register a new user with username: {}", authRequest.username());

        if (userRepository.existsByUsername(authRequest.username())) {
            throw new EntityExistsException("Username: '"  + authRequest.username() + "' already exists!");
        }

        User newUserRequest = AuthMapper.fromAuthRequestToUser(authRequest);

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

    @Override
    @Transactional
    public UserResponseModel getUserResponseModelById(UUID id) {
        User user = findById(id);
        return  userResponseModelAssembler.toModel(user);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
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
    public UserResponseModel updateUser(UUID id, UserRequest userRequest) {
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

        return userResponseModelAssembler.toModel(updatedUser);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    void handleProviderCreatedEvent(ProviderCreatedEvent providerCreatedEvent) {
        Role providerRole = roleService.ensureRoleExists(ERole.PROVIDER);
        log.info("Provider role {} created.", providerRole.getName());
        User user = findById(providerCreatedEvent.user().getId());
        if (user == null) {
            log.error("User in ProviderCreatedEvent is null.");
            throw new IllegalStateException("User in ProviderCreatedEvent is null.");
        }
        user.addRole(providerRole);
        log.info("Set role for user ID: {} to '{}'", user.getId(), providerRole.getName());

        Set<Permission> copiedPermissions = new HashSet<>(providerRole.getPermissions());
        user.addPermissions(copiedPermissions);
        log.info("Set permissions for user ID: {} to '{}'", user.getId(), copiedPermissions);

        User savedUser = saveUser(user);

        Set<String> userRoles = user.getRoles().stream().map(role -> role.getName().name())
                .collect(Collectors.toSet());
        log.info("User with ID: {} successfully updated with new role {}.", savedUser.getId(), userRoles);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public User saveUser(User user) {
        return userRepository.saveAndFlush(user);
    }
}
