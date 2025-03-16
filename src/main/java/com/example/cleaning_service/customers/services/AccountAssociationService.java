package com.example.cleaning_service.customers.services;

import com.example.cleaning_service.customers.dto.AccountAssociationRequest;
import com.example.cleaning_service.customers.entities.*;
import com.example.cleaning_service.customers.mappers.AccountAssociationMapper;
import com.example.cleaning_service.customers.repositories.AccountAssociationRepository;
import com.example.cleaning_service.security.entities.user.User;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

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

    private final AccountAssociationRepository accountAssociationRepository;
    private final AccountAssociationMapper accountAssociationMapper;

    /**
     * Constructs an `AccountAssociationService` with required dependencies.
     * <p>
     * This constructor performs the following actions:
     * 1. Initializes the repository for persistence operations.
     * 2. Sets up the mapper to convert between DTOs and entities.
     *
     * @param accountAssociationRepository Repository for account association persistence operations.
     * @param accountAssociationMapper Mapper to convert between DTOs and entities.
     */
    public AccountAssociationService(AccountAssociationRepository accountAssociationRepository, AccountAssociationMapper accountAssociationMapper) {
        this.accountAssociationRepository = accountAssociationRepository;
        this.accountAssociationMapper = accountAssociationMapper;
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
    AccountAssociation createAccountAssociation(@NotNull AccountAssociationRequest accountAssociationRequest) {
        AccountAssociation accountAssociation = accountAssociationMapper.fromAccountAssociationRequestToAccountAssociation(accountAssociationRequest);
        return accountAssociationRepository.save(accountAssociation);
    }

    /**
     * Checks if a user is associated with any customer account.
     * <p>
     * This method performs the following actions:
     * 1. Queries the system to check if an association exists for the user.
     * 2. Returns {@code true} if at least one association is found, otherwise returns {@code false}.
     *
     * @param user The user to check for associations.
     * @return {@code true} if the user has at least one association, {@code false} otherwise.
     */
    @Transactional
    boolean isExistsAccountAssociationByUser(@NotNull User user) {
        return accountAssociationRepository.existsAccountAssociationByUser(user);
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

    /**
     * Retrieves the account association for a specific customer.
     * <p>
     * This method performs the following actions:
     * 1. Queries the repository to find an association linked to the given customer.
     * 2. Returns the found `AccountAssociation` entity.
     *
     * @param customer The customer to find associations for.
     * @return The account association entity for the given customer.
     */
    @Transactional
    AccountAssociation getAccountAssociationByCustomer(@NotNull AbstractCustomer customer) {
        return accountAssociationRepository.findByCustomer(customer);
    }

    /**
     * Detaches a customer from its account association.
     * <p>
     * This method performs the following actions:
     * 1. Retrieves the existing account association for the given customer.
     * 2. Sets the customer reference in the association to `null` to detach it.
     * 3. Saves the updated association to maintain referential integrity.
     *
     * @param customer The customer to detach from its association.
     */
    @Transactional
    void detachCustomerFromAssociation(@NotNull AbstractCustomer customer) {
        AccountAssociation association = getAccountAssociationByCustomer(customer);
        association.setCustomer(null);
        accountAssociationRepository.save(association);
    }
}
