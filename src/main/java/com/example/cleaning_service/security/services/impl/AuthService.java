package com.example.cleaning_service.security.services.impl;

import com.example.cleaning_service.security.assemblers.AuthResponseModelAssembler;
import com.example.cleaning_service.security.controllers.AuthController;
import com.example.cleaning_service.security.dtos.auth.AuthRequest;
import com.example.cleaning_service.security.dtos.auth.AuthResponse;
import com.example.cleaning_service.security.dtos.auth.AuthResponseLoginModel;
import com.example.cleaning_service.security.dtos.auth.AuthResponseLogoutModel;
import com.example.cleaning_service.security.entities.user.User;
import com.example.cleaning_service.security.services.IAuthService;
import com.example.cleaning_service.security.util.JwtUtil;
import jakarta.transaction.Transactional;
import org.springframework.hateoas.Link;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Service
class AuthService implements IAuthService {


    private final JwtService jwtService;
    private final JwtUtil jwtUtil;
    private final AuthResponseModelAssembler authResponseModelAssembler;
    private final AuthenticationManager authenticationManager;

    public AuthService(JwtService jwtService, JwtUtil jwtUtil, AuthResponseModelAssembler authResponseModelAssembler, AuthenticationManager authenticationManager) {
        this.jwtService = jwtService;
        this.jwtUtil = jwtUtil;
        this.authResponseModelAssembler = authResponseModelAssembler;
        this.authenticationManager = authenticationManager;
    }

    @Override
    @Transactional
    public AuthResponseLoginModel login(AuthRequest authRequest) {
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

    @Override
    @Transactional
    public AuthResponseLogoutModel logout(String token) {
        token = token.substring(7); // Remove "Bearer " prefix
        jwtService.logoutToken(token);

        AuthResponseLogoutModel authResponseLogoutModel = new AuthResponseLogoutModel("Logout successful");

        Link loginLink = linkTo(AuthController.class).slash("login").withRel("login");
        authResponseLogoutModel.add(loginLink);

        return authResponseLogoutModel;
    }
}
