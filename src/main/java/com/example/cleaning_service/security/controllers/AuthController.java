package com.example.cleaning_service.security.controllers;

import com.example.cleaning_service.security.assemblers.AuthResponseRegisterModelAssembler;
import com.example.cleaning_service.security.assemblers.TokenLoginModelAssembler;
import com.example.cleaning_service.security.assemblers.TokenModelAssembler;
import com.example.cleaning_service.security.dtos.auth.*;
import com.example.cleaning_service.security.entities.token.TokenEntity;
import com.example.cleaning_service.security.entities.user.User;
import com.example.cleaning_service.security.services.IAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RestController
@RequestMapping("/auth")
@Tag(name = "Users Authentication", description = "Authentication management APIs")
public class AuthController {
    private final IAuthService authService;
    private final AuthResponseRegisterModelAssembler authResponseRegisterModelAssembler;
    private final TokenModelAssembler tokenModelAssembler;
    private final TokenLoginModelAssembler tokenLoginModelAssembler;

    public AuthController(IAuthService authService, AuthResponseRegisterModelAssembler authResponseRegisterModelAssembler,
                          TokenModelAssembler tokenModelAssembler, TokenLoginModelAssembler tokenLoginModelAssembler) {
        this.authService = authService;
        this.authResponseRegisterModelAssembler = authResponseRegisterModelAssembler;
        this.tokenModelAssembler = tokenModelAssembler;
        this.tokenLoginModelAssembler = tokenLoginModelAssembler;
    }

    @Operation(summary = "User login", description = "Authenticates a user and returns a JWT token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "400", description = "Invalid login credentials")
    })
    @PostMapping(value = "/login", produces = { "application/hal+json" })
    @ResponseStatus(HttpStatus.OK)
    public TokenModel login(@RequestBody @Valid AuthRequest authRequest) {
        TokenEntity tokenEntity = authService.login(authRequest);
        TokenModel tokenModel = tokenLoginModelAssembler.toModel(tokenEntity);
        log.info("Assembling token model {}...", tokenModel.getAccessToken().substring(0, 10));
        return tokenModel;
    }

    @Operation(summary = "Get authenticated user", description = "Fetches the profile details of the currently authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authenticated user details retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized, invalid token")
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping(value = "/me", produces = { "application/hal+json" })
    @ResponseStatus(HttpStatus.OK)
    public AuthResponseProfileModel getAuthenticatedUser(@AuthenticationPrincipal User user) {
        return authService.getAuthenticatedUser(user);
    }

    @Operation(summary = "User registration", description = "Registers a new user and returns user details with a token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid registration details")
    })
    @PostMapping(path = "/register", produces = { "application/hal+json" })
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponseRegisterModel register(@RequestBody @Valid AuthRequest authRequest) {
        User user = authService.register(authRequest);
        AuthResponseRegisterModel authResponseRegisterModel = authResponseRegisterModelAssembler.toModel(user);
        log.info("Assembling model {} for registered user {}", authResponseRegisterModel.getId(), user.getId());

        return authResponseRegisterModel;
    }

    @Operation(summary = "User logout", description = "Logs out the authenticated user by invalidating the JWT token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Logout successful"),
            @ApiResponse(responseCode = "401", description = "Unauthorized, invalid token")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping(path = "/logout", produces = { "application/hal+json" })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        authService.logout(token);

        Link loginLink = linkTo(methodOn(AuthController.class).login(null)).withRel("login");
        return ResponseEntity.noContent()
                .header("Link", loginLink.toUri().toString())
                .build();
    }

    @Operation(summary = "Refresh access token", description = "Generates a new access token using a valid refresh token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "New token generated successfully"),
            @ApiResponse(responseCode = "403", description = "Invalid or expired refresh token")
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping(path = "/refresh", produces = { "application/hal+json" })
    @ResponseStatus(HttpStatus.OK)
    public TokenModel refreshToken(HttpServletRequest request, @AuthenticationPrincipal User user) {
        String token = request.getHeader("Authorization");
        TokenEntity tokenEntity = authService.refreshToken(token, user);
        log.info("Assembling model {} for refreshing token of user {}",
                tokenEntity.getToken().substring(0, 10), user.getId());
        return tokenLoginModelAssembler.toModel(tokenEntity);
    }

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping(path = "/token/public/{id}", produces = { "application/hal+json" })
    @ResponseStatus(HttpStatus.OK)
    public TokenModel getTokenById(@PathVariable UUID id, @AuthenticationPrincipal User user) {
        TokenEntity tokenEntity = authService.getTokenById(id, user);
        return tokenModelAssembler.toModel(tokenEntity);
    }
}
