package com.example.cleaning_service.customers.services;

import com.example.cleaning_service.commons.BusinessEntityService;
import com.example.cleaning_service.customers.assemblers.individuals.IndividualCustomerDetailsResponseModelAssembler;
import com.example.cleaning_service.customers.assemblers.individuals.IndividualCustomerResponseModelAssembler;
import com.example.cleaning_service.customers.dto.AccountAssociationRequest;
import com.example.cleaning_service.customers.dto.inidividuals.IndividualCustomerDetailsResponseModel;
import com.example.cleaning_service.customers.dto.inidividuals.IndividualCustomerRequest;
import com.example.cleaning_service.customers.dto.inidividuals.IndividualCustomerResponseModel;
import com.example.cleaning_service.customers.dto.inidividuals.IndividualCustomerUpdateRequest;
import com.example.cleaning_service.customers.entities.AccountAssociation;
import com.example.cleaning_service.customers.entities.IndividualCustomer;
import com.example.cleaning_service.customers.enums.EAssociationType;
import com.example.cleaning_service.customers.mappers.IndividualCustomerMapper;
import com.example.cleaning_service.customers.repositories.IndividualCustomerRepository;
import com.example.cleaning_service.security.entities.user.User;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Service class responsible for managing Individual Customer entities.
 * <p>
 * This service provides operations for creating, retrieving, updating, and deleting
 * individual customer information, ensuring data integrity and coordinating with related services.
 */
@Service
public class IndividualCustomerService {

    private final IndividualCustomerRepository individualCustomerRepository;
    private final AccountAssociationService accountAssociationService;
    private final AbstractCustomerService abstractCustomerService;
    private final BusinessEntityService businessEntityService;
    private final OrganizationDetailsService organizationDetailsService;
    private final IndividualCustomerMapper individualCustomerMapper;
    private final IndividualCustomerResponseModelAssembler individualCustomerResponseModelAssembler;
    private final IndividualCustomerDetailsResponseModelAssembler individualCustomerDetailsResponseModelAssembler;

    /**
     * Constructs an IndividualCustomerService with required dependencies.
     *
     * @param individualCustomerRepository Repository for individual customer entity persistence operations.
     * @param accountAssociationService Service for managing user-individual customer associations.
     * @param abstractCustomerService Service for managing common customer-related operations.
     * @param businessEntityService Service for managing business entity operations.
     * @param organizationDetailsService Service for managing organization-specific operations.
     * @param individualCustomerMapper Mapper for converting between DTOs and individual customer entities.
     * @param individualCustomerResponseModelAssembler Assembler for basic individual customer response models.
     * @param individualCustomerDetailsResponseModelAssembler Assembler for detailed individual customer response models.
     */
    public IndividualCustomerService(
            IndividualCustomerRepository individualCustomerRepository,
            AccountAssociationService accountAssociationService,
            AbstractCustomerService abstractCustomerService,
            BusinessEntityService businessEntityService,
            OrganizationDetailsService organizationDetailsService,
            IndividualCustomerMapper individualCustomerMapper,
            IndividualCustomerResponseModelAssembler individualCustomerResponseModelAssembler,
            IndividualCustomerDetailsResponseModelAssembler individualCustomerDetailsResponseModelAssembler) {

        this.individualCustomerRepository = individualCustomerRepository;
        this.accountAssociationService = accountAssociationService;
        this.abstractCustomerService = abstractCustomerService;
        this.businessEntityService = businessEntityService;
        this.organizationDetailsService = organizationDetailsService;
        this.individualCustomerMapper = individualCustomerMapper;
        this.individualCustomerResponseModelAssembler = individualCustomerResponseModelAssembler;
        this.individualCustomerDetailsResponseModelAssembler = individualCustomerDetailsResponseModelAssembler;
    }

    /**
     * Creates a new individual customer and associates it with the specified user.
     * <p>
     * This method performs the following operations:
     * 1. Checks if the user is already associated with another account.
     * 2. Creates and persists a new individual customer based on the request data.
     * 3. Determines the correct association type and primary status.
     * 4. Creates an association between the user and the individual customer.
     *
     * @param individualCustomerRequest The request containing individual customer details.
     * @param user The user to associate with the individual customer.
     * @return A response model containing the created individual customer information.
     * @throws IllegalStateException If the user is already associated with an account.
     */
    @Transactional
    public IndividualCustomerResponseModel createIndividualCustomer(@Valid IndividualCustomerRequest individualCustomerRequest, User user) {
        if (accountAssociationService.isExistsAccountAssociationByUser(user)) {
            throw new IllegalStateException("User " + user.getUsername() + " is already associated with an account.");
        }

        IndividualCustomer individualCustomer = individualCustomerMapper.fromRequestToCustomer(individualCustomerRequest);
        IndividualCustomer savedIndividualCustomer = saveIndividualCustomer(individualCustomer);

        EAssociationType associationType = organizationDetailsService.getEAssociationTypeByIOrganization(savedIndividualCustomer);
        boolean isPrimary = organizationDetailsService.getIsPrimaryByIOrganization(savedIndividualCustomer);

        // Create account association
        AccountAssociationRequest accountAssociationRequest = new AccountAssociationRequest(
                user, savedIndividualCustomer, null, isPrimary, associationType
        );
        AccountAssociation dbAccountAssociation = accountAssociationService.createAccountAssociation(accountAssociationRequest);

        if (!(dbAccountAssociation.getCustomer() instanceof IndividualCustomer accountCustomer)) {
            throw new IllegalStateException("Account association does not reference a valid individual.");
        }

        return individualCustomerResponseModelAssembler.toModel(accountCustomer);
    }

