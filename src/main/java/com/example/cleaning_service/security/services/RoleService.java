package com.example.cleaning_service.security.services;

import com.example.cleaning_service.security.entities.role.ERole;
import com.example.cleaning_service.security.entities.role.Role;
import com.example.cleaning_service.security.repositories.RoleRepository;
import com.example.cleaning_service.security.entities.permission.EPermission;
import com.example.cleaning_service.security.entities.permission.Permission;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class RoleService {

    private final RoleRepository roleRepository;
    private final PermissionService permissionService;

    public RoleService(RoleRepository roleRepository, PermissionService permissionService) {
        this.roleRepository = roleRepository;
        this.permissionService = permissionService;
    }

    /**
     * Ensures that a role exists in the database.
     * If the role is missing, it is created along with its required permissions.
     */
    @Transactional
    public Role ensureRoleExists(ERole roleName) {
        return roleRepository.findByName(roleName)
                .orElseGet(() -> {
                    // Get EPermissions from the Enum Role
                    Set<EPermission> requiredPermissions = roleName.getPermissions();

                    // Ensure all necessary permissions exist in DB
                    Set<Permission> permissions = permissionService.ensurePermissionsExist(requiredPermissions);

                    // Create and save the new role
                    Role newRole = new Role(roleName, permissions);
                    newRole = roleRepository.save(newRole); // 🔹 Ensure Role is saved first

                    newRole.setPermissions(permissions); // 🔹 Explicitly assign permissions

                    return roleRepository.save(newRole);
                });
    }
}