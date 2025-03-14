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
import com.example.cleaning_service.customers.mappers.NonProfitOrgMapper;
import com.example.cleaning_service.customers.repositories.NonProfitOrgRepository;
import com.example.cleaning_service.security.entities.user.User;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class NonProfitOrgService {
    private final NonProfitOrgRepository nonProfitOrgRepository;
    private final NonProfitOrgMapper nonProfitOrgMapper;
    private final AccountAssociationService accountAssociationService;
    private final NonProfitOrgResponseModelAssembler nonProfitOrgResponseModelAssembler;
    private final NonProfitOrgDetailsResponseModelAssembler nonProfitOrgDetailsResponseModelAssembler;
    private final OrganizationDetailsService organizationDetailsService;
    private final AbstractCustomerService abstractCustomerService;
    private final BusinessEntityService businessEntityService;


    public NonProfitOrgService(NonProfitOrgRepository nonProfitOrgRepository, NonProfitOrgMapper nonProfitOrgMapper, AccountAssociationService accountAssociationService, NonProfitOrgResponseModelAssembler nonProfitOrgResponseModelAssembler, NonProfitOrgDetailsResponseModelAssembler nonProfitOrgDetailsResponseModelAssembler, OrganizationDetailsService organizationDetailsService, AbstractCustomerService abstractCustomerService, BusinessEntityService businessEntityService) {
        this.nonProfitOrgRepository = nonProfitOrgRepository;
        this.nonProfitOrgMapper = nonProfitOrgMapper;
        this.accountAssociationService = accountAssociationService;
        this.nonProfitOrgResponseModelAssembler = nonProfitOrgResponseModelAssembler;
        this.nonProfitOrgDetailsResponseModelAssembler = nonProfitOrgDetailsResponseModelAssembler;
        this.organizationDetailsService = organizationDetailsService;
        this.abstractCustomerService = abstractCustomerService;
        this.businessEntityService = businessEntityService;
    }

    @Transactional
    public NonProfitOrgResponseModel createProfitOrg(@Valid NonProfitOrgRequest nonProfitOrgRequest, User user) {
        if (accountAssociationService.isExistsAccountAssociationByUser(user)) {
            throw new IllegalStateException("User " + user.getUsername() + " is already associated with an account.");
        }

        NonProfitOrg nonProfitOrg = nonProfitOrgMapper.fromRequestToNonProfitOrg(nonProfitOrgRequest);
        NonProfitOrg savedNonProfitOrg = saveNonProfitOrg(nonProfitOrg);

        AccountAssociationRequest accountAssociationRequest = new AccountAssociationRequest(user, savedNonProfitOrg);
        AccountAssociation accountAssociation = accountAssociationService.saveAccountAssociation(accountAssociationRequest);

        if(!(accountAssociation.getCustomer() instanceof NonProfitOrg accountNonProfitOrg)) {
            throw new IllegalStateException("Account association does not reference a valid non-profit org.");
        }
        return nonProfitOrgResponseModelAssembler.toModel(accountNonProfitOrg);
    }

    @Transactional
    NonProfitOrg saveNonProfitOrg(NonProfitOrg nonProfitOrg) {
        return nonProfitOrgRepository.save(nonProfitOrg);
    }

    @Transactional
    public NonProfitOrgDetailsResponseModel getNonProfitOrgDetailsResponseModelById(UUID id, User user) {
        NonProfitOrg nonProfitOrg = getByIdAndUser(id, user);
        return nonProfitOrgDetailsResponseModelAssembler.toModel(nonProfitOrg);
    }

    @Transactional
    NonProfitOrg getByIdAndUser(UUID id, User user) {
        if (!(accountAssociationService.isExistsAccountAssociationByUser(user))) {
            throw new IllegalStateException("User " + user.getUsername() + " is not associated with a non-profit organization.");
        }
        return findById(id);
    }

    @Transactional
    NonProfitOrg findById(UUID id) {
        return nonProfitOrgRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Non-profit org not found."));
    }

    @Transactional
    public NonProfitOrgDetailsResponseModel updateNonProfitOrgDetailsById(UUID id, @Valid NonProfitOrgUpdateRequest updateRequest, User user) {
        NonProfitOrg nonProfitOrg = getByIdAndUser(id, user);

        updateNonProfitOrgDetails(nonProfitOrg, updateRequest);
        NonProfitOrg updatedNonProfitOrg = saveNonProfitOrg(nonProfitOrg);
        return nonProfitOrgDetailsResponseModelAssembler.toModel(updatedNonProfitOrg);
    }

    @Transactional
    void updateNonProfitOrgDetails(NonProfitOrg nonProfitOrg, @Valid NonProfitOrgUpdateRequest updateRequest) {
        organizationDetailsService.updateOrganizationDetails(nonProfitOrg, updateRequest.organizationDetails());

        abstractCustomerService.updateAbstractCustomerDetails(nonProfitOrg, updateRequest.customerDetails());

        businessEntityService.updateBusinessEntityFields(nonProfitOrg, updateRequest.businessEntityRequest());
    }

    @Transactional
    public void deleteNonProfitOrgById(UUID id, User user) {
        NonProfitOrg nonProfitOrg = getByIdAndUser(id, user);
        accountAssociationService.detachCustomerFromAssociation(nonProfitOrg);
        nonProfitOrgRepository.delete(nonProfitOrg);
    }
}
