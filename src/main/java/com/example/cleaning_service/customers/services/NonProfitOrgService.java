package com.example.cleaning_service.customers.services;

import com.example.cleaning_service.commons.BusinessEntityService;
import com.example.cleaning_service.customers.assemblers.non_profit_org.NonProfitOrgDetailsResponseModelAssembler;
import com.example.cleaning_service.customers.assemblers.non_profit_org.NonProfitOrgResponseModelAssembler;
import com.example.cleaning_service.customers.dto.accounts.AccountRequest;
import com.example.cleaning_service.customers.dto.non_profit_org.NonProfitOrgDetailsResponseModel;
import com.example.cleaning_service.customers.dto.non_profit_org.NonProfitOrgRequest;
import com.example.cleaning_service.customers.dto.non_profit_org.NonProfitOrgResponseModel;
import com.example.cleaning_service.customers.dto.non_profit_org.NonProfitOrgUpdateRequest;
import com.example.cleaning_service.customers.entities.Account;
import com.example.cleaning_service.customers.entities.NonProfitOrg;
import com.example.cleaning_service.customers.enums.EAssociationType;
import com.example.cleaning_service.customers.mappers.NonProfitOrgMapper;
import com.example.cleaning_service.customers.repositories.NonProfitOrgRepository;
import com.example.cleaning_service.security.entities.user.User;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Service class responsible for managing Non-Profit Organization entities.
 * <p>
 * This service provides operations for creating, retrieving, updating, and deleting
 * non-profit organization information, ensuring data integrity and coordinating with related services.
 */
@Service
public class NonProfitOrgService {
    private static final Logger log = LoggerFactory.getLogger(NonProfitOrgService.class);
    private final NonProfitOrgRepository nonProfitOrgRepository;
    private final AccountService accountService;
    private final OrganizationDetailsService organizationDetailsService;
    private final AbstractCustomerService abstractCustomerService;
    private final BusinessEntityService businessEntityService;
    private final NonProfitOrgMapper nonProfitOrgMapper;
    private final NonProfitOrgResponseModelAssembler nonProfitOrgResponseModelAssembler;
    private final NonProfitOrgDetailsResponseModelAssembler nonProfitOrgDetailsResponseModelAssembler;
    private final CustomerService customerService;

    /**
     * Constructs a NonProfitOrgService with required dependencies.
     *
     * @param nonProfitOrgRepository Repository for non-profit organization persistence operations.
     * @param accountService Service for managing user-organization associations.
     * @param organizationDetailsService Service for managing organization-specific operations.
     * @param abstractCustomerService Service for managing customer-related operations.
     * @param businessEntityService Service for managing business entity operations.
     * @param nonProfitOrgMapper Mapper for converting between DTOs and entities.
     * @param nonProfitOrgResponseModelAssembler Assembler for basic non-profit organization response models.
     * @param nonProfitOrgDetailsResponseModelAssembler Assembler for detailed non-profit organization response models.
     */
    public NonProfitOrgService(
            NonProfitOrgRepository nonProfitOrgRepository,
            AccountService accountService,
            OrganizationDetailsService organizationDetailsService,
            AbstractCustomerService abstractCustomerService,
            BusinessEntityService businessEntityService,
            NonProfitOrgMapper nonProfitOrgMapper,
            NonProfitOrgResponseModelAssembler nonProfitOrgResponseModelAssembler,
            NonProfitOrgDetailsResponseModelAssembler nonProfitOrgDetailsResponseModelAssembler, CustomerService customerService) {

        this.nonProfitOrgRepository = nonProfitOrgRepository;
        this.accountService = accountService;
        this.organizationDetailsService = organizationDetailsService;
        this.abstractCustomerService = abstractCustomerService;
        this.businessEntityService = businessEntityService;
        this.nonProfitOrgMapper = nonProfitOrgMapper;
        this.nonProfitOrgResponseModelAssembler = nonProfitOrgResponseModelAssembler;
        this.nonProfitOrgDetailsResponseModelAssembler = nonProfitOrgDetailsResponseModelAssembler;
        this.customerService = customerService;
    }

