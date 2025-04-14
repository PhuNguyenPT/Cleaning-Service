package com.example.cleaning_service.customers.services.impl;

import com.example.cleaning_service.commons.BusinessEntityService;
import com.example.cleaning_service.customers.assemblers.governments.AdminGovernmentDetailsModelAssembler;
import com.example.cleaning_service.customers.assemblers.governments.GovernmentDetailsModelAssembler;
import com.example.cleaning_service.customers.assemblers.governments.GovernmentModelAssembler;
import com.example.cleaning_service.customers.dto.accounts.AccountRequest;
import com.example.cleaning_service.customers.dto.governments.GovernmentDetailsResponseModel;
import com.example.cleaning_service.customers.dto.governments.GovernmentRequest;
import com.example.cleaning_service.customers.dto.governments.GovernmentResponseModel;
import com.example.cleaning_service.customers.dto.governments.GovernmentUpdateRequest;
import com.example.cleaning_service.customers.entities.AbstractCustomer;
import com.example.cleaning_service.customers.entities.Account;
import com.example.cleaning_service.customers.entities.Government;
import com.example.cleaning_service.customers.enums.EAssociationType;
import com.example.cleaning_service.customers.mappers.GovernmentMapper;
import com.example.cleaning_service.customers.repositories.GovernmentRepository;
import com.example.cleaning_service.customers.services.*;
import com.example.cleaning_service.security.entities.user.User;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Service class responsible for managing Government entities.
 * <p>
 * This service provides operations for creating, retrieving, updating, and deleting
 * government information, ensuring data integrity and coordinating with related services.
 */
@Service
public class GovernmentServiceImpl implements GovernmentService {
    private static final Logger log = LoggerFactory.getLogger(GovernmentServiceImpl.class);
    private final GovernmentRepository governmentRepository;
    private final AccountService accountService;
    private final BusinessEntityService businessEntityService;
    private final AbstractCustomerService abstractCustomerService;
    private final OrganizationDetailsService organizationDetailsService;
    private final CustomerService customerService;


    private final GovernmentModelAssembler governmentModelAssembler;
    private final GovernmentDetailsModelAssembler governmentDetailsModelAssembler;

    private final GovernmentMapper governmentMapper;
    private final AdminGovernmentDetailsModelAssembler adminGovernmentDetailsModelAssembler;

    public GovernmentServiceImpl(
            GovernmentRepository governmentRepository,
            AccountService accountService,
            BusinessEntityService businessEntityService,
            AbstractCustomerService abstractCustomerService,
            OrganizationDetailsService organizationDetailsService,
            CustomerService customerService,

            GovernmentModelAssembler governmentModelAssembler,
            GovernmentDetailsModelAssembler governmentDetailsModelAssembler,

            GovernmentMapper governmentMapper,
            AdminGovernmentDetailsModelAssembler adminGovernmentDetailsModelAssembler) {

        this.governmentRepository = governmentRepository;

        this.accountService = accountService;
        this.businessEntityService = businessEntityService;
        this.abstractCustomerService = abstractCustomerService;
        this.organizationDetailsService = organizationDetailsService;
        this.customerService = customerService;

        this.governmentModelAssembler = governmentModelAssembler;
        this.governmentDetailsModelAssembler = governmentDetailsModelAssembler;

        this.governmentMapper = governmentMapper;
        this.adminGovernmentDetailsModelAssembler = adminGovernmentDetailsModelAssembler;
    }

    @Override
    @Transactional
    public GovernmentResponseModel createGovernment(@Valid GovernmentRequest governmentRequest, User user) {
        log.info("Check duplicated fields");
        customerService.checkDuplicatedFields(
                governmentRequest,
                governmentRepository::existsByTaxId,
                governmentRepository::existsByRegistrationNumber,
                governmentRepository::existsByEmail
        );

        log.info("Attempting to create a government for user: {}", user.getUsername());
        accountService.checkAccountReferenceCustomer(user);

        Government government = governmentMapper.fromGovernmentRequestToGovernment(governmentRequest);
        Government savedGovernment = saveGovernment(government);
        log.info("Government {} saved successfully with ID: {}", savedGovernment.getName(), savedGovernment.getId());

        EAssociationType associationType = organizationDetailsService.getEAssociationTypeByIOrganization(savedGovernment);
        boolean isPrimary = organizationDetailsService.getIsPrimaryByIOrganization(savedGovernment);

        // Create account association
        AccountRequest accountRequest = new AccountRequest(
                user, savedGovernment, null, isPrimary, associationType
        );
        Account account = accountService.handleCustomerCreation(accountRequest);

        if (isNotValidReferenceAbstractCustomer(account.getCustomer().getId(), account.getCustomer())) {
            throw new IllegalStateException("Account association does not reference a valid government.");
        }

        GovernmentResponseModel governmentResponseModel = governmentModelAssembler.toModel((Government) account.getCustomer());
        log.info("Successfully created government response: {}", governmentResponseModel);

        return governmentResponseModel;
    }

