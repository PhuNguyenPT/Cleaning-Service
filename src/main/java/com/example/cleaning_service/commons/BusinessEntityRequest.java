package com.example.cleaning_service.commons;

import com.example.cleaning_service.customers.enums.ECountryType;

public record BusinessEntityRequest(
        String name,
        String address,
        String phone,
        String email,
        String city,
        String state,
        String zip,
        ECountryType country,
        String notes
) {}