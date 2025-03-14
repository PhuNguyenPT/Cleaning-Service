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
import com.example.cleaning_service.customers.mappers.GovernmentMapper;
import com.example.cleaning_service.customers.repositories.GovernmentRepository;
import com.example.cleaning_service.security.entities.user.User;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GovernmentService {
    private final GovernmentRepository governmentRepository;
    private final AccountAssociationService accountAssociationService;
    private final GovernmentMapper governmentMapper;
    private final GovernmentResponseModelAssembler governmentResponseModelAssembler;
    private final GovernmentDetailsResponseModelAssembler governmentDetailsResponseModelAssembler;
    private final BusinessEntityService businessEntityService;
    private final AbstractCustomerService abstractCustomerService;
    private final OrganizationDetailsService organizationDetailsService;

    public GovernmentService(GovernmentRepository governmentRepository, AccountAssociationService accountAssociationService, GovernmentMapper governmentMapper, GovernmentResponseModelAssembler governmentResponseModelAssembler, GovernmentDetailsResponseModelAssembler governmentDetailsResponseModelAssembler, BusinessEntityService businessEntityService, AbstractCustomerService abstractCustomerService, OrganizationDetailsService organizationDetailsService) {
        this.governmentRepository = governmentRepository;
        this.accountAssociationService = accountAssociationService;
        this.governmentMapper = governmentMapper;
        this.governmentResponseModelAssembler = governmentResponseModelAssembler;
        this.governmentDetailsResponseModelAssembler = governmentDetailsResponseModelAssembler;
        this.businessEntityService = businessEntityService;
        this.abstractCustomerService = abstractCustomerService;
        this.organizationDetailsService = organizationDetailsService;
    }


    @Transactional
    public GovernmentResponseModel createGovernment(@Valid GovernmentRequest governmentRequest, @NotNull User user) {
        if (accountAssociationService.isExistsAccountAssociationByUser(user)) {
            throw new IllegalStateException("User " + user.getUsername() + " is already associated with an account.");
        }

        Government government = governmentMapper.fromGovernmentRequestToGovernment(governmentRequest);
        Government dbGovernment = governmentRepository.save(government);

        AccountAssociationRequest accountAssociationRequest = new AccountAssociationRequest(user, dbGovernment);
        AccountAssociation dbAccountAssociation = accountAssociationService.createAccountAssociation(accountAssociationRequest);

        if (!(dbAccountAssociation.getCustomer() instanceof Government accountGovernment)) {
            throw new IllegalStateException("Account association does not reference a valid government.");
        }

        return governmentResponseModelAssembler.toModel(accountGovernment);
    }

    @Transactional
    public GovernmentDetailsResponseModel getGovernmentDetailsResponseModelById(UUID id, @NotNull User user) {
        Government dbGovernment = getByIdAndUser(id, user);
        return governmentDetailsResponseModelAssembler.toModel(dbGovernment);
    }

    @Transactional
    Government getByIdAndUser(UUID id, User user) {
        if (!accountAssociationService.isExistsAccountAssociationByUser(user)) {
            throw new IllegalStateException("User " + user.getUsername() + " is not associated with a government.");
        }
        return findById(id);
    }

    @Transactional
    Government findById(UUID id) {
        return governmentRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Government with id " + id + " not found."));
    }

    @Transactional
    public GovernmentDetailsResponseModel updateCompanyDetailsById(UUID id, @Valid GovernmentUpdateRequest updateRequest, User user) {
        Government government = getByIdAndUser(id, user);

        updateGovernmentFields(government, updateRequest);
        Government updatedGovernment = saveGovernment(government);
        return governmentDetailsResponseModelAssembler.toModel(updatedGovernment);
    }

    @Transactional
    void updateGovernmentFields(Government government, @Valid GovernmentUpdateRequest updateRequest) {
        organizationDetailsService.updateOrganizationDetails(government, updateRequest.organizationDetails());

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

        abstractCustomerService.updateAbstractCustomerDetails(government, updateRequest.customerDetails());

        businessEntityService.updateBusinessEntityFields(government, updateRequest.businessEntityDetails());
    }

    @Transactional
    Government saveGovernment(Government government) {
        return governmentRepository.save(government);
    }

    @Transactional
    public void deleteGovernmentById(UUID id, User user) {
        Government dbGovernment = getByIdAndUser(id, user);
        accountAssociationService.detachCustomerFromAssociation(dbGovernment);
        governmentRepository.delete(dbGovernment);
    }
}
