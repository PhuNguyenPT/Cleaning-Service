package com.example.cleaning_service.customers.services;

import com.example.cleaning_service.customers.dto.OrganizationDetailsRequest;
import com.example.cleaning_service.customers.entities.*;
import com.example.cleaning_service.customers.enums.EAssociationType;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

@Service
public class OrganizationDetailsService {

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
    EAssociationType getEAssociationTypeByIOrganization(@NotNull IOrganization organization) {
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
}
