package com.example.cleaning_service.validations.impl;

import com.example.cleaning_service.customers.enums.ECountryType;
import com.example.cleaning_service.validations.ValidTaxId;

public class TaxIdValidatorImpl extends BaseCustomerValidator<ValidTaxId> {

    @Override
    protected String getRegexForCountry(ECountryType country) {
        return switch (country) {
            case US -> "^(\\d{9}|\\d{2}-\\d{7})$";   // 9-digit SSN/EIN or XX-XXXXXXX
            case GB -> "^[A-Z0-9]{10,12}$";         // 10-12 alphanumeric UTR / VAT numbers
            case VN -> "^(\\d{10}|\\d{13})$";       // 10 or 13-digit MST (business & branch)
            case FR -> "^\\d{9}$";                  // 9-digit SIREN (business identifier)
            case SG -> "^[0-9A-Z]{9,10}$";          // 9-10 digit UEN (Singapore)
            case ZZ -> "^[A-Z0-9-]{8,15}$";         // Generic fallback (8-15 characters)
        };
    }
}