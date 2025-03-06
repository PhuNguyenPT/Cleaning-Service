package com.example.cleaning_service.customers.api;

import com.example.cleaning_service.customers.enums.EDay;
import com.example.cleaning_service.customers.enums.ELoyaltyType;
import com.example.cleaning_service.customers.enums.EOrganizationType;

import java.util.Set;

public interface ICustomer {
    String getTaxId();
    String getRegistrationNumber();
    EOrganizationType getOrganizationType();
    ELoyaltyType getLoyaltyType();
    String getBillingAddress();
    String getPaymentMethod();
    Set<EDay> getPreferredDays();
}