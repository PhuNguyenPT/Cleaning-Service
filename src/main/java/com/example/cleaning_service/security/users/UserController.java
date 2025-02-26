package com.example.cleaning_service.security.users;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasRole('ADMIN')")
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


    @PreAuthorize("hasRole('ADMIN')")
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

    @PreAuthorize("hasRole('ADMIN')")
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
}
