package com.example.cleaning_service.security.dtos.user;

import com.example.cleaning_service.security.entities.permission.EPermission;

import java.util.UUID;

public record PermissionResponse(UUID id, EPermission name) {
}
