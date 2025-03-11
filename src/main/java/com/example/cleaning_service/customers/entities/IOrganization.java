package com.example.cleaning_service.customers.entities;

import com.example.cleaning_service.customers.enums.EOrganizationType;

public interface IOrganization {
    String getTaxId();
    String getRegistrationNumber();
    EOrganizationType getOrganizationType();
}

