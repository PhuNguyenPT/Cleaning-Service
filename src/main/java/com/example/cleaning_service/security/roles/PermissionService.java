package com.example.cleaning_service.security.roles;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PermissionService {

    private final PermissionRepository permissionRepository;

    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    /**
     * Ensures that all required permissions exist in the database.
     * If any permissions are missing, they are created and saved.
     */
    @Transactional
    public Set<Permission> ensurePermissionsExist(Set<EPermission> requiredPermissions) {
        // 🔹 Fetch existing permissions in one query
        Set<Permission> existingPermissions = permissionRepository.findByNameIn(requiredPermissions);

        // 🔹 Identify missing permissions
        Set<EPermission> missingPermissions = requiredPermissions.stream()
                .filter(permission -> existingPermissions.stream()
                        .noneMatch(existing -> existing.getName().equals(permission)))
                .collect(Collectors.toSet());

        // 🔹 Insert missing permissions in **one batch query**
        if (!missingPermissions.isEmpty()) {
            Set<Permission> newPermissions = missingPermissions.stream()
                    .map(Permission::new)
                    .collect(Collectors.toSet());

            permissionRepository.saveAll(newPermissions); // 🔹 Save permissions before returning them
            existingPermissions.addAll(newPermissions);
        }

        return existingPermissions;
    }
}
