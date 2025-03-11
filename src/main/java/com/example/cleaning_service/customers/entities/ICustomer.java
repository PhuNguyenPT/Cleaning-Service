package com.example.cleaning_service.customers.entities;

import com.example.cleaning_service.customers.enums.EDay;
import com.example.cleaning_service.customers.enums.ELoyaltyType;
import com.example.cleaning_service.customers.enums.EOrganizationType;
import com.example.cleaning_service.customers.enums.EPaymentType;

import java.util.Set;

public interface ICustomer {
    String getTaxId();
    String getRegistrationNumber();
    EOrganizationType getOrganizationType();
    ELoyaltyType getLoyaltyType();
    String getBillingAddress();
    EPaymentType getPaymentMethod();
    Set<EDay> getPreferredDays();
}