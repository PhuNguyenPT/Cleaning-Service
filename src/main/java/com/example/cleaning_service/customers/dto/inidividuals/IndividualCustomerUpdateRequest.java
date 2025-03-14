package com.example.cleaning_service.customers.dto.inidividuals;

import com.example.cleaning_service.commons.BusinessEntityRequest;
import com.example.cleaning_service.customers.dto.AbstractCustomerRequest;
import com.example.cleaning_service.customers.dto.OrganizationDetailsUpdateRequest;

public record IndividualCustomerUpdateRequest(
        OrganizationDetailsUpdateRequest organizationDetails,

        AbstractCustomerRequest customerDetails,

        BusinessEntityRequest businessEntityDetails
) {
}
