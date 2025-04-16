package com.example.cleaning_service.security.repositories;

import com.example.cleaning_service.security.entities.token.TokenEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TokenRepository extends CrudRepository<TokenEntity, UUID> {
    Optional<TokenEntity> findByToken(String token);
    boolean existsByToken(String token);
}