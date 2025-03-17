package com.example.cleaning_service.customers.services;

import com.example.cleaning_service.customers.dto.AccountAssociationRequest;
import com.example.cleaning_service.customers.entities.*;
import com.example.cleaning_service.customers.enums.EAssociationType;
import com.example.cleaning_service.customers.repositories.AccountAssociationRepository;
import com.example.cleaning_service.security.entities.user.User;
import com.example.cleaning_service.security.events.UserDeletedEvent;
import com.example.cleaning_service.security.events.UserRegisteredEvent;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class that manages associations between users and customer accounts.
 * <p>
 * This service performs the following operations:
 * 1. Provides methods for creating, retrieving, counting, and modifying account associations.
 * 2. Acts as the main interface for managing relationships between users and customer entities.
 * 3. Ensures referential integrity when detaching associations.
 */
@Service
public class AccountAssociationService {

    private static final Logger log = LoggerFactory.getLogger(AccountAssociationService.class);
    private final AccountAssociationRepository accountAssociationRepository;

    /**
     * Constructs an `AccountAssociationService` with required dependencies.
     * <p>
     * This constructor performs the following actions:
     * 1. Initializes the repository for persistence operations.
     * 2. Sets up the mapper to convert between DTOs and entities.
     *
     * @param accountAssociationRepository Repository for account association persistence operations.
     */
    public AccountAssociationService(AccountAssociationRepository accountAssociationRepository) {
        this.accountAssociationRepository = accountAssociationRepository;
    }

    @EventListener
    @Transactional
    void createAccountAssociationOnUserRegisteredEvent(@NotNull UserRegisteredEvent event) {
        AccountAssociation association = new AccountAssociation(event.user(), null, null, true, EAssociationType.OWNER);
        saveAccountAssociation(association);
    }

    @Transactional
    AccountAssociation saveAccountAssociation(AccountAssociation accountAssociation) {
        return accountAssociationRepository.save(accountAssociation);
    }

    /**
     * Counts the number of account associations for a specific customer.
     * <p>
     * This method performs the following actions:
     * 1. Queries the repository for the number of associations linked to the customer.
     * 2. Returns the count as an integer.
     *
     * @param customer The customer entity to count associations for.
     * @return The number of associations for the given customer.
     */
    @Transactional
    Integer countAccountAssociationByCustomer(@NotNull AbstractCustomer customer) {
        return accountAssociationRepository.countByCustomer(customer);
    }

    @Transactional
    AccountAssociation findByUser(@NotNull User user) {
        return accountAssociationRepository.findByUser(user)
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
     * @param accountAssociationRequest The request containing association details.
     * @return The created and persisted account association entity.
     */
    @Transactional
    AccountAssociation updateAccountAssociation(@NotNull AccountAssociationRequest accountAssociationRequest, AccountAssociation accountAssociation) {
        if (accountAssociationRequest.customer() != null) {
            accountAssociation.setCustomer(accountAssociationRequest.customer());
        }
        if (accountAssociationRequest.isPrimary() != null) {
            accountAssociation.setPrimary(accountAssociationRequest.isPrimary());
        }
        if (accountAssociationRequest.notes() != null) {
            accountAssociation.setNotes(accountAssociationRequest.notes());
        }
        if (accountAssociationRequest.associationType() != null) {
            accountAssociation.setAssociationType(accountAssociationRequest.associationType());
        }
        return saveAccountAssociation(accountAssociation);
    }

    /**
     * Checks if no association exists between a specific user and customer.
     * <p>
     * This method performs the following actions:
     * 1. Queries the system to determine if an association exists between the given user and customer.
     * 2. Returns {@code true} if no association is found, otherwise returns {@code false}.
     *
     * @param user The user to check for an association.
     * @param customer The customer to check for an association with the user.
     * @return {@code true} if no association exists between the user and customer, {@code false} otherwise.
     */
    @Transactional
    boolean isNotExistsAccountAssociationByUserAndCustomer(@NotNull User user, @NotNull AbstractCustomer customer) {
        return !accountAssociationRepository.existsAccountAssociationByUserAndCustomer(user, customer);
    }

    @Transactional
    void detachCustomerFromAssociation(@NotNull AbstractCustomer abstractCustomer) {
        List<AccountAssociation> accountAssociations = findAllByCustomer(abstractCustomer);
        accountAssociations.forEach(accountAssociation -> accountAssociation.setCustomer(null));
        accountAssociationRepository.saveAll(accountAssociations);
    }

    List<AccountAssociation> findAllByCustomer(@NotNull AbstractCustomer abstractCustomer) {
        return accountAssociationRepository.findByCustomer(abstractCustomer);
    }

    @EventListener
    @Transactional
    void deleteAccountAssociationByUser(@NotNull UserDeletedEvent event) {
        accountAssociationRepository.deleteByUser(event.user());
        log.info("User with id " + event.user().getId() + " 's account successfully deleted.");
    }
}
