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
import com.example.cleaning_service.customers.mappers.IndividualCustomerMapper;
import com.example.cleaning_service.customers.repositories.IndividualCustomerRepository;
import com.example.cleaning_service.security.entities.user.User;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class IndividualCustomerService {

    private final IndividualCustomerRepository individualCustomerRepository;
    private final IndividualCustomerMapper individualCustomerMapper;
    private final AccountAssociationService accountAssociationService;
    private final IndividualCustomerResponseModelAssembler individualCustomerResponseModelAssembler;
    private final IndividualCustomerDetailsResponseModelAssembler individualCustomerDetailsResponseModelAssembler;
    private final AbstractCustomerService abstractCustomerService;
    private final BusinessEntityService businessEntityService;
    private final OrganizationDetailsService organizationDetailsService;

    public IndividualCustomerService(IndividualCustomerRepository individualCustomerRepository,
                                     IndividualCustomerMapper individualCustomerMapper,
                                     AccountAssociationService accountAssociationService, IndividualCustomerResponseModelAssembler individualCustomerResponseModelAssembler, IndividualCustomerDetailsResponseModelAssembler individualCustomerDetailsResponseModelAssembler, AbstractCustomerService abstractCustomerService, BusinessEntityService businessEntityService, OrganizationDetailsService organizationDetailsService) {
        this.individualCustomerRepository = individualCustomerRepository;
        this.individualCustomerMapper = individualCustomerMapper;
        this.accountAssociationService = accountAssociationService;
        this.individualCustomerResponseModelAssembler = individualCustomerResponseModelAssembler;
        this.individualCustomerDetailsResponseModelAssembler = individualCustomerDetailsResponseModelAssembler;
        this.abstractCustomerService = abstractCustomerService;
        this.businessEntityService = businessEntityService;
        this.organizationDetailsService = organizationDetailsService;
    }

    @Transactional
    public IndividualCustomerResponseModel createIndividualCustomer(@Valid IndividualCustomerRequest individualCustomerRequest, User user) {
        if (accountAssociationService.isExistsAccountAssociationByUser(user)) {
            throw new IllegalStateException("User " + user.getUsername() + " is already associated with an account.");
        }

        IndividualCustomer individualCustomer = individualCustomerMapper.fromRequestToCustomer(individualCustomerRequest);
        IndividualCustomer dbCustomer = saveIndividualCustomer(individualCustomer);

        AccountAssociationRequest accountAssociationRequest = new AccountAssociationRequest(user, dbCustomer);
        AccountAssociation dbAccountAssociation = accountAssociationService.createAccountAssociation(accountAssociationRequest);

        if (!(dbAccountAssociation.getCustomer() instanceof IndividualCustomer accountCustomer)) {
            throw new IllegalStateException("Account association does not reference a valid individual.");
        }

        return individualCustomerResponseModelAssembler.toModel(accountCustomer);
    }

    @Transactional
    IndividualCustomer saveIndividualCustomer(IndividualCustomer individualCustomer) {
        return individualCustomerRepository.save(individualCustomer);
    }

    @Transactional
    public IndividualCustomerDetailsResponseModel getIndividualCustomerById(UUID id, User user) {
        IndividualCustomer individualCustomer = getByIdAndUser(id, user);
        return individualCustomerDetailsResponseModelAssembler.toModel(individualCustomer);
    }

    @Transactional
    IndividualCustomer getByIdAndUser(UUID id, User user) {
        if (!(accountAssociationService.isExistsAccountAssociationByUser(user))) {
            throw new IllegalStateException("User " + user.getUsername() + " is not associated with an individual customer.");
        }
        return findById(id);
    }

    @Transactional
    IndividualCustomer findById(UUID id) {
        return individualCustomerRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Individual customer with id " + id + " does not exist."));
    }

    @Transactional
    public IndividualCustomerDetailsResponseModel updateIndividualCustomerDetailsById(UUID id, @Valid IndividualCustomerUpdateRequest updateRequest, User user) {
        IndividualCustomer individualCustomer = getByIdAndUser(id, user);

        updateCustomerFields(individualCustomer, updateRequest);
        IndividualCustomer updatedIndividualCustomer = saveIndividualCustomer(individualCustomer);
        return individualCustomerDetailsResponseModelAssembler.toModel(updatedIndividualCustomer);
    }

    @Transactional
    void updateCustomerFields(IndividualCustomer individualCustomer, @Valid IndividualCustomerUpdateRequest updateRequest) {
        organizationDetailsService.updateOrganizationDetails(individualCustomer, updateRequest.organizationDetails());

        abstractCustomerService.updateAbstractCustomerDetails(individualCustomer, updateRequest.customerDetails());

        businessEntityService.updateBusinessEntityFields(individualCustomer, updateRequest.businessEntityDetails());
    }

    @Transactional
    public void deleteIndividualCustomerById(UUID id, User user) {
        IndividualCustomer individualCustomer = getByIdAndUser(id, user);
        accountAssociationService.detachCustomerFromAssociation(individualCustomer);
        individualCustomerRepository.delete(individualCustomer);
    }
}