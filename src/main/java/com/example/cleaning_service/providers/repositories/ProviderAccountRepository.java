package com.example.cleaning_service.providers.repositories;

import com.example.cleaning_service.providers.entities.ProviderAccount;
import com.example.cleaning_service.security.entities.user.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProviderAccountRepository extends JpaRepository<ProviderAccount, UUID> {
    @EntityGraph(attributePaths = {"provider", "user"})
    Optional<ProviderAccount> findByProvider_IdAndUser(UUID providerId, User user);
}
