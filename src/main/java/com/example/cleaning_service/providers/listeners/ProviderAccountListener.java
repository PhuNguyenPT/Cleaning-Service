package com.example.cleaning_service.providers.listeners;

import com.example.cleaning_service.providers.entities.Provider;
import com.example.cleaning_service.providers.entities.ProviderAccount;
import com.example.cleaning_service.providers.events.ProviderCreatedEvent;
import com.example.cleaning_service.providers.services.ProviderAccountService;
import com.example.cleaning_service.providers.services.ProviderService;
import com.example.cleaning_service.security.entities.user.User;
import com.example.cleaning_service.security.services.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
public class ProviderAccountListener {
    private final ProviderService providerService;
    private final IUserService userService;
    private final ProviderAccountService providerAccountService;

    public ProviderAccountListener(ProviderService providerService, IUserService userService, ProviderAccountService providerAccountService) {
        this.providerService = providerService;
        this.userService = userService;
        this.providerAccountService = providerAccountService;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    void handleProviderAccountCreatedEvent(ProviderCreatedEvent providerCreatedEvent) {
        Provider provider = providerService.findById(providerCreatedEvent.provider().getId());
        User user = userService.findById(providerCreatedEvent.user().getId());
        ProviderAccount providerAccount = ProviderAccount.builder()
                .provider(provider)
                .user(user)
                .active(true)
                .build();
        log.info("Provider account created for provider {}, and user {}", providerAccount.getProvider().getId(),
                providerAccount.getUser().getId());
        ProviderAccount savedProviderAccount = providerAccountService.saveProviderAccount(providerAccount);

        log.info("Provider account {} successfully saved", savedProviderAccount.getId());
    }
}