    /**
     * Saves a government entity to the database.
     * <p>
     * This method persists the provided government entity, ensuring that any changes
     * or new records are stored with updated metadata.
     *
     * @param government The government entity to persist.
     * @return The saved {@link Government} entity with updated metadata.
     */
    @Transactional
    Government saveGovernment(Government government) {
        return governmentRepository.save(government);
    }

    @Override
    @Transactional
    public GovernmentDetailsResponseModel getGovernmentDetailsResponseModelById(UUID id, User user) {
        Government dbGovernment = getByIdAndUser(id, user);
        return governmentDetailsModelAssembler.toModel(dbGovernment);
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
     * @return The {@link Government} entity if found and accessible by the user.
     * @throws AccessDeniedException If the government entity is not found or if the user does not have
     *                               the required association.
     */
    @Transactional
    Government getByIdAndUser(UUID id, User user) {
        AbstractCustomer abstractCustomer = accountService.findAccountWithCustomerByUser(user).getCustomer();
        if (isNotValidReferenceAbstractCustomer(id, abstractCustomer)) {
            throw new AccessDeniedException("User " + user.getUsername() + " is not associated with a government with id "
            + id);
        }
        return (Government) abstractCustomer;
    }

    @Override
    @Transactional
    public GovernmentDetailsResponseModel updateCompanyDetailsById(UUID id, @Valid GovernmentUpdateRequest updateRequest, User user) {
        log.info("Updating government details for ID: {} by user: {}", id, user);
        Government government = findGovernmentToChange(id, user);
        updateGovernmentFields(government, updateRequest);
        Government updatedGovernment = saveGovernment(government);
        log.info("Successfully updated company with ID: {}", updatedGovernment.getId());

        return governmentDetailsModelAssembler.toModel(updatedGovernment);
    }

    /**
     * Updates only the non-null fields of the government entity.
     * <p>
     * This method performs the following operations:
     * <ol>
     * <li>Checks if specific government attributes (e.g., contractor name, department name, tax exemption status)
     *  are provided in the request and updates them accordingly</li>
     * <li> Delegates the update of organization-specific details to {@code OrganizationDetailsService}</li>
     * <li>Delegates the update of customer-related details to {@code AbstractCustomerService}</li>
     * <li> Delegates the update of business entity-specific details to {@code BusinessEntityService}</li>
     * </ol>
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

    @Override
    @Transactional
    public void deleteGovernmentById(UUID id, User user) {
        log.info("Deleting government details for ID: {} by user: {}", id, user);
        Government dbGovernment = findGovernmentToChange(id, user);
        accountService.detachCustomerFromAccount(dbGovernment);
        governmentRepository.delete(dbGovernment);
    }

    @Transactional
    Government findGovernmentToChange(UUID id, User user) {
        Account account = accountService.findAccountWithCustomerByUser(user);

        if (accountService.isRepresentativeAssociationType(account)) {
            throw new AccessDeniedException("User " + user.getUsername() + " does not have permission to the " +
                    "government with id " + id);
        }

        if (isNotValidReferenceAbstractCustomer(id, account.getCustomer())) {
            throw new IllegalStateException("Account does not reference a valid government.");
        }
        return (Government) account.getCustomer();
    }

    /**
     * Retrieves a company by its ID.
     * <p>
     * This method performs a direct database lookup for a government entity.
     *
     * @param id The UUID of the government to retrieve
     * @return The {@link Government} entity if found
     * @throws EntityNotFoundException If no government exists with the given ID
     */
    @Transactional
    Government findById(UUID id) {
        return governmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Government with id " + id + " not found"));
    }

    /**
     * Checks if the provided customer is not a valid reference to the government with the given ID.
     * <p>
     * This method verifies that:
     * <ol>
     * <li>The customer's ID matches the provided ID</li>
     * <li>The customer is an instance of Government</li>
     * </ol>
     *
     * @param id The UUID to compare against the customer's ID
     * @param abstractCustomer The customer to validate
     * @return {@code true} if the customer is not a valid reference to the government, {@code false} otherwise
     */
    @Transactional
    boolean isNotValidReferenceAbstractCustomer(UUID id, AbstractCustomer abstractCustomer) {
        return abstractCustomer == null || !abstractCustomer.getId().equals(id) || !(abstractCustomer instanceof Government);
    }

    @Override
    @Transactional
    public GovernmentDetailsResponseModel getAdminGovernmentDetailsResponseModelById(UUID id) {
        log.info("Attempting to retrieve admin government with ID: {}", id);
        Government government = findById(id);
        log.info("Retrieved government with ID: {}", id);
        GovernmentDetailsResponseModel governmentDetailsResponseModel = adminGovernmentDetailsModelAssembler.toModel(government);
        log.info("Successfully retrieved admin government details response model: {}", governmentDetailsResponseModel);
        return governmentDetailsResponseModel;
    }
}
