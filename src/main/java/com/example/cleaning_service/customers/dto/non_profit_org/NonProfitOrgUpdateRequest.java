package com.example.cleaning_service.customers.dto.non_profit_org;

import com.example.cleaning_service.commons.BusinessEntityRequest;
import com.example.cleaning_service.customers.dto.AbstractCustomerRequest;
import com.example.cleaning_service.customers.dto.OrganizationDetailsRequest;

public record NonProfitOrgUpdateRequest(
        OrganizationDetailsRequest organizationDetails,

        AbstractCustomerRequest customerDetails,

        BusinessEntityRequest businessEntityRequest
) {
}
