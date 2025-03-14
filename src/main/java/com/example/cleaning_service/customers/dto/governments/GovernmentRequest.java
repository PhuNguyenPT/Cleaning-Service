package com.example.cleaning_service.customers.dto.governments;

import com.example.cleaning_service.customers.enums.ECountryType;
import com.example.cleaning_service.customers.enums.EDay;
import com.example.cleaning_service.customers.enums.EPaymentType;
import com.example.cleaning_service.validations.ValidRegistrationNumber;
import com.example.cleaning_service.validations.ValidTaxId;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record GovernmentRequest(
        @NotBlank @ValidTaxId String taxId,
        @NotBlank @ValidRegistrationNumber String registrationNumber,

        String contractorName,
        String departmentName,
        boolean isTaxExempt,
        boolean requiresEmergencyCleaning,

        String billingAddress,
        EPaymentType paymentMethod,
        Set<EDay> preferredDays,

        @NotBlank String governmentName,
        String address,
        String phone,
        String email,
        String city,
        String state,
        String zip,
        @NotNull ECountryType country,
        String notes
) {
}
