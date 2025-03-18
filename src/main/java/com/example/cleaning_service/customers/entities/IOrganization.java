package com.example.cleaning_service.customers.entities;

import com.example.cleaning_service.customers.enums.EOrganizationType;

public sealed interface IOrganization permits Company, Government, IndividualCustomer, NonProfitOrg {
    String getTaxId();
    void setTaxId(String taxId);
    String getRegistrationNumber();
    void setRegistrationNumber(String registrationNumber);
    EOrganizationType getOrganizationType();
}

