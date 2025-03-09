package com.example.cleaning_service.security.dtos.user;

import java.util.Set;

public record UserResponse(Long id, String username, RoleResponse role, Set<PermissionResponse> permissions) {
}