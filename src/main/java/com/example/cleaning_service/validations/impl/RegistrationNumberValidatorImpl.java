package com.example.cleaning_service.validations.impl;

import com.example.cleaning_service.customers.enums.ECountryType;
import com.example.cleaning_service.validations.ValidRegistrationNumber;

public class RegistrationNumberValidatorImpl extends BaseCustomerValidator<ValidRegistrationNumber> {

    @Override
    protected String getRegexForCountry(ECountryType country) {
        return switch (country) {
            case US -> "^(\\d{9}|\\d{2}-\\d{7})$"; // US EIN (9 digits or 2-7 format)
            case GB -> "^[A-Z0-9]{8}$";           // UK Company Number (8 characters)            case VN -> "^(\\d{10}|\\d{13})$";      // Vietnam MST (10 or 13 digits)
            case VN -> "^(\\d{10}|\\d{13})$";      // Vietnam MST (10 or 13 digits)
            case FR -> "^(\\d{9}|[A-Z]{2}\\d{11})$"; // France SIREN (9 digits) or SIRET (14 characters)
            case SG -> "^[0-9A-Z]{9,10}$";        // Singapore UEN (9-10 alphanumeric)
            case ZZ -> "^[A-Z0-9-]{6,20}$";       // Generic fallback
        };
    }
}
