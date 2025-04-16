package com.example.cleaning_service.security.services.impl;

import com.example.cleaning_service.security.assemblers.AuthResponseProfileModelAssembler;
import com.example.cleaning_service.security.controllers.AuthController;
import com.example.cleaning_service.security.dtos.auth.*;
import com.example.cleaning_service.security.entities.token.TokenEntity;
import com.example.cleaning_service.security.entities.user.User;
import com.example.cleaning_service.security.services.IAuthService;
import com.example.cleaning_service.security.services.IJwtService;
import com.example.cleaning_service.security.services.IUserService;
import com.example.cleaning_service.security.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.Link;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
class AuthService implements IAuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);
    private final IJwtService jwtService;
    private final IUserService userService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final AuthResponseProfileModelAssembler authResponseProfileModelAssembler;
    private final HttpServletRequest httpServletRequest;


    public AuthService(IJwtService jwtService, IUserService userService, JwtUtil jwtUtil,
                       AuthenticationManager authenticationManager,
                       AuthResponseProfileModelAssembler authResponseProfileModelAssembler, HttpServletRequest httpServletRequest) {
        this.jwtService = jwtService;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.authResponseProfileModelAssembler = authResponseProfileModelAssembler;
        this.httpServletRequest = httpServletRequest;
    }

    @Override
    @Transactional
    public User register(AuthRequest authRequest) {
        User user = userService.register(authRequest);
        log.info("Assembling model for registered user {}", user.getId());
        return user;
    }

    @Override
    @Transactional
    public TokenEntity login(AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.username(), authRequest.password())
        );

        User user = (User) authentication.getPrincipal();
        return saveAndGetTokenEntity(user);
    }

    @Override
    @Transactional
    public void logout(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // Remove "Bearer " prefix
            jwtService.logoutToken(token);
        } else {
            log.warn("Invalid authorization header format for logout");
            throw new BadCredentialsException("Invalid token format");
        }
    }

    @Override
    @Transactional
    public AuthResponseProfileModel getAuthenticatedUser(User user) {
        log.info("Assembling model for authenticated user {}", user.getId());
        AuthResponseProfileModel authResponseProfileModel = authResponseProfileModelAssembler.toModel(user);
        // Add HATEOAS self-link
        Link selfLink =  linkTo(methodOn(AuthController.class).getAuthenticatedUser(user)).withSelfRel();
        Link logoutLink = linkTo(methodOn(AuthController.class).logout(httpServletRequest)).withRel("logout");
        authResponseProfileModel.add(selfLink, logoutLink);
        return authResponseProfileModel;
    }

    @Override
    @Transactional
    public TokenEntity refreshToken(String token, User user) {
        log.info("Attempting to refresh token for user {}", user.getId());
        logout(token);
        return saveAndGetTokenEntity(user);
    }

    @Transactional
    TokenEntity saveAndGetTokenEntity(User user) {
        String newToken = jwtUtil.generateToken(user, null);
        TokenEntity savedTokenEntity = jwtService.saveToken(newToken);
        log.info("Saved token: {}...", savedTokenEntity.getToken().substring(0, 10));
        return savedTokenEntity;
    }

    @Transactional
    @Override
    public TokenEntity getTokenById(UUID id, User user) {
        TokenEntity tokenEntity = jwtService.findById(id);
        if (!tokenEntity.getUsername().equals(user.getUsername())) {
            throw new AccessDeniedException("Username " + user.getUsername() + " does not match token's username");
        }
        return tokenEntity;
    }
}
