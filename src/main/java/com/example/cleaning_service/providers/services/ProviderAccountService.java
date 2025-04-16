package com.example.cleaning_service.providers.services;

import com.example.cleaning_service.providers.entities.ProviderAccount;
import com.example.cleaning_service.providers.repositories.ProviderAccountRepository;
import com.example.cleaning_service.security.entities.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
public class ProviderAccountService {
    private final ProviderAccountRepository providerAccountRepository;


    public ProviderAccountService(ProviderAccountRepository providerAccountRepository) {
        this.providerAccountRepository = providerAccountRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ProviderAccount saveProviderAccount(ProviderAccount providerAccount) {
        return providerAccountRepository.saveAndFlush(providerAccount);
    }

    @Transactional
    public ProviderAccount findByProvider_IdAndUser(UUID providerId, User user) {
        ProviderAccount providerAccount = providerAccountRepository.findByProvider_IdAndUser(providerId, user)
                .orElseThrow(() -> new EntityNotFoundException("Provider " + providerId + " not found for user " + user.getId()));
        if (!providerAccount.getActive()) {
            throw new IllegalStateException("Provider Account" + providerAccount.getId() + " is not active");
        }
        return providerAccount;
    }

    @Transactional
    public ProviderAccount findById(UUID id) {
        return providerAccountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Provider Account with id " + id + " not found"));
    }
}
