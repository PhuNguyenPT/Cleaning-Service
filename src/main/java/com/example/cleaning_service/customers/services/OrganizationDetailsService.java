package com.example.cleaning_service.customers.services;

import com.example.cleaning_service.customers.dto.OrganizationDetailsRequest;
import com.example.cleaning_service.customers.entities.IOrganization;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
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
}
