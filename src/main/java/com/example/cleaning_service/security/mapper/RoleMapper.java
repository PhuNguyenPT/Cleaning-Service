package com.example.cleaning_service.security.mapper;

import com.example.cleaning_service.security.dtos.user.RoleResponse;
import com.example.cleaning_service.security.entities.role.Role;

import java.util.Set;
import java.util.stream.Collectors;

public class RoleMapper {
    public static RoleResponse fromRoleToRoleResponse(Role role) {
        return new RoleResponse(
                role.getId(),
                role.getName()
        );
    }

    public static Set<RoleResponse> fromRolesToRoleResponseSet(Set<Role> roles) {
        return roles.stream().map(RoleMapper::fromRoleToRoleResponse).collect(Collectors.toSet());
    }
}
