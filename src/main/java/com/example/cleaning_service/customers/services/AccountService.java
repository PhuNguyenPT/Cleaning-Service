package com.example.cleaning_service.customers.services;

import com.example.cleaning_service.customers.assemblers.accounts.AccountDetailsResponseModelAssembler;
import com.example.cleaning_service.customers.assemblers.accounts.AccountResponseModelAssembler;
import com.example.cleaning_service.customers.controllers.AccountController;
import com.example.cleaning_service.customers.dto.accounts.AccountDetailsResponseModel;
import com.example.cleaning_service.customers.dto.accounts.AccountRequest;
import com.example.cleaning_service.customers.dto.accounts.AccountResponseModel;
import com.example.cleaning_service.customers.entities.*;
import com.example.cleaning_service.customers.enums.EAssociationType;
import com.example.cleaning_service.customers.repositories.AccountRepository;
import com.example.cleaning_service.security.controllers.AuthController;
import com.example.cleaning_service.security.entities.user.User;
import com.example.cleaning_service.security.events.UserDeletedEvent;
import com.example.cleaning_service.security.events.UserRegisteredEvent;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.hateoas.Link;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Service class that manages associations between users and customer accounts.
 * <p>
 * This service performs the following operations:
 * 1. Provides methods for creating, retrieving, counting, and modifying account associations.
 * 2. Acts as the main interface for managing relationships between users and customer entities.
 * 3. Ensures referential integrity when detaching associations.
 */
@Service
public class AccountService {

    private static final Logger log = LoggerFactory.getLogger(AccountService.class);
    private final AccountRepository accountRepository;
    private final AccountDetailsResponseModelAssembler accountDetailsResponseModelAssembler;
    private final OrganizationDetailsService organizationDetailsService;
    private final AccountResponseModelAssembler accountResponseModelAssembler;

    /**
     * Constructs an `AccountService` with required dependencies.
     * <p>
     * This constructor performs the following actions:
     * 1. Initializes the repository for persistence operations.
     * 2. Sets up the mapper to convert between DTOs and entities.
     *
     * @param accountRepository Repository for account association persistence operations.
     */
    public AccountService(AccountRepository accountRepository, AccountDetailsResponseModelAssembler accountDetailsResponseModelAssembler, OrganizationDetailsService organizationDetailsService, AccountResponseModelAssembler accountResponseModelAssembler) {
        this.accountRepository = accountRepository;
        this.accountDetailsResponseModelAssembler = accountDetailsResponseModelAssembler;
        this.organizationDetailsService = organizationDetailsService;
        this.accountResponseModelAssembler = accountResponseModelAssembler;
    }

    @Transactional
    Account findById(UUID id) {
        log.info("Start retrieving account details with id: {}", id);
        return accountRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Transactional
    Account saveAccount(Account account) {
        return accountRepository.save(account);
    }

    @Transactional
    Account findAccountWithCustomerByUser(User user) {
        log.info("Attempting to find account for {}", user);
        return accountRepository.findByUser(user)
                .orElseThrow(() -> new EntityNotFoundException("User " + user.getUsername() + "'s account not found"));
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
    @Transactional
    Account updateAccount(@Valid AccountRequest accountRequest, Account account) {
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

    @Transactional
    void detachCustomerFromAccount(AbstractCustomer abstractCustomer) {
        List<Account> accounts = findAllByCustomer(abstractCustomer);
        accounts.forEach(accountAssociation -> accountAssociation.setCustomer(null));
        accountRepository.saveAll(accounts);
    }

    List<Account> findAllByCustomer(AbstractCustomer abstractCustomer) {
        return accountRepository.findByCustomer(abstractCustomer);
    }

    @EventListener
    @Transactional
    void createAccountOnUserRegisteredEvent(UserRegisteredEvent event) {
        Account association = new Account(event.user(), null, null, true, EAssociationType.OWNER);
        saveAccount(association);
    }

    @EventListener
    @Transactional
    void deleteAccountByUser(UserDeletedEvent event) {
        accountRepository.deleteByUser(event.user());
        log.info("User with id {} 's account successfully deleted.", event.user().getId());
    }

    @Transactional
    public AccountResponseModel getAccountResponseModelById(User user) {
        log.info("Start retrieving account details for {}", user);
        Account account = findAccountWithCustomerByUser(user);
        log.info("Retrieved account entity {}", account);

        AccountResponseModel accountResponseModel = accountResponseModelAssembler.toModel(account);
        log.info("Retrieved account model {}", accountResponseModel);

        if (account.getCustomer() != null) {
            Link customerLink = organizationDetailsService.getLinkByIOrganization((IOrganization) account.getCustomer());
            log.info("Retrieved customer link {}", customerLink);
            accountResponseModel.add(customerLink);
        }

        Link userProfileLink = linkTo(methodOn(AuthController.class).getAuthenticatedUser(user)).withRel("profile");
        log.info("Retrieved user profile link {}", userProfileLink);

        accountResponseModel.add(userProfileLink);

        return accountResponseModel;
    }

    @Transactional
    public AccountDetailsResponseModel getAccountDetailsResponseModelById(UUID id, User user) {
        Account account = findById(id);
        if (!account.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("User " + user.getUsername()  + " is not authorized to access this account with id " + id);
        }
        AccountDetailsResponseModel accountDetailsResponseModel = accountDetailsResponseModelAssembler.toModel(account);
        log.info("Retrieved account details model {}", accountDetailsResponseModel);

        Link accountDefaultLink = linkTo(methodOn(AccountController.class).getAccountByUser(user)).withRel("me");
        log.info("Retrieved account link {}", accountDefaultLink);
        accountDetailsResponseModel.add(accountDefaultLink);

        return accountDetailsResponseModel;
    }

    @Transactional
    boolean isRepresentativeAssociationType(Account account) {
        return account.getAssociationType().equals(EAssociationType.REPRESENTATIVE);
    }
}
