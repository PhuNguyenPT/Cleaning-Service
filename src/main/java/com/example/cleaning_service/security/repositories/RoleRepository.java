package com.example.cleaning_service.security.repositories;

import com.example.cleaning_service.security.entities.role.ERole;
import com.example.cleaning_service.security.entities.role.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {
    Optional<Role> findByName(ERole name);
}
