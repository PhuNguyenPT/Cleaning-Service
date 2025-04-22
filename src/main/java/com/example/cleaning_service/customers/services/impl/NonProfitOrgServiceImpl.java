package com.example.cleaning_service.customers.services.impl;

import com.example.cleaning_service.commons.BusinessEntityService;
import com.example.cleaning_service.customers.assemblers.non_profit_org.AdminNonProfitOrgDetailsModelAssembler;
import com.example.cleaning_service.customers.assemblers.non_profit_org.NonProfitOrgDetailModelAssembler;
import com.example.cleaning_service.customers.assemblers.non_profit_org.NonProfitOrgModelAssembler;
import com.example.cleaning_service.customers.dto.accounts.AccountRequest;
import com.example.cleaning_service.customers.dto.non_profit_org.NonProfitOrgDetailsResponseModel;
import com.example.cleaning_service.customers.dto.non_profit_org.NonProfitOrgRequest;
import com.example.cleaning_service.customers.dto.non_profit_org.NonProfitOrgResponseModel;
import com.example.cleaning_service.customers.dto.non_profit_org.NonProfitOrgUpdateRequest;
import com.example.cleaning_service.customers.entities.AbstractCustomer;
import com.example.cleaning_service.customers.entities.Account;
import com.example.cleaning_service.customers.entities.NonProfitOrg;
import com.example.cleaning_service.customers.enums.EAssociationType;
import com.example.cleaning_service.customers.events.CustomerCreationEvent;
import com.example.cleaning_service.customers.mappers.NonProfitOrgMapper;
import com.example.cleaning_service.customers.repositories.NonProfitOrgRepository;
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
 * Implementation of {@link NonProfitOrgService} responsible for managing Non-Profit Organization entities.
 * <p>
 * This service provides concrete implementations for creating, retrieving, updating, and deleting
 * non-profit organization information, ensuring data integrity and coordinating with related services.
 * @see NonProfitOrgService
 */
@Service
class NonProfitOrgServiceImpl implements NonProfitOrgService {
    private static final Logger log = LoggerFactory.getLogger(NonProfitOrgServiceImpl.class);
    private final NonProfitOrgRepository nonProfitOrgRepository;
    private final AccountService accountService;
    private final OrganizationDetailsService organizationDetailsService;
    private final AbstractCustomerService abstractCustomerService;
    private final BusinessEntityService businessEntityService;
    private final CustomerService customerService;

    private final NonProfitOrgModelAssembler nonProfitOrgModelAssembler;
    private final NonProfitOrgDetailModelAssembler nonProfitOrgDetailModelAssembler;

    private final NonProfitOrgMapper nonProfitOrgMapper;
    private final AdminNonProfitOrgDetailsModelAssembler adminNonProfitOrgDetailsModelAssembler;
    private final ApplicationEventPublisher applicationEventPublisher;

