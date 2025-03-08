package com.example.cleaning_service.security.controllers;

import com.example.cleaning_service.security.dtos.auth.AuthRequest;
import com.example.cleaning_service.security.dtos.auth.AuthResponse;
import com.example.cleaning_service.security.dtos.user.UserResponseLogin;
import com.example.cleaning_service.security.entities.user.User;
import com.example.cleaning_service.security.services.UserService;
import com.example.cleaning_service.security.services.JwtService;
import com.example.cleaning_service.security.util.JwtUtil;
import com.nimbusds.jose.JOSEException;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthController(JwtUtil jwtUtil, UserService userService, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthRequest authRequest) throws JOSEException, ParseException {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.username(), authRequest.password())
        );
        User user = (User) authentication.getPrincipal();
        long expDuration = 86400000;
        String token = jwtUtil.generateToken(user, expDuration);
        jwtService.saveToken(token);
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping(path = "/register", produces = { "application/hal+json" })
    public ResponseEntity<?> register(@RequestBody @Valid AuthRequest authRequest) throws ParseException, JOSEException {
        UserResponseLogin userResponse = userService.register(authRequest);

        AuthRequest dummy = new AuthRequest("dummy", "dummy");

        Link loginLink = linkTo(methodOn(AuthController.class).login(dummy)).withRel("login");
        Link selfLink = linkTo(methodOn(AuthController.class).register(dummy)).withSelfRel();

        EntityModel<UserResponseLogin> response = EntityModel.of(userResponse, loginLink, selfLink);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) throws ParseException {
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