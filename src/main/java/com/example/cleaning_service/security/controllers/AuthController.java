package com.example.cleaning_service.security.controllers;

import com.example.cleaning_service.security.dtos.auth.AuthRequest;
import com.example.cleaning_service.security.dtos.auth.AuthResponse;
import com.example.cleaning_service.security.dtos.user.UserResponseLogin;
import com.example.cleaning_service.security.entities.user.User;
import com.example.cleaning_service.security.services.UserService;
import com.example.cleaning_service.security.services.JwtService;
import com.example.cleaning_service.security.util.JwtUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.persistence.EntityExistsException;
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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> login(@RequestBody @Valid AuthRequest authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.username(), authRequest.password())
            );
            User user = (User) authentication.getPrincipal();
            long expDuration = 86400000;
            String token = jwtUtil.generateToken(user, expDuration);
            jwtService.saveToken(token);
            return ResponseEntity.ok(new AuthResponse(token));
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid username or password");
        } catch (UsernameNotFoundException e) {
            throw new UsernameNotFoundException("User not found");
        } catch (DisabledException e) {
            throw new DisabledException("User account is disabled");
        } catch (LockedException e) {
            throw new LockedException("User account is locked");
        } catch (Exception e) {
            throw new RuntimeException("Authentication failed");
        }
    }

    @PostMapping(path = "/register", produces = { "application/hal+json" })
    public ResponseEntity<?> register(@RequestBody @Valid AuthRequest authRequest) {
        try {
            UserResponseLogin userResponse = userService.register(authRequest);

            AuthRequest dummy = new AuthRequest("dummy", "dummy");

            Link loginLink = linkTo(methodOn(AuthController.class).login(dummy)).withRel("login");
            Link selfLink = linkTo(methodOn(AuthController.class).register(dummy)).withSelfRel();

            EntityModel<UserResponseLogin> response = EntityModel.of(userResponse, loginLink, selfLink);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (EntityExistsException e) {
            throw new EntityExistsException("User already exists");
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid registration details");
        } catch (Exception e) {
            logger.error("Registration failed: {}", e.getMessage(), e);
            throw new RuntimeException("Registration failed");
        }
    }

    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");

            token = token.substring(7); // Remove "Bearer " prefix

            jwtService.logoutToken(token);
            return ResponseEntity.ok("Logged out successfully");
        } catch (Exception e) {
            logger.error("Logout failed: {}", e.getMessage(), e);
            throw new RuntimeException("Logout failed: " + e.getMessage());
        }
    }

    // Uncomment if refresh token is needed
    /*
    @GetMapping("/refresh")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
        try {
            String token = jwtUtil.extractToken(request);
            String newToken = jwtUtil.refreshToken(token);
            return ResponseEntity.ok(new AuthResponse(newToken));
        } catch (Exception e) {
            logger.error("Token refresh failed: {}", e.getMessage(), e);

            throw new RuntimeException("Token refresh failed: " + e.getMessage());

        }
    }
    */
}