    /**
     * Creates a new non-profit organization and associates it with the specified user.
     * <p>
     * This method performs the following operations:
     * 1. Retrieves the user's current account association.
     * 2. Creates and persists a new non-profit organization based on the provided request data.
     * 3. Determines the appropriate association type and primary status for the non-profit organization.
     * 4. Updates the user's account association with the newly created non-profit organization.
     * 5. Ensures that the updated account association references a valid non-profit organization.
     *
     * @param nonProfitOrgRequest The request containing non-profit organization details.
     * @param user The user to associate with the non-profit organization.
     * @return A {@link NonProfitOrgResponseModel} containing details of the created non-profit organization.
     * @throws IllegalStateException If the updated account association does not reference a valid non-profit organization.
     */
    @Transactional
    public NonProfitOrgResponseModel createProfitOrg(@Valid NonProfitOrgRequest nonProfitOrgRequest, User user) {
        log.info("Check duplicated fields");
        customerService.checkDuplicatedFields(
                nonProfitOrgRequest,
                nonProfitOrgRepository::existsByTaxId,
                nonProfitOrgRepository::existsByRegistrationNumber,
                nonProfitOrgRepository::existsByEmail
        );

        Account account = accountService.findByUser(user);

        NonProfitOrg nonProfitOrg = nonProfitOrgMapper.fromRequestToNonProfitOrg(nonProfitOrgRequest);
        NonProfitOrg savedNonProfitOrg = saveNonProfitOrg(nonProfitOrg);

        EAssociationType associationType = organizationDetailsService.getEAssociationTypeByIOrganization(savedNonProfitOrg);
        boolean isPrimary = organizationDetailsService.getIsPrimaryByIOrganization(savedNonProfitOrg);

        // Create account association
        AccountRequest accountRequest = new AccountRequest(
                savedNonProfitOrg, null, isPrimary, associationType
        );
        Account dbAccount = accountService.updateAccount(accountRequest, account);

        if(!(dbAccount.getCustomer() instanceof NonProfitOrg accountNonProfitOrg)) {
            throw new IllegalStateException("Account association does not reference a valid non-profit org.");
        }
        return nonProfitOrgResponseModelAssembler.toModel(accountNonProfitOrg);
    }

    /**
     * Saves a non-profit organization entity to the database.
     *
     * @param nonProfitOrg The non-profit organization entity to persist.
     * @return The saved non-profit organization entity with updated metadata.
     */
    @Transactional
    NonProfitOrg saveNonProfitOrg(NonProfitOrg nonProfitOrg) {
        return nonProfitOrgRepository.save(nonProfitOrg);
    }

    /**
     * Retrieves detailed non-profit organization information by ID for a specific user.
     * <p>
     * This method performs the following operations:
     * 1. Retrieves the non-profit organization entity using the provided ID.
     * 2. Verifies if the user is associated with the non-profit organization.
     * 3. Converts the non-profit organization entity into a detailed response model.
     *
     * @param id The UUID of the non-profit organization to retrieve.
     * @param user The user requesting the non-profit organization details.
     * @return A detailed response model containing non-profit organization information.
     * @throws IllegalStateException If the non-profit organization is not found or the user doesn't have access.
     */
    @Transactional
    public NonProfitOrgDetailsResponseModel getNonProfitOrgDetailsResponseModelById(UUID id, User user) {
        NonProfitOrg nonProfitOrg = getByIdAndUser(id, user);
        return nonProfitOrgDetailsResponseModelAssembler.toModel(nonProfitOrg);
    }

    /**
     * Retrieves a non-profit organization by its ID while verifying that the requesting user has access to it.
     * <p>
     * This method performs the following operations:
     * 1. Retrieves the non-profit organization entity using the provided ID.
     * 2. Checks if the user is associated with the non-profit organization through an account association.
     * 3. If the user lacks the required association, throws an {@code IllegalStateException}.
     *
     * @param id The UUID of the non-profit organization to retrieve.
     * @param user The user requesting access to the non-profit organization.
     * @return The non-profit organization entity if found and accessible by the user.
     * @throws IllegalStateException If the non-profit organization is not found or if the user does not have
     *                               the required association.
     */
    @Transactional
    NonProfitOrg getByIdAndUser(UUID id, User user) {
        NonProfitOrg nonProfitOrg = findById(id);
        if (accountService.isNotExistsAccountByUserAndCustomer(user, nonProfitOrg)) {
            throw new IllegalStateException("User " + user.getUsername() + " is not associated with a non-profit organization.");
        }
        return nonProfitOrg;
    }

