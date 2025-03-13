package com.example.cleaning_service.customers.dto.governments;

import com.example.cleaning_service.customers.enums.ECountryType;
import com.example.cleaning_service.customers.enums.EDay;
import com.example.cleaning_service.customers.enums.EPaymentType;
import com.example.cleaning_service.validations.ValidRegistrationNumber;
import com.example.cleaning_service.validations.ValidTaxId;
import org.springframework.hateoas.RepresentationModel;

import java.util.Set;

public class GovernmentDetailsResponseModel extends RepresentationModel<GovernmentDetailsResponseModel> {
    private final @ValidTaxId String taxId;
    private final @ValidRegistrationNumber String registrationNumber;
    private final String contractorName;
    private final String departmentName;
    private final boolean isTaxExempt;
    private final boolean requiresEmergencyCleaning;
    private final String billingAddress;
    private final EPaymentType paymentMethod;
    private final Set<EDay> preferredDays;
    private final String governmentName;
    private final String address;
    private final String phone;
    private final String email;
    private final String city;
    private final String state;
    private final String zip;
    private final ECountryType country;
    private final String notes;

    public GovernmentDetailsResponseModel(
            @ValidTaxId String taxId,
            @ValidRegistrationNumber String registrationNumber,
            String contractorName,
            String departmentName,
            boolean isTaxExempt,
            boolean requiresEmergencyCleaning,
            String billingAddress,
            EPaymentType paymentMethod,
            Set<EDay> preferredDays,
            String governmentName,
            String address,
            String phone,
            String email,
            String city,
            String state,
            String zip,
            ECountryType country,
            String notes
    ) {
        this.taxId = taxId;
        this.registrationNumber = registrationNumber;
        this.contractorName = contractorName;
        this.departmentName = departmentName;
        this.isTaxExempt = isTaxExempt;
        this.requiresEmergencyCleaning = requiresEmergencyCleaning;
        this.billingAddress = billingAddress;
        this.paymentMethod = paymentMethod;
        this.preferredDays = preferredDays;
        this.governmentName = governmentName;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.country = country;
        this.notes = notes;
    }

}
