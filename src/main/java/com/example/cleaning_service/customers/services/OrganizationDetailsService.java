package com.example.cleaning_service.customers.services;

import com.example.cleaning_service.customers.controllers.*;
import com.example.cleaning_service.customers.dto.OrganizationDetailsRequest;
import com.example.cleaning_service.customers.entities.*;
import com.example.cleaning_service.customers.enums.EAssociationType;
import com.example.cleaning_service.security.entities.user.User;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class OrganizationDetailsService {

    private static final Logger log = LoggerFactory.getLogger(OrganizationDetailsService.class);

    @Transactional
    void updateOrganizationDetails(IOrganization organization, @Valid OrganizationDetailsRequest organizationDetailsUpdateRequest) {
        if (organizationDetailsUpdateRequest.taxId() != null) {
            organization.setTaxId(organizationDetailsUpdateRequest.taxId());
        }
        if (organizationDetailsUpdateRequest.registrationNumber() != null) {
            organization.setRegistrationNumber(organizationDetailsUpdateRequest.registrationNumber());
        }
    }
    
    @Transactional
    EAssociationType getEAssociationTypeByIOrganization(IOrganization organization) {
        return switch (organization) {
            case Company _, Government _, NonProfitOrg _ -> EAssociationType.REPRESENTATIVE;
            case IndividualCustomer _ -> EAssociationType.OWNER;
        };
    }
    
    @Transactional
    boolean getIsPrimaryByIOrganization(IOrganization organization) {
        return switch (organization) {
            case Company _, Government _, NonProfitOrg _ -> false;
            case IndividualCustomer _ -> true;
        };
    }

    @Transactional
    Link getLinkByIOrganization(IOrganization organization) {
        log.info("Attempting to retrieve link for {}", organization);
        return switch (organization) {
            case Company company -> linkTo(methodOn(CompanyController.class).getCompanyById(company.getId(), new User())).withRel("customer");
            case IndividualCustomer customer -> linkTo(methodOn(IndividualCustomerController.class).getIndividualCustomerById(customer.getId(), new User())).withRel("customer");
            case Government government -> linkTo(methodOn(GovernmentController.class).getGovernmentById(government.getId(), new User())).withRel("customer");
            case NonProfitOrg nonProfitOrg -> linkTo(methodOn(NonProfitOrgController.class).getNonProfitOrgById(nonProfitOrg.getId(), new User())).withRel("customer");
        };
    }
}
