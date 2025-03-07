package com.example.cleaning_service.security.controllers;

import com.example.cleaning_service.security.assemblers.UserResponseModelAssembler;
import com.example.cleaning_service.security.dtos.user.UserRequest;
import com.example.cleaning_service.security.dtos.user.UserResponse;
import com.example.cleaning_service.security.dtos.user.UserResponseModel;
import com.example.cleaning_service.security.services.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/users")
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasRole('ADMIN')")  // Default rule for all methods
public class UserController {
    private final UserService userService;
    private final PagedResourcesAssembler<UserResponse> pagedResourcesAssembler;
    private final UserResponseModelAssembler userResponseModelAssembler;

    public UserController(UserService userService, PagedResourcesAssembler<UserResponse> pagedResourcesAssembler, UserResponseModelAssembler userResponseModelAssembler) {
        this.userService = userService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.userResponseModelAssembler = userResponseModelAssembler;
    }

    @PreAuthorize("hasRole('ADMIN') and hasAuthority('MANAGE_USERS')")
    @PostMapping(produces = { "application/hal+json" })
    @ResponseStatus(HttpStatus.CREATED)
    public EntityModel<UserResponseModel> createUser(@RequestBody @Valid UserRequest userRequest) {
        UserResponse userResponse = userService.saveUser(
                userRequest.username(),
                userRequest.password(),
                userRequest.role()
        );

        // Convert to UserResponseModel using assembler
        UserResponseModel userResponseModel = userResponseModelAssembler.toModel(userResponse);

        // Add an additional link for all users
        PageRequest pageRequest = PageRequest.of(0, 20);
        Link allUsersLink = linkTo(methodOn(UserController.class).getAllUsers(pageRequest)).withRel("users");

        // Return EntityModel with additional links
        return EntityModel.of(userResponseModel, userResponseModel.getLinks()).add(allUsersLink);
    }

    @PreAuthorize("hasRole('ADMIN') and hasAuthority('MANAGE_USERS')")
    @GetMapping(produces = { "application/hal+json" })
    @ResponseStatus(HttpStatus.OK)
    public PagedModel<UserResponseModel> getAllUsers(@ParameterObject Pageable pageable) {
        Page<UserResponse> userPage = userService.findAll(pageable);
        return pagedResourcesAssembler.toModel(userPage, userResponseModelAssembler);
    }

    @PreAuthorize("hasRole('ADMIN') and hasAuthority('MANAGE_USERS')")
    @GetMapping(value = "/{id}", produces = { "application/hal+json" })
    @ResponseStatus(HttpStatus.OK)
    public EntityModel<UserResponseModel> getUserById(@PathVariable Long id) {
        UserResponse userResponse = userService.findById(id);

        // Convert to UserResponseModel using assembler
        UserResponseModel userResponseModel = userResponseModelAssembler.toModel(userResponse);

        return EntityModel.of(userResponseModel, userResponseModel.getLinks());
    }

    @PreAuthorize("hasRole('ADMIN') and hasAuthority('MANAGE_USERS')")
    @DeleteMapping(value = "/{id}", produces = { "application/hal+json" })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> deleteUserById(@PathVariable Long id) {
        userService.deleteUser(id);

        Link allUsersLink = linkTo(methodOn(UserController.class).getAllUsers(PageRequest.of(0, 20)))
                .withRel("allUsers");

        return ResponseEntity.noContent()
                .header("Link", allUsersLink.toUri().toString()) // Optional: Add HAL link in headers
                .build();
    }


    @PreAuthorize("hasRole('ADMIN') and hasAuthority('MANAGE_USERS')")
    @PutMapping("{id}")
    public EntityModel<UserResponseModel> updateUserById(@PathVariable Long id, @RequestBody @Valid UserRequest userRequest) {
        UserResponse userResponse = userService.updateUser(id, userRequest);

        // Convert to UserResponseModel using assembler
        UserResponseModel userResponseModel = userResponseModelAssembler.toModel(userResponse);

        return EntityModel.of(userResponseModel, userResponseModel.getLinks());
    }
}
