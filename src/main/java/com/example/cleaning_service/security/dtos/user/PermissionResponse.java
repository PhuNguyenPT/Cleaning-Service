package com.example.cleaning_service.security.dtos.user;

import com.example.cleaning_service.security.entities.permission.EPermission;

public record PermissionResponse(Long id, EPermission name) {
}
