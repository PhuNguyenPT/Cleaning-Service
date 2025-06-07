package com.example.cleaning_service.security.repositories;

import com.example.cleaning_service.security.entities.user.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    @EntityGraph(attributePaths = {"roles", "permissions"})
    Optional<User> findWithRolesAndPermissionsByUsername(String username);
    boolean existsByUsername(String username);
}