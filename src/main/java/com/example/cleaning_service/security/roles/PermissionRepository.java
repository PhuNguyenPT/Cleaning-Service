package com.example.cleaning_service.security.roles;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Optional<Permission> findByName(EPermission name);
    Set<Permission> findByNameIn(Set<EPermission> names);
}
