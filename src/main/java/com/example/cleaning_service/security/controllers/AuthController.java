package com.example.cleaning_service.security.controllers;

import com.example.cleaning_service.security.dtos.auth.*;
import com.example.cleaning_service.security.entities.user.User;
import com.example.cleaning_service.security.services.IAuthService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/auth")
@Tag(name = "Users Authentication", description = "Authentication management APIs")
public class AuthController {
    private final IAuthService authService;

    public AuthController(IAuthService authService) {
        this.authService = authService;
    }

    @PostMapping(value = "/login", produces = { "application/hal+json" })
    @ResponseStatus(HttpStatus.OK)
    public AuthResponseLoginModel login(@RequestBody @Valid AuthRequest authRequest) {
        return authService.login(authRequest);
    }

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping(value = "/me", produces = { "application/hal+json" })
    @ResponseStatus(HttpStatus.OK)
    public AuthResponseProfileModel getAuthenticatedUser(@AuthenticationPrincipal User user) {
        return authService.getAuthenticatedUser(user);
    }

    @PostMapping(path = "/register", produces = { "application/hal+json" })
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponseRegisterModel register(@RequestBody @Valid AuthRequest authRequest) {
        return authService.register(authRequest);
    }

    @SecurityRequirement(name = "bearerAuth")
    @PostMapping(path = "/logout", produces = { "application/hal+json" })
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        authService.logout(token);

        Link loginLink = linkTo(methodOn(AuthController.class).login(null)).withRel("login");
        return ResponseEntity.noContent()
                .header("Link", loginLink.toUri().toString())
                .build();
    }

    // Uncomment if refresh token is needed
    /*
    @GetMapping("/refresh", produces = { "application/hal+json" })
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
        String token = jwtUtil.extractToken(request);
        String newToken = jwtUtil.refreshToken(token);
        return ResponseEntity.ok(new AuthResponse(newToken));
    }
    */
}