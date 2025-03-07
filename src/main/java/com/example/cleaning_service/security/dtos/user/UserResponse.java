package com.example.cleaning_service.security.dtos.user;

import com.example.cleaning_service.security.entities.role.ERole;

import java.util.Set;

public record UserResponse(Long id, String username, ERole role, Set<PermissionResponse> permissions) {
}