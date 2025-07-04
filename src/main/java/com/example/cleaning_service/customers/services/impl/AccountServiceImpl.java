package com.example.cleaning_service.customers.services.impl;

import com.example.cleaning_service.customers.dto.accounts.AccountRequest;
import com.example.cleaning_service.customers.dto.accounts.AccountUpdateRequest;
import com.example.cleaning_service.customers.entities.*;
import com.example.cleaning_service.customers.enums.EAssociationType;
import com.example.cleaning_service.customers.repositories.AccountRepository;
import com.example.cleaning_service.customers.services.AccountService;
import com.example.cleaning_service.security.entities.user.User;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Service class that manages associations between users and customer accounts.
 * <p>
 * This service performs the following operations:
 * 1. Provides methods for creating, retrieving, counting, and modifying account associations.
 * 2. Acts as the main interface for managing relationships between users and customer entities.
 * 3. Ensures referential integrity when detaching associations.
 */
@Service
class AccountServiceImpl implements AccountService {

    private static final Logger log = LoggerFactory.getLogger(AccountServiceImpl.class);
    private final AccountRepository accountRepository;

    AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    @Transactional
    public Account findById(UUID id) {
        log.info("Start retrieving account details with id: {}", id);
        return accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Account with id " + id + " not found"));
    }

    @Transactional
    Account saveAccount(Account account) {
        return accountRepository.save(account);
    }

    @Override
    @Transactional
    public Account findAccountWithCustomerByUser(User user) {
        log.info("Attempting to find account for {}", user);
        return accountRepository.findByUser(user)
                .orElseThrow(() -> new EntityNotFoundException("User " + user.getUsername() + "'s account not found"));
    }

    @Override
    @Transactional
    public void checkAccountReferenceCustomer(User user) {
        Account account = accountRepository.findByUser(user)
                .orElse(null);
        if (account == null) {return;}
        log.info("Attempting to check account reference a customer: {}", account);
        if (account.getCustomer() != null) {
            throw new EntityExistsException("Account with ID: " + account.getId() +
                    " already references a Customer with ID: " + account.getCustomer().getId());
        }
        log.info("Account with ID: {} does not reference a Customer", account.getId());
    }

    /**
     * Creates a new account association from a request.
     * <p>
     * This method performs the following actions:
     * 1. Maps the provided request to an `AccountAssociation` entity.
     * 2. Persists the entity in the database.
     * 3. Returns the saved entity.
     *
     * @param accountRequest The request containing association details.
     * @return The created and persisted account association entity.
     */
    @Override
    @Transactional
    public Account handleCustomerCreation(@Valid AccountRequest accountRequest) {
        Account account = accountRepository.findByUser(accountRequest.user())
                .orElse(new Account());
        if (accountRequest.user() != null) {
            account.setUser(accountRequest.user());
        }
        if (accountRequest.customer() != null) {
            account.setCustomer(accountRequest.customer());
        }
        if (accountRequest.isPrimary() != null) {
            account.setPrimary(accountRequest.isPrimary());
        }
        if (accountRequest.notes() != null) {
            account.setNotes(accountRequest.notes());
        }
        if (accountRequest.associationType() != null) {
            account.setAssociationType(accountRequest.associationType());
        }
        return saveAccount(account);
    }

    @Override
    @Transactional
    public void detachCustomerFromAccount(@NotNull AbstractCustomer abstractCustomer) {
        List<Account> accounts = findAllByCustomer(abstractCustomer);
        accounts.forEach(accountAssociation -> accountAssociation.setCustomer(null));
        accountRepository.saveAll(accounts);
    }

    List<Account> findAllByCustomer(AbstractCustomer abstractCustomer) {
        return accountRepository.findByCustomer(abstractCustomer);
    }

    @Override
    @Transactional
    public Account getAccountDetailsResponseModelById(UUID id, User user) {
        Account account = findWithCustomerById(id);
        if (!account.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("User " + user.getUsername()  + " is not authorized to access this account with id " + id);
        }
        return account;
    }

    @Transactional
    public boolean isRepresentativeAssociationType(Account account) {
        return account.getAssociationType().equals(EAssociationType.REPRESENTATIVE);
    }

    @Override
    @Transactional
    public Account patchAccountDetailsById(UUID id, AccountUpdateRequest accountUpdateRequest) {
        log.info("Attempting to update account details by id {}", id);
        Account account = findById(id);
        log.info("Retrieved account details for patch {}", account);
        Account patchedAccount = patchAccountFields(account, accountUpdateRequest);
        log.info("Patched account details {}", patchedAccount);
        return patchedAccount;

    }

    @Transactional
    Account patchAccountFields(Account account, AccountUpdateRequest accountUpdateRequest) {
        if (account == null || accountUpdateRequest == null) {
            log.warn("Attempting to patch account fields with account: {}, \n " +
                    "accountUpdateRequest: {}", account, accountUpdateRequest);
            return null;
        }
        if (accountUpdateRequest.notes() != null)  {
            account.setNotes(accountUpdateRequest.notes());
        }
        if (accountUpdateRequest.isPrimary() != null) {
            account.setPrimary(accountUpdateRequest.isPrimary());
        }
        if (accountUpdateRequest.eAssociationType() != null) {
            account.setAssociationType(accountUpdateRequest.eAssociationType());
        }
        return saveAccount(account);
    }

    @Override
    @Transactional(readOnly = true)
    public Account findWithCustomerById(UUID id) {
        return accountRepository.findWithCustomerById(id)
                .orElseThrow(() -> new EntityNotFoundException("Account with id " + id + " not found"));
    }
}
