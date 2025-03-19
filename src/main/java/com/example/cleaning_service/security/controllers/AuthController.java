package com.example.cleaning_service.security.controllers;

import com.example.cleaning_service.security.assemblers.AuthResponseProfileModelAssembler;
import com.example.cleaning_service.security.assemblers.AuthResponseRegisterModelAssembler;
import com.example.cleaning_service.security.dtos.auth.*;
import com.example.cleaning_service.security.entities.user.User;
import com.example.cleaning_service.security.services.IAuthService;
import com.example.cleaning_service.security.services.IUserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    private final IAuthService authService;
    private final IUserService userService;
    private final AuthResponseProfileModelAssembler authResponseProfileModelAssembler;
    private final AuthResponseRegisterModelAssembler authResponseRegisterModelAssembler;

    public AuthController(IAuthService authService, IUserService userService,
                          AuthResponseProfileModelAssembler authResponseProfileModelAssembler, AuthResponseRegisterModelAssembler authResponseRegisterModelAssembler) {
        this.authService = authService;
        this.userService = userService;
        this.authResponseProfileModelAssembler = authResponseProfileModelAssembler;
        this.authResponseRegisterModelAssembler = authResponseRegisterModelAssembler;
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
        log.info("Assembling model for authenticated user {}", user);
        return authResponseProfileModelAssembler.toModel(user);
    }

    @PostMapping(path = "/register", produces = { "application/hal+json" })
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponseRegisterModel register(@RequestBody @Valid AuthRequest authRequest) {
        User user = userService.register(authRequest);
        log.info("Assembling model for registered user {}", user);
        return authResponseRegisterModelAssembler.toModel(user);
    }

    @SecurityRequirement(name = "bearerAuth")
    @PostMapping(path = "/logout", produces = { "application/hal+json" })
    @ResponseStatus(HttpStatus.OK)
    public AuthResponseLogoutModel logout(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        return authService.logout(token);
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