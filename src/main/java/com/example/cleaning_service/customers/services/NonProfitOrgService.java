package com.example.cleaning_service.customers.services;

import com.example.cleaning_service.commons.BusinessEntityService;
import com.example.cleaning_service.customers.assemblers.non_profit_org.NonProfitOrgDetailsResponseModelAssembler;
import com.example.cleaning_service.customers.assemblers.non_profit_org.NonProfitOrgResponseModelAssembler;
import com.example.cleaning_service.customers.dto.AccountAssociationRequest;
import com.example.cleaning_service.customers.dto.non_profit_org.NonProfitOrgDetailsResponseModel;
import com.example.cleaning_service.customers.dto.non_profit_org.NonProfitOrgRequest;
import com.example.cleaning_service.customers.dto.non_profit_org.NonProfitOrgResponseModel;
import com.example.cleaning_service.customers.dto.non_profit_org.NonProfitOrgUpdateRequest;
import com.example.cleaning_service.customers.entities.AccountAssociation;
import com.example.cleaning_service.customers.entities.NonProfitOrg;
import com.example.cleaning_service.customers.enums.EAssociationType;
import com.example.cleaning_service.customers.mappers.NonProfitOrgMapper;
import com.example.cleaning_service.customers.repositories.NonProfitOrgRepository;
import com.example.cleaning_service.security.entities.user.User;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
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
    private final NonProfitOrgRepository nonProfitOrgRepository;
    private final AccountAssociationService accountAssociationService;
    private final OrganizationDetailsService organizationDetailsService;
    private final AbstractCustomerService abstractCustomerService;
    private final BusinessEntityService businessEntityService;
    private final NonProfitOrgMapper nonProfitOrgMapper;
    private final NonProfitOrgResponseModelAssembler nonProfitOrgResponseModelAssembler;
    private final NonProfitOrgDetailsResponseModelAssembler nonProfitOrgDetailsResponseModelAssembler;

    /**
     * Constructs a NonProfitOrgService with required dependencies.
     *
     * @param nonProfitOrgRepository Repository for non-profit organization persistence operations.
     * @param accountAssociationService Service for managing user-organization associations.
     * @param organizationDetailsService Service for managing organization-specific operations.
     * @param abstractCustomerService Service for managing customer-related operations.
     * @param businessEntityService Service for managing business entity operations.
     * @param nonProfitOrgMapper Mapper for converting between DTOs and entities.
     * @param nonProfitOrgResponseModelAssembler Assembler for basic non-profit organization response models.
     * @param nonProfitOrgDetailsResponseModelAssembler Assembler for detailed non-profit organization response models.
     */
    public NonProfitOrgService(
            NonProfitOrgRepository nonProfitOrgRepository,
            AccountAssociationService accountAssociationService,
            OrganizationDetailsService organizationDetailsService,
            AbstractCustomerService abstractCustomerService,
            BusinessEntityService businessEntityService,
            NonProfitOrgMapper nonProfitOrgMapper,
            NonProfitOrgResponseModelAssembler nonProfitOrgResponseModelAssembler,
            NonProfitOrgDetailsResponseModelAssembler nonProfitOrgDetailsResponseModelAssembler) {

        this.nonProfitOrgRepository = nonProfitOrgRepository;
        this.accountAssociationService = accountAssociationService;
        this.organizationDetailsService = organizationDetailsService;
        this.abstractCustomerService = abstractCustomerService;
        this.businessEntityService = businessEntityService;
        this.nonProfitOrgMapper = nonProfitOrgMapper;
        this.nonProfitOrgResponseModelAssembler = nonProfitOrgResponseModelAssembler;
        this.nonProfitOrgDetailsResponseModelAssembler = nonProfitOrgDetailsResponseModelAssembler;
    }

    /**
     * Creates a new non-profit organization and associates it with the specified user.
     * <p>
     * This method performs the following operations:
     * 1. Checks if the user is already associated with another account.
     * 2. Creates and persists a new non-profit organization based on the request data.
     * 3. Determines the correct association type and primary status.
     * 4. Creates an association between the user and the non-profit organization.
     *
     * @param nonProfitOrgRequest The request containing non-profit organization details.
     * @param user The user to associate with the non-profit organization.
     * @return A response model containing the created non-profit organization information.
     * @throws IllegalStateException If the user is already associated with an account.
     */
    @Transactional
    public NonProfitOrgResponseModel createProfitOrg(@Valid NonProfitOrgRequest nonProfitOrgRequest, User user) {
        if (accountAssociationService.isExistsAccountAssociationByUser(user)) {
            throw new IllegalStateException("User " + user.getUsername() + " is already associated with an account.");
        }

        NonProfitOrg nonProfitOrg = nonProfitOrgMapper.fromRequestToNonProfitOrg(nonProfitOrgRequest);
        NonProfitOrg savedNonProfitOrg = saveNonProfitOrg(nonProfitOrg);

        EAssociationType associationType = organizationDetailsService.getEAssociationTypeByIOrganization(savedNonProfitOrg);
        boolean isPrimary = organizationDetailsService.getIsPrimaryByIOrganization(savedNonProfitOrg);

        // Create account association
        AccountAssociationRequest accountAssociationRequest = new AccountAssociationRequest(
                user, savedNonProfitOrg, null, isPrimary, associationType
        );
        AccountAssociation accountAssociation = accountAssociationService.createAccountAssociation(accountAssociationRequest);

        if(!(accountAssociation.getCustomer() instanceof NonProfitOrg accountNonProfitOrg)) {
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
        if (!(accountAssociationService.isNotExistsAccountAssociationByUserAndCustomer(user, nonProfitOrg))) {
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
     * @throws IllegalStateException If no non-profit organization exists with the given ID.
     */
    @Transactional
    NonProfitOrg findById(UUID id) {
        return nonProfitOrgRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Non-profit org " + id + "not found."));
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

        businessEntityService.updateBusinessEntityFields(nonProfitOrg, updateRequest.businessEntityRequest());
    }

    /**
     * Deletes a non-profit organization by its ID.
     * <p>
     * This method performs the following operations:
     * 1. Verifies the non-profit organization exists and the user has access to it.
     * 2. Detaches the organization from any user associations.
     * 3. Deletes the non-profit organization entity.
     *
     * @param id The UUID of the non-profit organization to delete.
     * @param user The user requesting the deletion.
     * @throws IllegalStateException If the organization is not found or the user doesn't have access.
     */
    @Transactional
    public void deleteNonProfitOrgById(UUID id, User user) {
        NonProfitOrg nonProfitOrg = getByIdAndUser(id, user);
        accountAssociationService.detachCustomerFromAssociation(nonProfitOrg);
        nonProfitOrgRepository.delete(nonProfitOrg);
    }
}
