package com.example.cleaning_service.security.auth;

import com.example.cleaning_service.security.users.User;
import com.example.cleaning_service.security.users.UserResponse;
import com.example.cleaning_service.security.users.UserService;
import com.example.cleaning_service.security.util.JwtService;
import com.example.cleaning_service.security.util.JwtUtil;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.username(), authRequest.password())
            );
            User user = (User) authentication.getPrincipal();
            long expDuration = 86400000;
            String token = jwtUtil.generateToken(user, expDuration);
            jwtService.saveToken(token);
            return ResponseEntity.ok(new AuthResponse(token));
        } catch (Exception e) {
            logger.error("Login failed: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid credentials: " + e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<EntityModel<UserResponse>> register(@RequestBody @Valid AuthRequest authRequest) {
        UserResponse userResponse = userService.register(authRequest);

        // Create HAL+JSON response with links
        EntityModel<UserResponse> responseModel = EntityModel.of(userResponse,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.class).slash("login").withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.class).slash("register").withSelfRel()
        );

        return ResponseEntity.created(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(AuthController.class).login(null)).toUri())
                .body(responseModel);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String token) {
        if (token == null || token.isBlank()) {
            return ResponseEntity.badRequest().body("Missing Authorization header");
        }

        if (!token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token format");
        }

        token = token.substring(7); // Remove "Bearer " prefix

        jwtService.logoutToken(token);
        return ResponseEntity.ok("Logged out successfully");
    }
//
//    @GetMapping("/refresh")
//    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
//        String token = jwtUtil.extractToken(request);
//        String newToken = jwtUtil.refreshToken(token);
//        return ResponseEntity.ok(new AuthResponse(newToken));
//    }
}
