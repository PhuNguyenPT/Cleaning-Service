package com.example.cleaning_service.customers.dto.non_profit_org;

import com.example.cleaning_service.customers.enums.ECountryType;
import com.example.cleaning_service.customers.enums.EDay;
import com.example.cleaning_service.customers.enums.EPaymentType;
import com.example.cleaning_service.validations.ValidRegistrationNumber;
import com.example.cleaning_service.validations.ValidTaxId;
import org.springframework.hateoas.RepresentationModel;

import java.util.Set;

public class NonProfitOrgDetailsResponseModel extends RepresentationModel<NonProfitOrgDetailsResponseModel> {
    private final @ValidTaxId String taxId;
    private final @ValidRegistrationNumber String registrationNumber;

    private final String billingAddress;
    private final EPaymentType paymentMethod;
    private final Set<EDay> preferredDays;

    private final String organizationName;
    private final String address;
    private final String phone;
    private final String email;
    private final String city;
    private final String state;
    private final String zip;
    private final ECountryType country;
    private final String notes;

    public NonProfitOrgDetailsResponseModel(String taxId, String registrationNumber, String billingAddress, EPaymentType paymentMethod, Set<EDay> preferredDays, String organizationName, String address, String phone, String email, String city, String state, String zip, ECountryType country, String notes) {
        this.taxId = taxId;
        this.registrationNumber = registrationNumber;
        this.billingAddress = billingAddress;
        this.paymentMethod = paymentMethod;
        this.preferredDays = preferredDays;
        this.organizationName = organizationName;
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