    /**
     * Saves an individual customer entity to the database.
     * <p>
     * This method persists the provided individual customer entity and returns the saved
     * instance with updated metadata.
     *
     * @param individualCustomer The individual customer entity to persist.
     * @return The saved individual customer entity with updated metadata.
     */
    @Transactional
    IndividualCustomer saveIndividualCustomer(IndividualCustomer individualCustomer) {
        return individualCustomerRepository.save(individualCustomer);
    }

    /**
     * Retrieves detailed individual customer information by ID for a specific user.
     * <p>
     * This method performs the following operations:
     * 1. Retrieves the individual customer entity using the provided ID.
     * 2. Verifies if the user is associated with the individual customer.
     * 3. Converts the individual customer entity into a detailed response model.
     *
     * @param id The UUID of the individual customer to retrieve.
     * @param user The user requesting the customer details.
     * @return A detailed response model containing individual customer information.
     * @throws IllegalStateException If the individual customer is not found or the user doesn't have access.
     */
    @Transactional
    public IndividualCustomerDetailsResponseModel getIndividualCustomerDetailsById(UUID id, User user) {
        IndividualCustomer individualCustomer = getByIdAndUser(id, user);
        return individualCustomerDetailsResponseModelAssembler.toModel(individualCustomer);
    }

    /**
     * Retrieves an individual customer by its ID while verifying that the requesting user has access to it.
     * <p>
     * This method performs the following operations:
     * 1. Retrieves the individual customer entity using the provided ID.
     * 2. Checks if the user is associated with the individual customer through an account association.
     * 3. If the user lacks the required association, throws an {@code IllegalStateException}.
     *
     * @param id The UUID of the individual customer to retrieve.
     * @param user The user requesting access to the individual customer.
     * @return The individual customer entity if found and accessible by the user.
     * @throws IllegalStateException If the user does not have the required association.
     */
    @Transactional
    IndividualCustomer getByIdAndUser(UUID id, User user) {
        IndividualCustomer individualCustomer = findById(id);
        if (!(accountAssociationService.isNotExistsAccountAssociationByUserAndCustomer(user, individualCustomer))) {
            throw new IllegalStateException("User " + user.getUsername() + " is not associated with an individual customer with id " + id);
        }
        return individualCustomer;
    }

    /**
     * Finds an individual customer by its ID.
     * <p>
     * This method performs the following operations:
     * 1. Attempts to retrieve the individual customer entity from the database using the provided ID.
     * 2. If the individual customer exists, it is returned.
     * 3. If no individual customer is found, an {@code IllegalStateException} is thrown.
     *
     * @param id The UUID of the individual customer to find.
     * @return The individual customer entity if found.
     * @throws IllegalStateException If no individual customer exists with the given ID.
     */
    @Transactional
    IndividualCustomer findById(UUID id) {
        return individualCustomerRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Individual customer with id " + id + " does not exist."));
    }

    /**
     * Updates individual customer details based on the provided request.
     * <p>
     * This method performs the following operations:
     * 1. Retrieves the individual customer entity by its ID while ensuring the requesting user has access.
     * 2. Updates the customer's fields based on the provided update request.
     * 3. Persists the updated customer entity to the database.
     * 4. Converts the updated entity into a response model and returns it.
     *
     * @param id The UUID of the individual customer to update.
     * @param updateRequest The request containing fields to update.
     * @param user The user requesting the update.
     * @return A detailed response model containing the updated individual customer information.
     * @throws IllegalStateException If the individual customer is not found or the user doesn't have access.
     */
    @Transactional
    public IndividualCustomerDetailsResponseModel updateIndividualCustomerDetailsById(UUID id, @Valid IndividualCustomerUpdateRequest updateRequest, User user) {
        IndividualCustomer individualCustomer = getByIdAndUser(id, user);

        updateCustomerFields(individualCustomer, updateRequest);
        IndividualCustomer updatedIndividualCustomer = saveIndividualCustomer(individualCustomer);
        return individualCustomerDetailsResponseModelAssembler.toModel(updatedIndividualCustomer);
    }

    /**
     * Updates only the non-null fields of the individual customer entity.
     * <p>
     * This method performs the following operations:
     * 1. Delegates the update of organization-specific details to {@code OrganizationDetailsService}.
     * 2. Delegates the update of customer-related details to {@code AbstractCustomerService}.
     * 3. Delegates the update of business entity-specific details to {@code BusinessEntityService}.
     *
     * @param individualCustomer The individual customer entity to update.
     * @param updateRequest The request containing fields to update.
     */
    @Transactional
    void updateCustomerFields(IndividualCustomer individualCustomer, @Valid IndividualCustomerUpdateRequest updateRequest) {
        organizationDetailsService.updateOrganizationDetails(individualCustomer, updateRequest.organizationDetails());

        abstractCustomerService.updateAbstractCustomerDetails(individualCustomer, updateRequest.customerDetails());

        businessEntityService.updateBusinessEntityFields(individualCustomer, updateRequest.businessEntityDetails());
    }

    /**
     * Deletes an individual customer by its ID.
     * <p>
     * This method performs the following operations:
     * 1. Verifies the individual customer exists and the user has access to it.
     * 2. Detaches the individual customer from any user associations.
     * 3. Deletes the individual customer entity.
     *
     * @param id The UUID of the individual customer to delete.
     * @param user The user requesting the deletion.
     * @throws IllegalStateException If the individual customer is not found or the user doesn't have access.
     */
    @Transactional
    public void deleteIndividualCustomerById(UUID id, User user) {
        IndividualCustomer individualCustomer = getByIdAndUser(id, user);
        accountAssociationService.detachCustomerFromAssociation(individualCustomer);
        individualCustomerRepository.delete(individualCustomer);
    }
}