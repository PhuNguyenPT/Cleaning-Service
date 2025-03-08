package com.example.cleaning_service.security.dtos.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AuthRequest(
        @NotBlank
        @Size(min = 8, max = 32, message = "Username must be between 8 and 32 characters long")
        @Pattern(
                regexp = "^(?![_.])[a-zA-Z0-9._]{8,32}(?<![_.])$",
                message = "Username must be 8-32 characters, contain letters, numbers, dots, or underscores, but cannot start or end with a dot/underscore"
        )
        @Schema(
                description = "Username must be 8-32 characters long, contain letters, numbers, dots, or underscores, but cannot start or end with a dot/underscore",
                minLength = 8,
                maxLength = 32,
                example = "user_1234"
        )
        String username,

        @NotBlank
        @Size(min = 12, max = 64, message = "Password must be between 12 and 64 characters long")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{12,64}$",
                message = "Password must be 12-64 characters, include upper & lower case letters, a number, and a special character"
        )
        @Schema(
                description = "Password must be 12-64 characters long, include upper & lower case letters, a number, and a special character",
                minLength = 12,
                maxLength = 64,
                example = "StrongP@ssw0rd123"
        )
        String password
) {}
