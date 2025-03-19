package com.example.cleaning_service.customers.services;

import com.example.cleaning_service.commons.BusinessEntityService;
import com.example.cleaning_service.customers.assemblers.individuals.IndividualCustomerDetailsResponseModelAssembler;
import com.example.cleaning_service.customers.assemblers.individuals.IndividualCustomerResponseModelAssembler;
import com.example.cleaning_service.customers.dto.accounts.AccountRequest;
import com.example.cleaning_service.customers.dto.inidividuals.IndividualCustomerDetailsResponseModel;
import com.example.cleaning_service.customers.dto.inidividuals.IndividualCustomerRequest;
import com.example.cleaning_service.customers.dto.inidividuals.IndividualCustomerResponseModel;
import com.example.cleaning_service.customers.dto.inidividuals.IndividualCustomerUpdateRequest;
import com.example.cleaning_service.customers.entities.Account;
import com.example.cleaning_service.customers.entities.IndividualCustomer;
import com.example.cleaning_service.customers.enums.EAssociationType;
import com.example.cleaning_service.customers.mappers.IndividualCustomerMapper;
import com.example.cleaning_service.customers.repositories.IndividualCustomerRepository;
import com.example.cleaning_service.security.entities.user.User;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(IndividualCustomerService.class);
    private final IndividualCustomerRepository individualCustomerRepository;
    private final AccountService accountService;
    private final AbstractCustomerService abstractCustomerService;
    private final BusinessEntityService businessEntityService;
    private final OrganizationDetailsService organizationDetailsService;
    private final IndividualCustomerMapper individualCustomerMapper;
    private final IndividualCustomerResponseModelAssembler individualCustomerResponseModelAssembler;
    private final IndividualCustomerDetailsResponseModelAssembler individualCustomerDetailsResponseModelAssembler;
    private final CustomerService customerService;

    /**
     * Constructs an IndividualCustomerService with required dependencies.
     *
     * @param individualCustomerRepository Repository for individual customer entity persistence operations.
     * @param accountService Service for managing user-individual customer associations.
     * @param abstractCustomerService Service for managing common customer-related operations.
     * @param businessEntityService Service for managing business entity operations.
     * @param organizationDetailsService Service for managing organization-specific operations.
     * @param individualCustomerMapper Mapper for converting between DTOs and individual customer entities.
     * @param individualCustomerResponseModelAssembler Assembler for basic individual customer response models.
     * @param individualCustomerDetailsResponseModelAssembler Assembler for detailed individual customer response models.
     */
    public IndividualCustomerService(
            IndividualCustomerRepository individualCustomerRepository,
            AccountService accountService,
            AbstractCustomerService abstractCustomerService,
            BusinessEntityService businessEntityService,
            OrganizationDetailsService organizationDetailsService,
            IndividualCustomerMapper individualCustomerMapper,
            IndividualCustomerResponseModelAssembler individualCustomerResponseModelAssembler,
            IndividualCustomerDetailsResponseModelAssembler individualCustomerDetailsResponseModelAssembler, CustomerService customerService) {

        this.individualCustomerRepository = individualCustomerRepository;
        this.accountService = accountService;
        this.abstractCustomerService = abstractCustomerService;
        this.businessEntityService = businessEntityService;
        this.organizationDetailsService = organizationDetailsService;
        this.individualCustomerMapper = individualCustomerMapper;
        this.individualCustomerResponseModelAssembler = individualCustomerResponseModelAssembler;
        this.individualCustomerDetailsResponseModelAssembler = individualCustomerDetailsResponseModelAssembler;
        this.customerService = customerService;
    }

    /**
     * Creates a new individual customer and associates it with the specified user.
     * <p>
     * This method performs the following operations:
     * 1. Retrieves the user's current account association.
     * 2. Creates and persists a new individual customer based on the provided request data.
     * 3. Determines the appropriate association type and primary status for the individual customer.
     * 4. Updates the user's account association with the newly created individual customer.
     * 5. Ensures that the updated account association references a valid individual customer.
     *
     * @param individualCustomerRequest The request containing individual customer details.
     * @param user The user to associate with the individual customer.
     * @return A {@link IndividualCustomerResponseModel} containing details of the created individual customer.
     * @throws IllegalStateException If the updated account association does not reference a valid individual customer.
     */
    @Transactional
    public IndividualCustomerResponseModel createIndividualCustomer(@Valid IndividualCustomerRequest individualCustomerRequest, User user) {
        log.info("Check duplicated fields");
        customerService.checkDuplicatedFields(
                individualCustomerRequest,
                individualCustomerRepository::existsByTaxId,
                individualCustomerRepository::existsByRegistrationNumber,
                individualCustomerRepository::existsByEmail
        );
        log.info("Attempting to create an individual customer for user: {}", user.getUsername());
        Account account = accountService.findByUser(user);

        IndividualCustomer individualCustomer = individualCustomerMapper.fromRequestToCustomer(individualCustomerRequest);
        IndividualCustomer savedIndividualCustomer = saveIndividualCustomer(individualCustomer);
        log.info("Individual customer {} saved successfully with ID: {}", savedIndividualCustomer.getName(), savedIndividualCustomer.getId());

        EAssociationType associationType = organizationDetailsService.getEAssociationTypeByIOrganization(savedIndividualCustomer);
        boolean isPrimary = organizationDetailsService.getIsPrimaryByIOrganization(savedIndividualCustomer);

        // Create account association
        AccountRequest accountRequest = new AccountRequest(
                savedIndividualCustomer, null, isPrimary, associationType
        );
        Account dbAccount = accountService.updateAccount(accountRequest, account);

        if (!(dbAccount.getCustomer() instanceof IndividualCustomer accountCustomer)) {
            throw new IllegalStateException("Account association does not reference a valid individual.");
        }

        IndividualCustomerResponseModel individualCustomerResponseModel = individualCustomerResponseModelAssembler.toModel(accountCustomer);
        log.info("Successfully created individual customer response: {}", individualCustomerResponseModel);
        return individualCustomerResponseModel;
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
        log.info("Retrieving individual details for ID: {} by user: {}", id, user.getUsername());
        IndividualCustomer individualCustomer = getByIdAndUser(id, user);
        IndividualCustomerDetailsResponseModel individualCustomerDetailsResponseModel =
                individualCustomerDetailsResponseModelAssembler.toModel(individualCustomer);
        log.info("Retrieved individual details: {}", individualCustomerDetailsResponseModel);
        return individualCustomerDetailsResponseModel;
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
        if (accountService.isNotExistsAccountByUserAndCustomer(user, individualCustomer)) {
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
     * @throws EntityNotFoundException If no individual customer exists with the given ID.
     */
    @Transactional
    IndividualCustomer findById(UUID id) {
        return individualCustomerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Individual customer with id " + id + " does not exist."));
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
     * Deletes an individual customer by its ID and ensures it is associated with the specified user.
     * <p>
     * This method performs the following operations:
     * 1. Retrieves the individual customer by its ID and verifies that it is associated with the given user.
     * 2. Detaches the individual customer from any account associations linked to the user.
     * 3. Deletes the individual customer from the database.
     *
     * @param id   The unique identifier of the individual customer to be deleted. Must not be {@code null}.
     * @param user The user requesting the deletion. Must not be {@code null}.
     */
    @Transactional
    public void deleteIndividualCustomerById(UUID id, User user) {
        IndividualCustomer individualCustomer = getByIdAndUser(id, user);
        accountService.detachCustomerFromAccount(individualCustomer);
        individualCustomerRepository.delete(individualCustomer);
    }
}