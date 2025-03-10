package com.example.cleaning_service.security.dtos.user;

import com.example.cleaning_service.security.entities.role.ERole;

import java.util.UUID;

public record RoleResponse(UUID id, ERole name) {
}
