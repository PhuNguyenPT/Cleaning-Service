package com.example.cleaning_service.security.mapper;

import com.example.cleaning_service.security.dtos.user.PermissionResponse;
import com.example.cleaning_service.security.entities.permission.Permission;

import java.util.Set;
import java.util.stream.Collectors;

public class PermissionMapper {
    public static Set<PermissionResponse> fromPermissionSetToPermissionResponseSet(Set<Permission> permissions) {
        return permissions.stream()
                .map(PermissionMapper::fromPermissionToPermissionResponse)
                .collect(Collectors.toSet());
    }

    public static PermissionResponse fromPermissionToPermissionResponse(Permission permission) {
        return new PermissionResponse(
                permission.getId(),
                permission.getName()
        );
    }
}
