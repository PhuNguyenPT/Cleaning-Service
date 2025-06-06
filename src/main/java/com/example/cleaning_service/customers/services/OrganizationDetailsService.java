package com.example.cleaning_service.customers.services;

import com.example.cleaning_service.customers.entities.IOrganization;
import com.example.cleaning_service.customers.enums.EAssociationType;
import org.springframework.hateoas.Link;

/**
 * Service interface that handles organization-related operations for different types of customers.
 * <p>
 * This service provides functionality for determining association types, generating appropriate links,
 * and managing HATEOAS relationships for various types of organizations in the cleaning service system.
 * It supports different customer types such as Company, IndividualCustomer, Government, and NonProfitOrg.
 * </p>
 */
public interface OrganizationDetailsService {

    /**
     * Determines the association type based on the organization type.
     *
     * @param organization The organization to evaluate
     * @return {@code  EAssociationType.REPRESENTATIVE} for Company, Government, or NonProfitOrg,
     *         {@code EAssociationType.OWNER} for IndividualCustomer
     */
    EAssociationType getEAssociationTypeByIOrganization(IOrganization organization);

    /**
     * Determines if the organization should be marked as primary based on its type.
     *
     * @param organization The organization to evaluate
     * @return true if the organization is an IndividualCustomer, false otherwise
     */
    boolean getIsPrimaryByIOrganization(IOrganization organization);

    /**
     * Generates a HATEOAS link for the specified organization to be used in regular views.
     *
     * @param organization The organization for which to generate a link
     * @return A Link object pointing to the appropriate controller endpoint for the organization type
     */
    Link getLinkByIOrganization(IOrganization organization);
}