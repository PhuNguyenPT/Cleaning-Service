package com.example.cleaning_service.security.repositories;

import com.example.cleaning_service.security.entities.permission.EPermission;
import com.example.cleaning_service.security.entities.permission.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface PermissionRepository extends JpaRepository<Permission, UUID> {
    Optional<Permission> findByName(EPermission name);
    Set<Permission> findByNameIn(Set<EPermission> names);
}
