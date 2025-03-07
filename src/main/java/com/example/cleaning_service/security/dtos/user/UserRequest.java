package com.example.cleaning_service.security.dtos.user;

import com.example.cleaning_service.security.entities.role.ERole;
import jakarta.validation.constraints.NotBlank;

public record UserRequest(@NotBlank String username,
                          @NotBlank String password,
                          ERole role) {
}
