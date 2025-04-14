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

public interface OrganizationDetailsService {
    EAssociationType getEAssociationTypeByIOrganization(IOrganization organization);
    boolean getIsPrimaryByIOrganization(IOrganization organization);
    Link getLinkByIOrganization(IOrganization organization);
    Link getAdminCustomerLinkByIOrganization(IOrganization organization);
    <ID, T, D extends RepresentationModel<D>, I extends IOrganization> void addLinksForIOrganization(
            Map<ID, Link> idLinkMap,
            Page<T> page,
            PagedModel<D> pagedModel,
            Function<T, ID> entityIdExtractor,
            Function<D, ID> modelIdExtractor,
            Function<T, I> organizationExtractor, // Ensures the extracted object implements IOrganization
            Function<I, Link> linkExtractor,
            BiConsumer<D, Link> linkAdder);
}
