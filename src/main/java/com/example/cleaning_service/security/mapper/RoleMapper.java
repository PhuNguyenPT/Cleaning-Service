package com.example.cleaning_service.security.mapper;

import com.example.cleaning_service.security.dtos.user.RoleResponse;
import com.example.cleaning_service.security.entities.role.Role;

public class RoleMapper {
    public static RoleResponse fromRoleToRoleResponse(Role role) {
        return new RoleResponse(
                role.getId(),
                role.getName()
        );
    }
}
