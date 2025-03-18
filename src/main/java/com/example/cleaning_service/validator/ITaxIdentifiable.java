package com.example.cleaning_service.validator;

import com.example.cleaning_service.customers.enums.ECountryType;

public interface ITaxIdentifiable {
    String taxId();
    ECountryType country();
}
