package com.example.cleaning_service.security.services.impl;

import com.example.cleaning_service.security.assemblers.AuthResponseModelAssembler;
import com.example.cleaning_service.security.assemblers.AuthResponseProfileModelAssembler;
import com.example.cleaning_service.security.assemblers.AuthResponseRegisterModelAssembler;
import com.example.cleaning_service.security.controllers.AuthController;
import com.example.cleaning_service.security.dtos.auth.*;
import com.example.cleaning_service.security.entities.user.User;
import com.example.cleaning_service.security.services.IAuthService;
import com.example.cleaning_service.security.services.IJwtService;
import com.example.cleaning_service.security.services.IUserService;
import com.example.cleaning_service.security.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.Link;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
class AuthService implements IAuthService {


    private static final Logger log = LoggerFactory.getLogger(AuthService.class);
    private final IJwtService jwtService;
    private final IUserService userService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final AuthResponseModelAssembler authResponseModelAssembler;
    private final AuthResponseRegisterModelAssembler authResponseRegisterModelAssembler;
    private final AuthResponseProfileModelAssembler authResponseProfileModelAssembler;
    private final HttpServletRequest httpServletRequest;


    public AuthService(IJwtService jwtService, IUserService userService, JwtUtil jwtUtil,
                       AuthenticationManager authenticationManager, AuthResponseModelAssembler authResponseModelAssembler,
                       AuthResponseRegisterModelAssembler authResponseRegisterModelAssembler,
                       AuthResponseProfileModelAssembler authResponseProfileModelAssembler, HttpServletRequest httpServletRequest) {
        this.jwtService = jwtService;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.authResponseModelAssembler = authResponseModelAssembler;
        this.authResponseRegisterModelAssembler = authResponseRegisterModelAssembler;
        this.authResponseProfileModelAssembler = authResponseProfileModelAssembler;
        this.httpServletRequest = httpServletRequest;
    }

    @Override
    @Transactional
    public AuthResponseRegisterModel register(AuthRequest authRequest) {
        User user = userService.register(authRequest);
        log.info("Assembling model for registered user {}", user);
        return authResponseRegisterModelAssembler.toModel(user);
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
    public void logout(String token) {
        token = token.substring(7); // Remove "Bearer " prefix
        jwtService.logoutToken(token);
    }

    @Override
    @Transactional
    public AuthResponseProfileModel getAuthenticatedUser(User user) {
        log.info("Assembling model for authenticated user {}", user);
        AuthResponseProfileModel authResponseProfileModel = authResponseProfileModelAssembler.toModel(user);
        // Add HATEOAS self-link
        Link selfLink =  linkTo(methodOn(AuthController.class).getAuthenticatedUser(user)).withSelfRel();
        Link logoutLink = linkTo(methodOn(AuthController.class).logout(httpServletRequest)).withRel("logout");
        authResponseProfileModel.add(selfLink, logoutLink);
        return authResponseProfileModel;
    }
}
