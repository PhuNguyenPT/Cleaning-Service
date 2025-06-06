package com.example.cleaning_service.customers.services.impl;

import com.example.cleaning_service.customers.controllers.*;
import com.example.cleaning_service.customers.entities.*;
import com.example.cleaning_service.customers.enums.EAssociationType;
import com.example.cleaning_service.customers.services.OrganizationDetailsService;
import com.example.cleaning_service.security.entities.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
class OrganizationDetailsServiceImpl implements OrganizationDetailsService {

    private static final Logger log = LoggerFactory.getLogger(OrganizationDetailsServiceImpl.class);

    @Override
    @Transactional
    public EAssociationType getEAssociationTypeByIOrganization(IOrganization organization) {
        return switch (organization) {
            case Company ignored -> EAssociationType.REPRESENTATIVE;
            case Government ignored -> EAssociationType.REPRESENTATIVE;
            case NonProfitOrg ignored -> EAssociationType.REPRESENTATIVE;
            case IndividualCustomer ignored -> EAssociationType.OWNER;
        };
    }

    @Override
    @Transactional
    public boolean getIsPrimaryByIOrganization(IOrganization organization) {
        return switch (organization) {
            case Company ignored -> false;
            case  Government ignored -> false;
            case NonProfitOrg ignored -> false;
            case IndividualCustomer ignored -> true;
        };
    }

    @Override
    @Transactional
    public Link getLinkByIOrganization(IOrganization organization) {
        log.info("Attempting to retrieve link for {}", organization);
        return switch (organization) {
            case Company company -> linkTo(methodOn(CompanyController.class).getCompanyById(company.getId(), new User())).withRel("customer");
            case IndividualCustomer customer -> linkTo(methodOn(IndividualCustomerController.class).getIndividualCustomerById(customer.getId(), new User())).withRel("customer");
            case Government government -> linkTo(methodOn(GovernmentController.class).getGovernmentById(government.getId(), new User())).withRel("customer");
            case NonProfitOrg nonProfitOrg -> linkTo(methodOn(NonProfitOrgController.class).getNonProfitOrgById(nonProfitOrg.getId(), new User())).withRel("customer");
        };
    }

}
