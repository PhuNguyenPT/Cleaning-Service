package com.example.cleaning_service.customers.services;

import com.example.cleaning_service.commons.BusinessEntityService;
import com.example.cleaning_service.customers.assemblers.governments.GovernmentDetailsResponseModelAssembler;
import com.example.cleaning_service.customers.assemblers.governments.GovernmentResponseModelAssembler;
import com.example.cleaning_service.customers.dto.*;
import com.example.cleaning_service.customers.dto.governments.GovernmentDetailsResponseModel;
import com.example.cleaning_service.customers.dto.governments.GovernmentRequest;
import com.example.cleaning_service.customers.dto.governments.GovernmentResponseModel;
import com.example.cleaning_service.customers.dto.governments.GovernmentUpdateRequest;
import com.example.cleaning_service.customers.entities.AccountAssociation;
import com.example.cleaning_service.customers.entities.Government;
import com.example.cleaning_service.customers.enums.EAssociationType;
import com.example.cleaning_service.customers.mappers.GovernmentMapper;
import com.example.cleaning_service.customers.repositories.GovernmentRepository;
import com.example.cleaning_service.security.entities.user.User;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Service class responsible for managing Government entities.
 * <p>
 * This service provides operations for creating, retrieving, updating, and deleting
 * government information, ensuring data integrity and coordinating with related services.
 */
@Service
public class GovernmentService {
    private final GovernmentRepository governmentRepository;
    private final AccountAssociationService accountAssociationService;
    private final BusinessEntityService businessEntityService;
    private final AbstractCustomerService abstractCustomerService;
    private final OrganizationDetailsService organizationDetailsService;
    private final GovernmentMapper governmentMapper;
    private final GovernmentResponseModelAssembler governmentResponseModelAssembler;
    private final GovernmentDetailsResponseModelAssembler governmentDetailsResponseModelAssembler;

    /**
     * Constructs a GovernmentService with required dependencies.
     *
     * @param governmentRepository Repository for government entity persistence operations.
     * @param accountAssociationService Service for managing user-government associations.
     * @param businessEntityService Service for managing business entity operations.
     * @param abstractCustomerService Service for managing customer-related operations.
     * @param organizationDetailsService Service for managing organization-specific operations.
     * @param governmentMapper Mapper for converting between DTOs and government entities.
     * @param governmentResponseModelAssembler Assembler for basic government response models.
     * @param governmentDetailsResponseModelAssembler Assembler for detailed government response models.
     */
    public GovernmentService(
            GovernmentRepository governmentRepository,
            AccountAssociationService accountAssociationService,
            BusinessEntityService businessEntityService,
            AbstractCustomerService abstractCustomerService,
            OrganizationDetailsService organizationDetailsService,
            GovernmentMapper governmentMapper,
            GovernmentResponseModelAssembler governmentResponseModelAssembler,
            GovernmentDetailsResponseModelAssembler governmentDetailsResponseModelAssembler) {

        this.governmentRepository = governmentRepository;
        this.accountAssociationService = accountAssociationService;
        this.businessEntityService = businessEntityService;
        this.abstractCustomerService = abstractCustomerService;
        this.organizationDetailsService = organizationDetailsService;
        this.governmentMapper = governmentMapper;
        this.governmentResponseModelAssembler = governmentResponseModelAssembler;
        this.governmentDetailsResponseModelAssembler = governmentDetailsResponseModelAssembler;
    }

    /**
     * Creates a new government entity and associates it with the specified user.
     * <p>
     * This method performs the following operations:
     * 1. Retrieves the user's current account association.
     * 2. Creates and persists a new government entity based on the provided request data.
     * 3. Determines the appropriate association type and primary status for the government.
     * 4. Updates the user's account association with the newly created government.
     * 5. Ensures that the updated account association references a valid government.
     *
     * @param governmentRequest The request containing government entity details.
     * @param user The user to associate with the government entity.
     * @return A {@link GovernmentResponseModel} containing details of the created government entity.
     * @throws IllegalStateException If the updated account association does not reference a valid government entity.
     */
    @Transactional
    public GovernmentResponseModel createGovernment(@Valid GovernmentRequest governmentRequest, @NotNull User user) {
        AccountAssociation accountAssociation = accountAssociationService.findByUser(user);

        Government government = governmentMapper.fromGovernmentRequestToGovernment(governmentRequest);
        Government savedGovernment = saveGovernment(government);

        EAssociationType associationType = organizationDetailsService.getEAssociationTypeByIOrganization(savedGovernment);
        boolean isPrimary = organizationDetailsService.getIsPrimaryByIOrganization(savedGovernment);

        // Create account association
        AccountAssociationRequest accountAssociationRequest = new AccountAssociationRequest(
                savedGovernment, null, isPrimary, associationType
        );
        AccountAssociation dbAccountAssociation = accountAssociationService.updateAccountAssociation(accountAssociationRequest, accountAssociation);

        if (!(dbAccountAssociation.getCustomer() instanceof Government accountGovernment)) {
            throw new IllegalStateException("Account association does not reference a valid government.");
        }

        return governmentResponseModelAssembler.toModel(accountGovernment);
    }

    /**
     * Saves a government entity to the database.
     * <p>
     * This method persists the provided government entity, ensuring that any changes
     * or new records are stored with updated metadata.
     *
     * @param government The government entity to persist.
     * @return The saved government entity with updated metadata.
     */
    @Transactional
    Government saveGovernment(Government government) {
        return governmentRepository.save(government);
    }

