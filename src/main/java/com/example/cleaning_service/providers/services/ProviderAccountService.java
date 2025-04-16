package com.example.cleaning_service.providers.services;

import com.example.cleaning_service.providers.entities.Provider;
import com.example.cleaning_service.providers.entities.ProviderAccount;
import com.example.cleaning_service.providers.events.ProviderCreatedEvent;
import com.example.cleaning_service.providers.repositories.ProviderAccountRepository;
import com.example.cleaning_service.security.entities.user.User;
import com.example.cleaning_service.security.services.IUserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.UUID;


@Slf4j
@Service
public class ProviderAccountService {
    private final ProviderAccountRepository providerAccountRepository;
    private final ProviderService providerService;
    private final IUserService userService;

    public ProviderAccountService(ProviderAccountRepository providerAccountRepository, ProviderService providerService, IUserService userService) {
        this.providerAccountRepository = providerAccountRepository;
        this.providerService = providerService;
        this.userService = userService;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleProviderAccountCreatedEvent(ProviderCreatedEvent providerCreatedEvent) {
        Provider provider = providerService.findById(providerCreatedEvent.provider().getId());
        User user = userService.findById(providerCreatedEvent.user().getId());
        ProviderAccount providerAccount = ProviderAccount.builder()
                .provider(provider)
                .user(user)
                .active(true)
                .build();
        log.info("Provider account created for provider {}, and user {}", providerAccount.getProvider().getId(),
                providerAccount.getUser().getId());
        ProviderAccount savedProviderAccount = saveProviderAccount(providerAccount);

        log.info("Provider account {} successfully saved", savedProviderAccount.getId());
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
