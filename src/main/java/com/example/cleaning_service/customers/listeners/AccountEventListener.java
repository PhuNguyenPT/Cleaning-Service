package com.example.cleaning_service.customers.listeners;

import com.example.cleaning_service.customers.repositories.AccountRepository;
import com.example.cleaning_service.security.events.UserDeletedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
public class AccountEventListener {


    private final AccountRepository accountRepository;

    public AccountEventListener(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener
    void deleteAccountByUser(UserDeletedEvent event) {
        accountRepository.deleteByUser(event.user());
        log.info("User with id {} 's account successfully deleted.", event.user().getId());
    }
}
