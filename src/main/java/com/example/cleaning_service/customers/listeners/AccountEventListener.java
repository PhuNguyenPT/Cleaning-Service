package com.example.cleaning_service.customers.listeners;

import com.example.cleaning_service.customers.entities.Account;
import com.example.cleaning_service.customers.events.CustomerCreationEvent;
import com.example.cleaning_service.customers.repositories.AccountRepository;
import com.example.cleaning_service.customers.services.AccountService;
import com.example.cleaning_service.security.events.UserDeletedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
public class AccountEventListener {


    private final AccountRepository accountRepository;
    private final AccountService accountService;

    public AccountEventListener(AccountRepository accountRepository, AccountService accountService) {
        this.accountRepository = accountRepository;
        this.accountService = accountService;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    void deleteAccountByUser(UserDeletedEvent event) {
        accountRepository.deleteByUser(event.user());
        log.info("User with id {} 's account successfully deleted.", event.user().getId());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    void handleCustomerCreationEvent(CustomerCreationEvent event) {
        Account account = accountService.handleCustomerCreation(event.accountRequest());
        log.info("Customer Account {} successfully created", account.getId());
    }
}
