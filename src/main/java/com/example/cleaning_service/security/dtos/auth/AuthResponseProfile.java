package com.example.cleaning_service.security.dtos.auth;

import com.example.cleaning_service.security.dtos.user.PermissionResponse;
import com.example.cleaning_service.security.dtos.user.RoleResponse;

import java.util.Set;

public record AuthResponseProfile(Long id, String username, RoleResponse role, Set<PermissionResponse> permissions) {
}
