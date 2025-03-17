package com.example.cleaning_service.customers.dto.inidividuals;

import com.example.cleaning_service.commons.BusinessEntityRequest;
import com.example.cleaning_service.customers.dto.AbstractCustomerRequest;
import com.example.cleaning_service.customers.dto.OrganizationDetailsRequest;
import com.example.cleaning_service.customers.enums.ECountryType;
import com.example.cleaning_service.validator.IRegistrationNumberIdentifiable;
import com.example.cleaning_service.validator.ITaxIdentifiable;

public record IndividualCustomerUpdateRequest(
        OrganizationDetailsRequest organizationDetails,

        AbstractCustomerRequest customerDetails,

        BusinessEntityRequest businessEntityDetails
) implements ITaxIdentifiable, IRegistrationNumberIdentifiable {
    @Override
    public String getRegistrationNumber()
    {
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