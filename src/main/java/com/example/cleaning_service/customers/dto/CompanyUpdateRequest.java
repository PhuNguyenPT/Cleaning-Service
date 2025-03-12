package com.example.cleaning_service.customers.dto;

import com.example.cleaning_service.customers.enums.ECompanyType;
import com.example.cleaning_service.customers.enums.ECountryType;
import com.example.cleaning_service.customers.enums.EDay;
import com.example.cleaning_service.customers.enums.EPaymentType;
import com.example.cleaning_service.validations.ValidRegistrationNumber;
import com.example.cleaning_service.validations.ValidTaxId;

import java.util.Set;
import java.util.UUID;

public record CompanyUpdateRequest(
        ECompanyType companyType,
        @ValidTaxId String taxId,
        @ValidRegistrationNumber String registrationNumber,
        String billingAddress,
        EPaymentType paymentMethod,
        Set<EDay> preferredDays,
        String companyName,
        String address,
        String phone,
        String email,
        String city,
        String state,
        String zip,
        ECountryType country,
        String notes
) {
}
