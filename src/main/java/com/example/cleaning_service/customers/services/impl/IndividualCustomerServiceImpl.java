package com.example.cleaning_service.customers.services.impl;

import com.example.cleaning_service.commons.BusinessEntityService;
import com.example.cleaning_service.customers.assemblers.individuals.IndividualCustomerDetailsModelAssembler;
import com.example.cleaning_service.customers.dto.accounts.AccountRequest;
import com.example.cleaning_service.customers.dto.individuals.IndividualCustomerRequest;
import com.example.cleaning_service.customers.dto.individuals.IndividualCustomerUpdateRequest;
import com.example.cleaning_service.customers.entities.AbstractCustomer;
import com.example.cleaning_service.customers.entities.Account;
import com.example.cleaning_service.customers.entities.IndividualCustomer;
import com.example.cleaning_service.customers.enums.EAssociationType;
import com.example.cleaning_service.customers.events.CustomerCreationEvent;
import com.example.cleaning_service.customers.mappers.IndividualCustomerMapper;
import com.example.cleaning_service.customers.repositories.IndividualCustomerRepository;
import com.example.cleaning_service.customers.services.*;
import com.example.cleaning_service.security.entities.user.User;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Service implementation responsible for managing {@link IndividualCustomer} entities.
 * <p>
 * This service provides operations for creating, retrieving, updating, and deleting
 * individual customer information, ensuring data integrity and coordinating with related services.
 *
 * @see IndividualCustomerService
 */
@Service
class IndividualCustomerServiceImpl implements IndividualCustomerService {

    private static final Logger log = LoggerFactory.getLogger(IndividualCustomerServiceImpl.class);
    private final IndividualCustomerRepository individualCustomerRepository;
    private final AccountService accountService;
    private final AbstractCustomerService abstractCustomerService;
    private final BusinessEntityService businessEntityService;
    private final OrganizationDetailsService organizationDetailsService;
    private final CustomerService customerService;
    private final IndividualCustomerMapper individualCustomerMapper;
    private final ApplicationEventPublisher applicationEventPublisher;

