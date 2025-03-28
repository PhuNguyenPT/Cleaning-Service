package com.example.cleaning_service.security.repositories;

import com.example.cleaning_service.security.entities.token.TokenEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends CrudRepository<TokenEntity, String> {
}