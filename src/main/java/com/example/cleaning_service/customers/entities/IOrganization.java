package com.example.cleaning_service.customers.entities;

import com.example.cleaning_service.customers.enums.EOrganizationType;
import com.example.cleaning_service.validations.ValidRegistrationNumber;
import com.example.cleaning_service.validations.ValidTaxId;

public sealed interface IOrganization permits Company, Government, IndividualCustomer, NonProfitOrg {
    String getTaxId();
    void setTaxId(@ValidTaxId String taxId);
    String getRegistrationNumber();
    void setRegistrationNumber(@ValidRegistrationNumber String registrationNumber);
    EOrganizationType getOrganizationType();
}

