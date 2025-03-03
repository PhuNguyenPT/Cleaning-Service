package com.example.cleaning_service.security.users;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasRole('ADMIN')")  // Default rule for all methods
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasRole('ADMIN') and hasAuthority('MANAGE_USERS')")
    @PostMapping(produces = { "application/hal+json" })
    @ResponseStatus(HttpStatus.CREATED)
    public EntityModel<UserResponse> createUser(@RequestBody UserRequest userRequest) {
        UserResponse userResponse = userService.saveUser(
                userRequest.username(),
                userRequest.password(),
                userRequest.role()
        );

        // Adding HATEOAS self link
        userResponse.add(WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(UserController.class).getUserById(userResponse.getId()))
                .withSelfRel()
        );

        // Adding a link to get all users
        userResponse.add(WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(UserController.class).getAllUsers())
                .withRel("users")
        );
        return EntityModel.of(userResponse);
    }


    @PreAuthorize("hasRole('ADMIN') and hasAuthority('MANAGE_USERS')")
    @GetMapping(produces = { "application/hal+json" })
    @ResponseStatus(HttpStatus.OK)
    public CollectionModel<UserResponse> getAllUsers() {
        List<UserResponse> users = userService.findAll();

        for (UserResponse userResponse : users) {
            Link selfLink = WebMvcLinkBuilder.linkTo(
                    WebMvcLinkBuilder.methodOn(UserController.class).getUserById(userResponse.getId())
            ).withSelfRel();
            userResponse.add(selfLink);
        }

        Link link = WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(UserController.class).getAllUsers()
        ).withSelfRel();

        return CollectionModel.of(users, link);
    }

    @PreAuthorize("hasRole('ADMIN') and hasAuthority('MANAGE_USERS')")
    @GetMapping(value = "/{id}", produces = { "application/hal+json" })
    @ResponseStatus(HttpStatus.OK)
    public EntityModel<UserResponse> getUserById(@PathVariable Long id) {
        UserResponse userResponse = userService.findById(id);


        // Adding HATEOAS self link
        userResponse.add(WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(UserController.class).getUserById(id))
                .withSelfRel()
        );

        // Adding a link to get all users
        userResponse.add(WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(UserController.class).getAllUsers())
                .withRel("users")
        );

        return EntityModel.of(userResponse);
    }

    @PreAuthorize("hasRole('ADMIN') and hasAuthority('MANAGE_USERS')")
    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable Long id) {
        userService.deleteUser(id);

        Link userListLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                        .getAllUsers())
                .withRel("users");

        return ResponseEntity.noContent()
                .header("Link", userListLink.toString()) // Adds the link in the response header
                .build();
    }

    @PreAuthorize("hasRole('ADMIN') and hasAuthority('MANAGE_USERS')")
    @PutMapping("{id}")
    public EntityModel<UserResponse> updateUserById(@PathVariable Long id, @RequestBody UserRequest userRequest) {
        UserResponse userResponse = userService.updateUser(id, userRequest);
        return EntityModel.of(userResponse);
    }
}
