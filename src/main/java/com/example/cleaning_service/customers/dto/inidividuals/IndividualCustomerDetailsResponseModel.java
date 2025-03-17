package com.example.cleaning_service.customers.dto.inidividuals;

import com.example.cleaning_service.customers.enums.ECountryType;
import com.example.cleaning_service.customers.enums.EDay;
import com.example.cleaning_service.customers.enums.EPaymentType;

import org.springframework.hateoas.RepresentationModel;

import java.util.Set;

public final class IndividualCustomerDetailsResponseModel extends RepresentationModel<IndividualCustomerDetailsResponseModel> {
    private final String taxId;
    private final String registrationNumber;

    private final String billingAddress;
    private final EPaymentType paymentMethod;
    private final Set<EDay> preferredDays;

    private final String customerName;
    private final String address;
    private final String phone;
    private final String email;
    private final String city;
    private final String state;
    private final String zip;
    private final ECountryType country;
    private final String notes;

    public IndividualCustomerDetailsResponseModel(String taxId, String registrationNumber, String billingAddress,
                                                  EPaymentType paymentMethod, Set<EDay> preferredDays,
                                                  String customerName, String address, String phone, String email,
                                                  String city, String state, String zip, ECountryType country,
                                                  String notes) {
        this.taxId = taxId;
        this.registrationNumber = registrationNumber;
        this.billingAddress = billingAddress;
        this.paymentMethod = paymentMethod;
        this.preferredDays = preferredDays;
        this.customerName = customerName;
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

    public String getBillingAddress() {
        return billingAddress;
    }

    public EPaymentType getPaymentMethod() {
        return paymentMethod;
    }

    public Set<EDay> getPreferredDays() {
        return preferredDays;
    }

    public String getCustomerName() {
        return customerName;
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
