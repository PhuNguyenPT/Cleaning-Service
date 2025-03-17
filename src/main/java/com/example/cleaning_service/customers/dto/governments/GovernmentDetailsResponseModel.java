package com.example.cleaning_service.customers.dto.governments;

import com.example.cleaning_service.customers.enums.ECountryType;
import com.example.cleaning_service.customers.enums.EDay;
import com.example.cleaning_service.customers.enums.EPaymentType;
import org.springframework.hateoas.RepresentationModel;

import java.util.Set;

public final class GovernmentDetailsResponseModel extends RepresentationModel<GovernmentDetailsResponseModel> {
    private final String taxId;
    private final String registrationNumber;
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
            String taxId,
            String registrationNumber,
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

    public String getTaxId() {
        return taxId;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public String getContractorName() {
        return contractorName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public boolean isTaxExempt() {
        return isTaxExempt;
    }

    public boolean isRequiresEmergencyCleaning() {
        return requiresEmergencyCleaning;
    }

    public String getBillingAddress() {
        return billingAddress;
    }

    public EPaymentType getPaymentMethod() {
        return paymentMethod;
    }

    public Set<EDay> getPreferredDays() {
        return preferredDays;
    }

    public String getGovernmentName() {
        return governmentName;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getZip() {
        return zip;
    }

    public ECountryType getCountry() {
        return country;
    }

    public String getNotes() {
        return notes;
    }
}
