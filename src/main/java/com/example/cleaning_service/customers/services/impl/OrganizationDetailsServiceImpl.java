package com.example.cleaning_service.customers.services.impl;

import com.example.cleaning_service.customers.controllers.*;
import com.example.cleaning_service.customers.entities.*;
import com.example.cleaning_service.customers.enums.EAssociationType;
import com.example.cleaning_service.customers.services.OrganizationDetailsService;
import com.example.cleaning_service.security.entities.user.User;
import com.example.cleaning_service.util.PagedModelAssemblerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
class OrganizationDetailsServiceImpl implements OrganizationDetailsService {

    private static final Logger log = LoggerFactory.getLogger(OrganizationDetailsServiceImpl.class);
    private final PagedModelAssemblerUtil pageModelAssemblerUtil;

    OrganizationDetailsServiceImpl(PagedModelAssemblerUtil pageModelAssemblerUtil) {
        this.pageModelAssemblerUtil = pageModelAssemblerUtil;
    }

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

    @Override
    @Transactional
    public Link getAdminCustomerLinkByIOrganization(IOrganization organization) {
        log.info("Attempting to retrieve admin link for {}", organization);
        if (organization == null) {
            return null;
        }
        return switch (organization) {
            case Company company -> linkTo(methodOn(AdminCustomerController.class).getAdminCompanyDetailsResponseModelById(company.getId())).withRel("customer");
            case IndividualCustomer customer -> linkTo(methodOn(AdminCustomerController.class).getAdminIndividualCustomerDetailsResponseModelById(customer.getId())).withRel("customer");
            case Government government -> linkTo(methodOn(AdminCustomerController.class).getAdminGovernmentDetailsResponseModelById(government.getId())).withRel("customer");
            case NonProfitOrg nonProfitOrg -> linkTo(methodOn(AdminCustomerController.class).getAdminNonProfitOrgDetailsResponseModelById(nonProfitOrg.getId())).withRel("customer");
        };
    }

    @Override
    @Transactional
    public <ID, T, D extends RepresentationModel<D>, I extends IOrganization> void addLinksForIOrganization(
            Map<ID, Link> idLinkMap,
            Page<T> page,
            PagedModel<D> pagedModel,
            Function<T, ID> entityIdExtractor,
            Function<D, ID> modelIdExtractor,
            Function<T, I> organizationExtractor, // Ensures the extracted object implements IOrganization
            Function<I, Link> linkExtractor,
            BiConsumer<D, Link> linkAdder) {

        pageModelAssemblerUtil.addLinksFromPageToPagedModelByMap(
                idLinkMap,
                page,
                pagedModel,
                entityIdExtractor,
                modelIdExtractor,
                organizationExtractor,
                linkExtractor,
                linkAdder
        );
    }
}
