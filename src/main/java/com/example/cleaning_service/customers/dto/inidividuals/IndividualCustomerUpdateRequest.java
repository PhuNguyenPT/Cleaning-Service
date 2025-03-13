package com.example.cleaning_service.customers.dto.inidividuals;

import com.example.cleaning_service.commons.BusinessEntityRequest;
import com.example.cleaning_service.customers.dto.AbstractCustomerRequest;
import com.example.cleaning_service.validations.ValidRegistrationNumber;
import com.example.cleaning_service.validations.ValidTaxId;

public record IndividualCustomerUpdateRequest(
        @ValidTaxId String taxId,
        @ValidRegistrationNumber String registrationNumber,

        AbstractCustomerRequest customerDetails,

        BusinessEntityRequest businessEntityDetails
) {
}
