package com.example.cleaning_service.customers.services;

import com.example.cleaning_service.customers.entities.IOrganization;
import com.example.cleaning_service.customers.enums.EAssociationType;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.RepresentationModel;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

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

    /**
     * Generates an admin-specific HATEOAS link for the specified organization.
     *
     * @param organization The organization for which to generate an admin link
     * @return A Link object pointing to the appropriate admin controller endpoint for the organization type,
     *         or null if the provided organization is null
     */
    Link getAdminCustomerLinkByIOrganization(IOrganization organization);

    /**
     * Adds HATEOAS links to a paged model based on organization data.
     * <p>
     * This method enriches a paged model with links extracted from organizations through a series of transformation
     * functions. It allows for generically handling different types of organizations and models.
     * </p>
     *
     * @param <ID> The type of the entity ID
     * @param <T> The type of the entity
     * @param <D> The type of the representation model extending RepresentationModel
     * @param <I> The type of organization that implements IOrganization
     * @param idLinkMap A map associating entity IDs with Links
     * @param page The page of entities to process
     * @param pagedModel The paged model to which links will be added
     * @param entityIdExtractor Function to extract ID from an entity
     * @param modelIdExtractor Function to extract ID from a model
     * @param organizationExtractor Function to extract an IOrganization from an entity
     * @param linkExtractor Function to generate a Link from an IOrganization
     * @param linkAdder Consumer to add a Link to a model
     */
    <ID, T, D extends RepresentationModel<D>, I extends IOrganization> void addLinksForIOrganization(
            Map<ID, Link> idLinkMap,
            Page<T> page,
            PagedModel<D> pagedModel,
            Function<T, ID> entityIdExtractor,
            Function<D, ID> modelIdExtractor,
            Function<T, I> organizationExtractor,
            Function<I, Link> linkExtractor,
            BiConsumer<D, Link> linkAdder);
}