    /**
     * Constructs a new {@link NonProfitOrgServiceImpl} with the required dependencies.
     *
     * @param nonProfitOrgRepository Repository for {@link NonProfitOrg} data access
     * @param accountService Service for managing account associations
     * @param organizationDetailsService Service for managing organization details
     * @param abstractCustomerService Service for managing customer details
     * @param businessEntityService Service for managing business entity details
     * @param customerService Service for general customer operations
     * @param nonProfitOrgModelAssembler Assembler for basic organization models
     * @param nonProfitOrgDetailModelAssembler Assembler for detailed organization models
     * @param nonProfitOrgMapper Mapper for converting between DTOs and entities
     * @param adminNonProfitOrgDetailsModelAssembler Assembler for admin-focused organization models
     */
    NonProfitOrgServiceImpl(
            NonProfitOrgRepository nonProfitOrgRepository,
            AccountService accountService,
            OrganizationDetailsService organizationDetailsService,
            AbstractCustomerService abstractCustomerService,
            BusinessEntityService businessEntityService,
            CustomerService customerService,
            NonProfitOrgModelAssembler nonProfitOrgModelAssembler,
            NonProfitOrgDetailModelAssembler nonProfitOrgDetailModelAssembler,
            NonProfitOrgMapper nonProfitOrgMapper,
            AdminNonProfitOrgDetailsModelAssembler adminNonProfitOrgDetailsModelAssembler, ApplicationEventPublisher applicationEventPublisher) {

        this.nonProfitOrgRepository = nonProfitOrgRepository;
        this.accountService = accountService;
        this.organizationDetailsService = organizationDetailsService;
        this.abstractCustomerService = abstractCustomerService;
        this.businessEntityService = businessEntityService;
        this.customerService = customerService;

        this.nonProfitOrgModelAssembler = nonProfitOrgModelAssembler;
        this.nonProfitOrgDetailModelAssembler = nonProfitOrgDetailModelAssembler;

        this.nonProfitOrgMapper = nonProfitOrgMapper;
        this.adminNonProfitOrgDetailsModelAssembler = adminNonProfitOrgDetailsModelAssembler;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Implementation steps:
     * <ol>
     *     <li>Validates for duplicate fields using {@link CustomerService}</li>
     *     <li>Verifies user's account reference status using {@link AccountService}</li>
     *     <li>Maps request data to a {@link NonProfitOrg} entity and persists it</li>
     *     <li>Determines association type and primary status via {@link OrganizationDetailsService}</li>
     *     <li>Creates account association with the new organization</li>
     *     <li>Verifies the account association references the correct organization</li>
     *     <li>Creates and returns the response model</li>
     * </ol>
     */
    @Override
    @Transactional
    public NonProfitOrgResponseModel createProfitOrg(@Valid NonProfitOrgRequest nonProfitOrgRequest, User user) {
        log.info("Check duplicated fields");
        customerService.checkDuplicatedFields(
                nonProfitOrgRequest,
                nonProfitOrgRepository::existsByTaxId,
                nonProfitOrgRepository::existsByRegistrationNumber,
                nonProfitOrgRepository::existsByEmail
        );

        log.info("Attempting to create a non-profit org for user: {}", user.getUsername());
        accountService.checkAccountReferenceCustomer(user);

        NonProfitOrg nonProfitOrg = nonProfitOrgMapper.fromRequestToNonProfitOrg(nonProfitOrgRequest);
        NonProfitOrg savedNonProfitOrg = saveNonProfitOrg(nonProfitOrg);
        log.info("Non-profit org {} saved successfully with ID: {}", savedNonProfitOrg.getName(), savedNonProfitOrg.getId());

        EAssociationType associationType = organizationDetailsService.getEAssociationTypeByIOrganization(savedNonProfitOrg);
        boolean isPrimary = organizationDetailsService.getIsPrimaryByIOrganization(savedNonProfitOrg);

        // Create account association
        AccountRequest accountRequest = new AccountRequest(
                user, savedNonProfitOrg, null, isPrimary, associationType
        );
        CustomerCreationEvent customerCreationEvent = new CustomerCreationEvent(accountRequest);
        applicationEventPublisher.publishEvent(customerCreationEvent);
//        Account account = accountService.handleCustomerCreation(accountRequest);
//
//        if(isNotValidReferenceAbstractCustomer(account.getCustomer().getId(), account.getCustomer())) {
//            throw new IllegalStateException("Account association does not reference a valid non-profit org.");
//        }
//        NonProfitOrgResponseModel nonProfitOrgResponseModel = nonProfitOrgModelAssembler.toModel((NonProfitOrg) account.getCustomer());

        NonProfitOrgResponseModel nonProfitOrgResponseModel = nonProfitOrgModelAssembler.toModel(savedNonProfitOrg);
        log.info("Successfully created non-profit response: {}", nonProfitOrgResponseModel);

        return nonProfitOrgResponseModel;
    }

    /**
     * Saves a non-profit organization entity to the database.
     *
     * @param nonProfitOrg The non-profit organization entity to persist
     * @return The saved {@link NonProfitOrg} entity with updated metadata
     */
    @Transactional
    NonProfitOrg saveNonProfitOrg(NonProfitOrg nonProfitOrg) {
        return nonProfitOrgRepository.save(nonProfitOrg);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Implementation steps:
     * <ol>
     *     <li>Retrieves and validates the {@link NonProfitOrg} using {@link #getByIdAndUser}</li>
     *     <li>Converts the entity to a detailed response model</li>
     * </ol>
     */
    @Override
    @Transactional
    public NonProfitOrgDetailsResponseModel getNonProfitOrgDetailsResponseModelById(UUID id, User user) {
        NonProfitOrg nonProfitOrg = getByIdAndUser(id, user);
        return nonProfitOrgDetailModelAssembler.toModel(nonProfitOrg);
    }

    /**
     * Retrieves a non-profit organization by its ID while verifying user access rights.
     * <p>
     * Implementation steps:
     * <ol>
     *     <li>Fetches the user's associated {@link AbstractCustomer} via {@link AccountService}</li>
     *     <li>Validates that the customer is the requested non-profit organization</li>
     *     <li>Returns the validated {@link NonProfitOrg} entity</li>
     * </ol>
     *
     * @param id The UUID of the non-profit organization to retrieve
     * @param user The user requesting access to the organization
     * @return The {@link NonProfitOrg} entity if accessible by the user
     * @throws AccessDeniedException If the user doesn't have access to the organization
     */
    @Transactional
    NonProfitOrg getByIdAndUser(UUID id, User user) {
        AbstractCustomer abstractCustomer = accountService.findAccountWithCustomerByUser(user).getCustomer();
        if (isNotValidReferenceAbstractCustomer(id, abstractCustomer)) {
            throw new AccessDeniedException("User " + user.getUsername() + " is not associated with a non-profit " +
                    "organization with id " + id);
        }
        return (NonProfitOrg) abstractCustomer;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Implementation steps:
     * <ol>
     *     <li>Finds and validates the {@link NonProfitOrg} using {@link #findNonProfitOrgToChange}</li>
     *     <li>Updates the entity fields using {@link #updateNonProfitOrgDetails}</li>
     *     <li>Persists the updated entity and returns the detailed response model</li>
     * </ol>
     */
    @Override
    @Transactional
    public NonProfitOrgDetailsResponseModel updateNonProfitOrgDetailsById(UUID id, @Valid NonProfitOrgUpdateRequest updateRequest, User user) {
        log.info("Updating non-profit org details for ID: {} by user: {}", id, user.getUsername());
        NonProfitOrg nonProfitOrg = findNonProfitOrgToChange(id, user);

        updateNonProfitOrgDetails(nonProfitOrg, updateRequest);

        NonProfitOrg updatedNonProfitOrg = saveNonProfitOrg(nonProfitOrg);
        log.info("Successfully updated organization with ID: {}", updatedNonProfitOrg.getId());

        return nonProfitOrgDetailModelAssembler.toModel(updatedNonProfitOrg);
    }

    /**
     * Updates non-null fields of the non-profit organization entity.
     * <p>
     * Implementation steps:
     * <ol>
     *     <li>Updates customer details via {@link AbstractCustomerService}</li>
     *     <li>Updates business entity details via {@link BusinessEntityService}</li>
     * </ol>
     *
     * @param nonProfitOrg The non-profit organization entity to update
     * @param updateRequest The request containing fields to update
     */
    @Transactional
    void updateNonProfitOrgDetails(NonProfitOrg nonProfitOrg, @Valid NonProfitOrgUpdateRequest updateRequest) {
        abstractCustomerService.updateAbstractCustomerDetails(nonProfitOrg, updateRequest.customerDetails());
        businessEntityService.updateBusinessEntityFields(nonProfitOrg, updateRequest.businessEntityDetails());
    }

    /**
     * {@inheritDoc}
     * <p>
     * Implementation steps:
     * <ol>
     *     <li>Finds and validates the {@link NonProfitOrg} using {@link #findNonProfitOrgToChange}</li>
     *     <li>Detaches the organization from account associations via {@link AccountService}</li>
     *     <li>Removes the organization from the database</li>
     * </ol>
     */
    @Override
    @Transactional
    public void deleteNonProfitOrgById(UUID id, User user) {
        log.info("Deleting non-profit org details for ID: {} by user: {}", id, user.getUsername());
        NonProfitOrg nonProfitOrg = findNonProfitOrgToChange(id, user);
        accountService.detachCustomerFromAccount(nonProfitOrg);
        nonProfitOrgRepository.delete(nonProfitOrg);
    }

    /**
     * Finds a non-profit organization that the user is authorized to modify.
     * <p>
     * Implementation steps:
     * <ol>
     *     <li>Retrieves the user's account with its associated customer</li>
     *     <li>Verifies the user has suitable association type for modifications</li>
     *     <li>Validates that the customer is the requested non-profit organization</li>
     * </ol>
     *
     * @param id The UUID of the non-profit organization to find
     * @param user The user requesting access to modify the organization
     * @return The {@link NonProfitOrg} if the user has permission to modify it
     * @throws AccessDeniedException If the user doesn't have required permissions
     * @throws IllegalStateException If the account doesn't reference a valid organization
     */
    @Transactional
    NonProfitOrg findNonProfitOrgToChange(UUID id, User user) {
        Account account = accountService.findAccountWithCustomerByUser(user);

        if (accountService.isRepresentativeAssociationType(account)) {
            throw new AccessDeniedException("User " + user.getUsername() + " does not have permission to the " +
                    "non-profit org with id " + id);
        }

        if (isNotValidReferenceAbstractCustomer(id, account.getCustomer())) {
            throw new IllegalStateException("Account does not reference a valid non-profit org.");
        }

        return (NonProfitOrg) account.getCustomer();
    }

    /**
     * Checks if the abstract customer is not a valid non-profit organization with the expected ID.
     *
     * @param abstractCustomerId The expected customer ID
     * @param abstractCustomer The customer to validate
     * @return {@code true} if the customer is invalid or not a non-profit organization, {@code false} otherwise
     */
    @Transactional
    boolean isNotValidReferenceAbstractCustomer(UUID abstractCustomerId, AbstractCustomer abstractCustomer) {
        return abstractCustomer == null || !abstractCustomer.getId().equals(abstractCustomerId) || !(abstractCustomer instanceof NonProfitOrg);
    }

    /**
     * Finds a non-profit organization by its ID without user association validation.
     *
     * @param id The UUID of the non-profit organization to find
     * @return The {@link NonProfitOrg} entity
     * @throws EntityNotFoundException If no organization exists with the given ID
     */
    @Transactional
    NonProfitOrg findById(UUID id) {
        return nonProfitOrgRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Non-profit organization with ID: " + id + " not found"));
    }

    /**
     * {@inheritDoc}
     * <p>
     * Implementation steps:
     * <ol>
     *     <li>Retrieves the {@link NonProfitOrg} by ID using {@link #findById}</li>
     *     <li>Creates admin-level detailed response model via appropriate assembler</li>
     * </ol>
     */
    @Override
    @Transactional
    public NonProfitOrgDetailsResponseModel getAdminNonProfitOrgDetailsResponseModelById(UUID id) {
        log.info("Attempting to retrieve admin non-profit organization details for ID: {}", id);
        NonProfitOrg nonProfitOrg = findById(id);
        log.info("Retrieved admin non-profit organization details: {}", nonProfitOrg);
        NonProfitOrgDetailsResponseModel nonProfitOrgDetailsResponseModel =
                adminNonProfitOrgDetailsModelAssembler.toModel(nonProfitOrg);
        log.info("Successfully retrieved admin non-profit organization details response model: {}", nonProfitOrgDetailsResponseModel);
        return nonProfitOrgDetailsResponseModel;
    }
}