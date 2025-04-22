package com.example.cleaning_service.security.services.impl;

import com.example.cleaning_service.security.entities.role.ERole;
import com.example.cleaning_service.security.entities.role.Role;
import com.example.cleaning_service.security.repositories.RoleRepository;
import com.example.cleaning_service.security.entities.permission.EPermission;
import com.example.cleaning_service.security.entities.permission.Permission;
import com.example.cleaning_service.security.services.IPermissionService;
import com.example.cleaning_service.security.services.IRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class RoleService implements IRoleService {

    private final RoleRepository roleRepository;
    private final IPermissionService permissionService;

    public RoleService(RoleRepository roleRepository, IPermissionService permissionService) {
        this.roleRepository = roleRepository;
        this.permissionService = permissionService;
    }

    /**
     * Ensures that a role exists in the database.
     * If the role is missing, it is created along with its required permissions.
     */
    @Transactional
    @Override
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