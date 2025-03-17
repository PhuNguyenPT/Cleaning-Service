package com.example.cleaning_service.customers.dto.companies;

import com.example.cleaning_service.commons.BusinessEntityRequest;
import com.example.cleaning_service.customers.dto.AbstractCustomerRequest;
import com.example.cleaning_service.customers.dto.OrganizationDetailsRequest;
import com.example.cleaning_service.customers.enums.ECompanyType;
import com.example.cleaning_service.customers.enums.ECountryType;
import com.example.cleaning_service.validator.RegistrationNumberIdentifiable;
import com.example.cleaning_service.validator.TaxIdentifiable;

public record CompanyUpdateRequest(
        ECompanyType companyType,

        OrganizationDetailsRequest organizationDetails,

        AbstractCustomerRequest customerDetails,

        BusinessEntityRequest businessEntityDetails
) implements TaxIdentifiable, RegistrationNumberIdentifiable
{
    @Override
    public String getRegistrationNumber() {
        return organizationDetails.registrationNumber();
    }

    @Override
    public String getTaxId() {
        return organizationDetails.taxId();
    }

    @Override
    public ECountryType getCountry() {
        return businessEntityDetails.country();
    }
}
