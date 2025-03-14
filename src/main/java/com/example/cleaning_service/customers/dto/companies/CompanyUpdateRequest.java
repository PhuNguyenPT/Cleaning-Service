package com.example.cleaning_service.customers.dto.companies;

import com.example.cleaning_service.commons.BusinessEntityRequest;
import com.example.cleaning_service.customers.dto.AbstractCustomerRequest;
import com.example.cleaning_service.customers.dto.OrganizationDetailsUpdateRequest;
import com.example.cleaning_service.customers.enums.ECompanyType;

public record CompanyUpdateRequest(
        ECompanyType companyType,

        OrganizationDetailsUpdateRequest organizationDetails,

        AbstractCustomerRequest customerDetails,

        BusinessEntityRequest businessEntityDetails
) {
}
