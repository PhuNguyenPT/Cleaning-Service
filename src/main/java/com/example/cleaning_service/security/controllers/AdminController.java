package com.example.cleaning_service.security.controllers;

import com.example.cleaning_service.security.dtos.user.UserRequest;
import com.example.cleaning_service.security.dtos.user.UserResponseModel;
import com.example.cleaning_service.security.services.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/admin")
@Tag(name = "Admin Users", description = "Users management APIs")
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final IUserService userService;

    public AdminController(IUserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Create a new user (admin)", description = "Creates a new user with the provided details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    @PreAuthorize("hasRole('ADMIN') and hasAuthority('MANAGE_USERS')")
    @PostMapping(path = "/users", produces = { "application/hal+json" })
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseModel createUser(@RequestBody @Valid UserRequest userRequest) {
        return userService.createUser(userRequest);
    }

    @Operation(summary = "Get all users (admin)", description = "Fetches a paginated list of all users.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of users retrieved successfully")
    })
    @PreAuthorize("hasRole('ADMIN') and hasAuthority('MANAGE_USERS')")
    @GetMapping(path = "/users", produces = { "application/hal+json" })
    @ResponseStatus(HttpStatus.OK)
    public PagedModel<UserResponseModel> getAllUsers(@ParameterObject Pageable pageable) {
        return userService.findAll(pageable);
    }

    @Operation(summary = "Get user by ID (admin)", description = "Fetches details of a specific user by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PreAuthorize("hasRole('ADMIN') and hasAuthority('MANAGE_USERS')")
    @GetMapping(path = "/users/{id}", produces = { "application/hal+json" })
    @ResponseStatus(HttpStatus.OK)
    public UserResponseModel getUserById(@PathVariable UUID id) {
        return userService.getUserResponseModelById(id);
    }

    @Operation(summary = "Delete user by ID (admin)", description = "Deletes a user by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PreAuthorize("hasRole('ADMIN') and hasAuthority('MANAGE_USERS')")
    @DeleteMapping(path = "/users/{id}", produces = { "application/hal+json" })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> deleteUserById(@PathVariable UUID id) {
        userService.deleteUser(id);

        Link allUsersLink = linkTo(methodOn(AdminController.class).getAllUsers(PageRequest.of(0, 20)))
                .withRel("allUsers");

        return ResponseEntity.noContent()
                .header("Link", allUsersLink.toUri().toString())
                .build();
    }

    @Operation(summary = "Update user by ID (admin)", description = "Updates user information by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PreAuthorize("hasRole('ADMIN') and hasAuthority('MANAGE_USERS')")
    @PutMapping(path = "/users/{id}", produces = "application/hal+json")
    public UserResponseModel updateUserById(@PathVariable UUID id, @RequestBody @Valid UserRequest userRequest) {
        return userService.updateUser(id, userRequest);
    }
}