    /**
     * Constructs a new IndividualCustomerServiceImpl with required dependencies.
     *
     * @param individualCustomerRepository The repository for individual customer operations
     * @param accountService The service for account operations
     * @param abstractCustomerService The service for abstract customer operations
     * @param businessEntityService The service for business entity operations
     * @param organizationDetailsService The service for organization details operations
     * @param customerService The service for general customer operations
     * @param individualCustomerMapper The mapper for individual customer objects
     */
    IndividualCustomerServiceImpl(
            IndividualCustomerRepository individualCustomerRepository,
            AccountService accountService,
            AbstractCustomerService abstractCustomerService,
            BusinessEntityService businessEntityService,
            OrganizationDetailsService organizationDetailsService,
            CustomerService customerService,
            IndividualCustomerMapper individualCustomerMapper,
            ApplicationEventPublisher applicationEventPublisher) {

        this.individualCustomerRepository = individualCustomerRepository;
        this.accountService = accountService;
        this.abstractCustomerService = abstractCustomerService;
        this.businessEntityService = businessEntityService;
        this.organizationDetailsService = organizationDetailsService;
        this.customerService = customerService;
        this.individualCustomerMapper = individualCustomerMapper;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Implementation details:
     * <ol>
     *   <li>Validates for duplicated fields using {@link CustomerService}</li>
     *   <li>Verifies account validity using {@link AccountService}</li>
     *   <li>Maps request data to entity using {@link IndividualCustomerMapper}</li>
     *   <li>Saves the new individual customer to the database</li>
     *   <li>Determines association type and primary status using {@link OrganizationDetailsService}</li>
     *   <li>Creates and links an account for the user and individual customer</li>
     *   <li>Verifies the account references the created individual customer correctly</li>
     * </ol>
     */
    @Override
    @Transactional
    public IndividualCustomer createIndividualCustomer(@Valid IndividualCustomerRequest individualCustomerRequest, User user) {
        log.info("Check duplicated fields");
        customerService.checkDuplicatedFields(
                individualCustomerRequest,
                individualCustomerRepository::existsByTaxId,
                individualCustomerRepository::existsByRegistrationNumber,
                individualCustomerRepository::existsByEmail
        );
        log.info("Attempting to create an individual customer for user: {}", user.getUsername());
        accountService.checkAccountReferenceCustomer(user);

        IndividualCustomer individualCustomer = individualCustomerMapper.fromRequestToCustomer(individualCustomerRequest);
        IndividualCustomer savedIndividualCustomer = saveIndividualCustomer(individualCustomer);

        log.info("Individual customer {} saved successfully with ID: {}", savedIndividualCustomer.getName(), savedIndividualCustomer.getId());

        EAssociationType associationType = organizationDetailsService.getEAssociationTypeByIOrganization(savedIndividualCustomer);
        boolean isPrimary = organizationDetailsService.getIsPrimaryByIOrganization(savedIndividualCustomer);

        // Create account
        AccountRequest accountRequest = new AccountRequest(
                user, savedIndividualCustomer, null, isPrimary, associationType
        );
        CustomerCreationEvent customerCreationEvent = new CustomerCreationEvent(accountRequest);
        applicationEventPublisher.publishEvent(customerCreationEvent);

        return savedIndividualCustomer;
    }

    /**
     * Saves an individual customer entity to the database.
     * <p>
     * This method persists the provided individual customer entity and returns the saved
     * instance with updated metadata.
     *
     * @param individualCustomer The {@link IndividualCustomer} entity to persist
     * @return The saved {@link IndividualCustomer} entity with updated metadata
     */
    @Transactional
    IndividualCustomer saveIndividualCustomer(IndividualCustomer individualCustomer) {
        return individualCustomerRepository.save(individualCustomer);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Implementation details:
     * <ol>
     *   <li>Logs retrieval attempt</li>
     *   <li>Calls {@link #getByIdAndUser} to verify access and retrieve the entity</li>
     *   <li>Uses {@link IndividualCustomerDetailsModelAssembler} to create the response model</li>
     *   <li>Logs successful retrieval</li>
     * </ol>
     */
    @Override
    @Transactional
    public IndividualCustomer getIndividualCustomerDetailsById(UUID id, User user) {
        log.info("Retrieving individual details for ID: {} by user: {}", id, user.getUsername());
        return getByIdAndUser(id, user);
    }

    /**
     * Retrieves an individual customer by its ID while verifying that the requesting user has access to it.
     * <p>
     * This method performs the following operations:
     * <ol>
     *   <li>Retrieves the user's account and associated customer</li>
     *   <li>Verifies the customer is the requested individual customer</li>
     *   <li>Returns the individual customer if validation passes</li>
     * </ol>
     *
     * @param id The UUID of the individual customer to retrieve
     * @param user The user requesting access to the individual customer
     * @return The {@link IndividualCustomer} entity if found and accessible by the user
     * @throws AccessDeniedException If the user does not have the required association
     */
    @Transactional
    IndividualCustomer getByIdAndUser(UUID id, User user) {
        AbstractCustomer abstractCustomer = accountService.findAccountWithCustomerByUser(user).getCustomer();
        if (isNotValidReferenceAbstractCustomer(id, abstractCustomer)) {
            throw new AccessDeniedException("User " + user.getUsername() + " is not associated with an individual customer with id " + id);
        }
        return (IndividualCustomer) abstractCustomer;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Implementation details:
     * <ol>
     *   <li>Logs update attempt</li>
     *   <li>Calls {@link #findIndividualCustomerToChange} to retrieve and verify access to the customer</li>
     *   <li>Updates the customer's fields using {@link #updateCustomerFields}</li>
     *   <li>Saves the updated customer to the database</li>
     *   <li>Logs successful update</li>
     *   <li>Returns the updated customer details as a response model</li>
     * </ol>
     */
    @Override
    @Transactional
    public IndividualCustomer updateIndividualCustomerDetailsById(UUID id, @Valid IndividualCustomerUpdateRequest updateRequest, User user) {
        log.info("Attempting to update individual customer details for ID: {} by user: {}", id, user.getUsername());
        IndividualCustomer individualCustomer = findIndividualCustomerToChange(id, user);

        updateCustomerFields(individualCustomer, updateRequest);

        IndividualCustomer updatedIndividualCustomer = saveIndividualCustomer(individualCustomer);
        log.info("Successfully updated company with ID: {}", updatedIndividualCustomer.getId());

        return updatedIndividualCustomer;
    }

    /**
     * Updates only the non-null fields of the individual customer entity.
     * <p>
     * This method performs the following operations:
     * <ol>
     *   <li>Delegates the update of customer-related details to {@link AbstractCustomerService}</li>
     *   <li>Delegates the update of business entity-specific details to {@link BusinessEntityService}</li>
     * </ol>
     *
     * @param individualCustomer The {@link IndividualCustomer} entity to update
     * @param updateRequest The request containing fields to update
     */
    @Transactional
    void updateCustomerFields(IndividualCustomer individualCustomer, @Valid IndividualCustomerUpdateRequest updateRequest) {
        abstractCustomerService.updateAbstractCustomerDetails(individualCustomer, updateRequest.customerDetails());

        businessEntityService.updateBusinessEntityFields(individualCustomer, updateRequest.businessEntityDetails());
    }

    /**
     * {@inheritDoc}
     * <p>
     * Implementation details:
     * <ol>
     *   <li>Logs deletion attempt</li>
     *   <li>Calls {@link #findIndividualCustomerToChange} to retrieve and verify access</li>
     *   <li>Detaches the individual customer from the user's account using {@link AccountService}</li>
     *   <li>Deletes the individual customer entity from the database</li>
     * </ol>
     */
    @Override
    @Transactional
    public void deleteIndividualCustomerById(UUID id, User user) {
        log.info("Deleting individual customer details for ID: {} by user: {}", id, user.getUsername());
        IndividualCustomer individualCustomer = findIndividualCustomerToChange(id, user);
        accountService.detachCustomerFromAccount(individualCustomer);
        individualCustomerRepository.delete(individualCustomer);
    }

    /**
     * Finds an individual customer for modification operations with access verification.
     * <p>
     * This method performs the following operations:
     * <ol>
     *   <li>Retrieves the user's account with associated customer</li>
     *   <li>Verifies the user has proper permission based on association type</li>
     *   <li>Verifies the customer is the requested individual customer</li>
     *   <li>Returns the individual customer if all verifications pass</li>
     * </ol>
     *
     * @param id The UUID of the individual customer to retrieve
     * @param user The user requesting to change the individual customer
     * @return The {@link IndividualCustomer} entity if found and accessible by the user
     * @throws AccessDeniedException If the user does not have appropriate permissions
     * @throws IllegalStateException If the account does not reference a valid individual customer
     */
    @Transactional
    IndividualCustomer findIndividualCustomerToChange(UUID id, User user) {
        Account account = accountService.findAccountWithCustomerByUser(user);

        if (accountService.isRepresentativeAssociationType(account)) {
            throw new AccessDeniedException("User " + user.getUsername() + " does not have permission to the " +
                    "individual with id " + id);
        }

        if (isNotValidReferenceAbstractCustomer(id, account.getCustomer())) {
            throw new IllegalStateException("Account does not reference a valid company.");
        }
        return (IndividualCustomer) account.getCustomer();
    }

    /**
     * Checks if the abstract customer is not a valid reference to the specified individual customer.
     * <p>
     * A reference is considered invalid if:
     * <ol>
     *   <li>The abstract customer is null</li>
     *   <li>The abstract customer's ID does not match the expected ID</li>
     *   <li>The abstract customer is not an instance of {@link IndividualCustomer}</li>
     * </ol>
     *
     * @param id The expected UUID of the individual customer
     * @param abstractCustomer The {@link AbstractCustomer} to validate
     * @return true if the reference is invalid, false otherwise
     */
    @Transactional
    boolean isNotValidReferenceAbstractCustomer(UUID id, AbstractCustomer abstractCustomer) {
        return abstractCustomer == null || !abstractCustomer.getId().equals(id) || !(abstractCustomer instanceof IndividualCustomer);
    }

    /**
     * Finds an individual customer by its ID.
     * <p>
     * This method retrieves an individual customer by ID without checking user associations,
     * intended for administrative use.
     *
     * @param id The UUID of the individual customer to retrieve
     * @return The {@link IndividualCustomer} entity if found
     * @throws EntityNotFoundException If the individual customer with the specified ID is not found
     */
    @Override
    @Transactional
    public IndividualCustomer findById(UUID id) {
        return individualCustomerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Individual customer with ID: " + id + " not found"));
    }
}