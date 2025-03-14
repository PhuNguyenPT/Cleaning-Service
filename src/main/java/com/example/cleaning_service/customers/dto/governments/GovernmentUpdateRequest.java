package com.example.cleaning_service.customers.dto.governments;

import com.example.cleaning_service.commons.BusinessEntityRequest;
import com.example.cleaning_service.customers.dto.AbstractCustomerRequest;
import com.example.cleaning_service.customers.dto.OrganizationDetailsRequest;

public record GovernmentUpdateRequest (
        OrganizationDetailsRequest organizationDetails,

        String contractorName,
        String departmentName,
        Boolean isTaxExempt,
        Boolean requiresEmergencyCleaning,

        AbstractCustomerRequest customerDetails,

        BusinessEntityRequest businessEntityDetails
){
}