    /**
     * Finds a non-profit organization by its ID.
     * <p>
     * This method performs the following operations:
     * 1. Attempts to retrieve the non-profit organization entity from the database using the provided ID.
     * 2. If the non-profit organization exists, it is returned.
     * 3. If no non-profit organization is found, an {@code IllegalStateException} is thrown.
     *
     * @param id The UUID of the non-profit organization to find.
     * @return The non-profit organization entity if found.
     * @throws EntityNotFoundException If no non-profit organization exists with the given ID.
     */
    @Transactional
    NonProfitOrg findById(UUID id) {
        return nonProfitOrgRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Non-profit org " + id + "not found."));
    }

    /**
     * Updates non-profit organization details based on the provided request.
     * <p>
     * This method performs the following operations:
     * 1. Retrieves the non-profit organization entity by its ID while ensuring the requesting user has access.
     * 2. Updates the organization's fields based on the provided update request.
     * 3. Persists the updated non-profit organization entity to the database.
     * 4. Converts the updated entity into a response model and returns it.
     *
     * @param id The UUID of the non-profit organization to update.
     * @param updateRequest The request containing fields to update.
     * @param user The user requesting the update.
     * @return A detailed response model containing the updated non-profit organization information.
     * @throws IllegalStateException If the non-profit organization is not found or the user doesn't have access.
     */
    @Transactional
    public NonProfitOrgDetailsResponseModel updateNonProfitOrgDetailsById(UUID id, @Valid NonProfitOrgUpdateRequest updateRequest, User user) {
        NonProfitOrg nonProfitOrg = getByIdAndUser(id, user);

        updateNonProfitOrgDetails(nonProfitOrg, updateRequest);
        NonProfitOrg updatedNonProfitOrg = saveNonProfitOrg(nonProfitOrg);
        return nonProfitOrgDetailsResponseModelAssembler.toModel(updatedNonProfitOrg);
    }

    /**
     * Updates only the non-null fields of the non-profit organization entity.
     * <p>
     * This method performs the following operations:
     * 1. Delegates the update of organization-specific details to {@code OrganizationDetailsService}.
     * 2. Delegates the update of customer-related details to {@code AbstractCustomerService}.
     * 3. Delegates the update of business entity-specific details to {@code BusinessEntityService}.
     *
     * @param nonProfitOrg The non-profit organization entity to update.
     * @param updateRequest The request containing fields to update.
     */
    @Transactional
    void updateNonProfitOrgDetails(NonProfitOrg nonProfitOrg, @Valid NonProfitOrgUpdateRequest updateRequest) {
        organizationDetailsService.updateOrganizationDetails(nonProfitOrg, updateRequest.organizationDetails());

        abstractCustomerService.updateAbstractCustomerDetails(nonProfitOrg, updateRequest.customerDetails());

        businessEntityService.updateBusinessEntityFields(nonProfitOrg, updateRequest.businessEntityDetails());
    }

    /**
     * Deletes a non-profit organization by its ID and ensures it is associated with the specified user.
     * <p>
     * This method performs the following operations:
     * 1. Retrieves the non-profit organization by its ID and verifies that it is associated with the given user.
     * 2. Detaches the non-profit organization from any account associations linked to the user.
     * 3. Deletes the non-profit organization from the database.
     *
     * @param id   The unique identifier of the non-profit organization to be deleted. Must not be {@code null}.
     * @param user The user requesting the deletion. Must not be {@code null}.
     */
    @Transactional
    public void deleteNonProfitOrgById(UUID id, User user) {
        NonProfitOrg nonProfitOrg = getByIdAndUser(id, user);
        accountService.detachCustomerFromAccount(nonProfitOrg);
        nonProfitOrgRepository.delete(nonProfitOrg);
    }
}