    /**
     * Retrieves detailed government entity information by ID for a specific user.
     * <p>
     * This method performs the following operations:
     * 1. Retrieves the government entity using the provided ID.
     * 2. Verifies if the user is associated with the government entity.
     * 3. Converts the government entity into a detailed response model.
     *
     * @param id The UUID of the government entity to retrieve.
     * @param user The user requesting the government entity details.
     * @return A detailed response model containing government entity information.
     * @throws IllegalStateException If the government entity is not found or the user doesn't have access.
     */
    @Transactional
    public GovernmentDetailsResponseModel getGovernmentDetailsResponseModelById(UUID id, @NotNull User user) {
        Government dbGovernment = getByIdAndUser(id, user);
        return governmentDetailsResponseModelAssembler.toModel(dbGovernment);
    }

    /**
     * Retrieves a government entity by its ID while verifying that the requesting user has access to it.
     * <p>
     * This method performs the following operations:
     * 1. Retrieves the government entity using the provided ID.
     * 2. Checks if the user is associated with the government entity through an account association.
     * 3. If the user lacks the required association, throws an {@code IllegalStateException}.
     *
     * @param id The UUID of the government entity to retrieve.
     * @param user The user requesting access to the government entity.
     * @return The government entity if found and accessible by the user.
     * @throws IllegalStateException If the government entity is not found or if the user does not have
     *                               the required association.
     */
    @Transactional
    Government getByIdAndUser(UUID id, User user) {
        Government government = findById(id);
        if (accountAssociationService.isNotExistsAccountAssociationByUserAndCustomer(user, government)) {
            throw new IllegalStateException("User " + user.getUsername() + " is not associated with a government with id "
            + id);
        }
        return government;
    }

    /**
     * Finds a government entity by its ID.
     * <p>
     * This method performs the following operations:
     * 1. Attempts to retrieve the government entity from the database using the provided ID.
     * 2. If the government entity exists, it is returned.
     * 3. If no government entity is found, an {@code IllegalStateException} is thrown.
     *
     * @param id The UUID of the government entity to find.
     * @return The government entity if found.
     * @throws IllegalStateException If no government entity exists with the given ID.
     */
    @Transactional
    Government findById(UUID id) {
        return governmentRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Government with id " + id + " not found."));
    }

    /**
     * Updates government details based on the provided request.
     * <p>
     * This method performs the following operations:
     * 1. Retrieves the government entity by its ID while ensuring the requesting user has access.
     * 2. Updates the government's fields based on the provided update request.
     * 3. Persists the updated government entity to the database.
     * 4. Converts the updated entity into a response model and returns it.
     *
     * @param id The UUID of the government entity to update.
     * @param updateRequest The request containing fields to update.
     * @param user The user requesting the update.
     * @return A detailed response model containing the updated government information.
     * @throws IllegalStateException If the government entity is not found or the user doesn't have access.
     */
    @Transactional
    public GovernmentDetailsResponseModel updateCompanyDetailsById(UUID id, @Valid GovernmentUpdateRequest updateRequest, User user) {
        Government government = getByIdAndUser(id, user);

        updateGovernmentFields(government, updateRequest);
        Government updatedGovernment = saveGovernment(government);
        return governmentDetailsResponseModelAssembler.toModel(updatedGovernment);
    }

    /**
     * Updates only the non-null fields of the government entity.
     * <p>
     * This method performs the following operations:
     * 1. Checks if specific government attributes (e.g., contractor name, department name, tax exemption status)
     *    are provided in the request and updates them accordingly.
     * 2. Delegates the update of organization-specific details to {@code OrganizationDetailsService}.
     * 3. Delegates the update of customer-related details to {@code AbstractCustomerService}.
     * 4. Delegates the update of business entity-specific details to {@code BusinessEntityService}.
     *
     * @param government The government entity to update.
     * @param updateRequest The request containing fields to update.
     */
    @Transactional
    void updateGovernmentFields(Government government, @Valid GovernmentUpdateRequest updateRequest) {
        if (updateRequest.contractorName() != null) {
            government.setContractorName(updateRequest.contractorName());
        }
        if (updateRequest.departmentName() != null) {
            government.setDepartmentName(updateRequest.departmentName());
        }
        if (updateRequest.isTaxExempt() != null) {
            government.setTaxExempt(updateRequest.isTaxExempt());
        }
        if (updateRequest.requiresEmergencyCleaning() != null) {
            government.setRequiresEmergencyCleaning(updateRequest.requiresEmergencyCleaning());
        }

        organizationDetailsService.updateOrganizationDetails(government, updateRequest.organizationDetails());

        abstractCustomerService.updateAbstractCustomerDetails(government, updateRequest.customerDetails());

        businessEntityService.updateBusinessEntityFields(government, updateRequest.businessEntityDetails());
    }

    /**
     * Deletes a government entity by its ID and ensures it is associated with the specified user.
     * <p>
     * This method performs the following operations:
     * 1. Retrieves the government entity by its ID and verifies that it is associated with the given user.
     * 2. Detaches the government entity from any account associations linked to the user.
     * 3. Deletes the government entity from the database.
     *
     * @param id   The unique identifier of the government entity to be deleted. Must not be {@code null}.
     * @param user The user requesting the deletion. Must not be {@code null}.
     */
    @Transactional
    public void deleteGovernmentById(UUID id, User user) {
        Government dbGovernment = getByIdAndUser(id, user);
        accountAssociationService.detachCustomerFromAssociation(dbGovernment);
        governmentRepository.delete(dbGovernment);
    }
}
