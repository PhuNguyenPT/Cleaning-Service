package com.example.cleaning_service.security.dtos.user;

import com.example.cleaning_service.security.entities.role.ERole;

public record RoleResponse(Long id, ERole name) {
}
