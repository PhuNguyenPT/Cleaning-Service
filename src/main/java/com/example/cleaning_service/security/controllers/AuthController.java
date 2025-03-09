package com.example.cleaning_service.security.controllers;

import com.example.cleaning_service.security.assemblers.AuthResponseModelAssembler;
import com.example.cleaning_service.security.assemblers.AuthResponseProfileModelAssembler;
import com.example.cleaning_service.security.assemblers.AuthResponseRegisterModelAssembler;
import com.example.cleaning_service.security.dtos.auth.*;
import com.example.cleaning_service.security.dtos.auth.AuthResponseRegister;
import com.example.cleaning_service.security.entities.user.User;
import com.example.cleaning_service.security.mapper.AuthMapper;
import com.example.cleaning_service.security.services.UserService;
import com.example.cleaning_service.security.services.JwtService;
import com.example.cleaning_service.security.util.JwtUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final AuthResponseModelAssembler authResponseModelAssembler;
    private final AuthResponseProfileModelAssembler authResponseProfileModelAssembler;
    private final AuthResponseRegisterModelAssembler authResponseRegisterModelAssembler;

    public AuthController(JwtUtil jwtUtil, UserService userService, AuthenticationManager authenticationManager,
                          JwtService jwtService, AuthResponseModelAssembler authResponseModelAssembler,
                          AuthResponseProfileModelAssembler authResponseProfileModelAssembler, AuthResponseRegisterModelAssembler authResponseRegisterModelAssembler) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.authResponseModelAssembler = authResponseModelAssembler;
        this.authResponseProfileModelAssembler = authResponseProfileModelAssembler;
        this.authResponseRegisterModelAssembler = authResponseRegisterModelAssembler;
    }

    @PostMapping(value = "/login", produces = { "application/hal+json" })
    @ResponseStatus(HttpStatus.OK)
    public AuthResponseModel login(@RequestBody @Valid AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.username(), authRequest.password())
        );

        User user = (User) authentication.getPrincipal();
        long expDurationMs = 86400000; // 24 hours in milliseconds
        long expDurationSec = expDurationMs / 1000; // Convert to seconds

        String token = jwtUtil.generateToken(user, expDurationMs);
        jwtService.saveToken(token);

        AuthResponse authResponse = new AuthResponse(user.getId(), token, expDurationSec);
        return authResponseModelAssembler.toModel(authResponse);
    }

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping(value = "/me", produces = { "application/hal+json" })
    @ResponseStatus(HttpStatus.OK)
    public AuthResponseProfileModel getAuthenticatedUser(@AuthenticationPrincipal User user) {
        AuthResponseProfile authResponseProfile = AuthMapper.fromUserToAuthResponseProfile(user);
        return authResponseProfileModelAssembler.toModel(authResponseProfile);
    }

    @PostMapping(path = "/register", produces = { "application/hal+json" })
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponseRegisterModel register(@RequestBody @Valid AuthRequest authRequest) {
        AuthResponseRegister authResponseRegister = userService.register(authRequest);
        return authResponseRegisterModelAssembler.toModel(authResponseRegister);
    }

    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String token = request.getHeader("Authorization");

        token = token.substring(7); // Remove "Bearer " prefix
        jwtService.logoutToken(token);

        return ResponseEntity.ok("Logged out successfully");
    }

    // Uncomment if refresh token is needed
    /*
    @GetMapping("/refresh")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
        String token = jwtUtil.extractToken(request);
        String newToken = jwtUtil.refreshToken(token);
        return ResponseEntity.ok(new AuthResponse(newToken));
    }
    */
}