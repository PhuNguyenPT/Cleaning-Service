package com.example.cleaning_service.validator.impl;

import com.example.cleaning_service.validator.TaxIdentifiable;
import com.example.cleaning_service.customers.enums.ECountryType;
import com.example.cleaning_service.validator.ValidTaxId;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class ValidTaxIdValidator implements ConstraintValidator<ValidTaxId, TaxIdentifiable> {
    private static final Map<ECountryType, Pattern> TAX_ID_PATTERNS = new HashMap<>();

    static {
        // United States - 9 digits, may contain hyphens
        TAX_ID_PATTERNS.put(ECountryType.US, Pattern.compile("^\\d{2}-?\\d{7}$|^\\d{3}-?\\d{2}-?\\d{4}$"));

        // Canada - Business Number (BN) - 9 digits
        TAX_ID_PATTERNS.put(ECountryType.CA, Pattern.compile("^\\d{9}$"));

        // United Kingdom - VAT number - GB followed by 9 or 12 digits
        TAX_ID_PATTERNS.put(ECountryType.GB, Pattern.compile("^GB\\d{9}$|^GB\\d{12}$"));

        // Germany - VAT number - DE followed by 9 digits
        TAX_ID_PATTERNS.put(ECountryType.DE, Pattern.compile("^DE\\d{9}$"));

        // France - VAT number - FR followed by 11 characters
        TAX_ID_PATTERNS.put(ECountryType.FR, Pattern.compile("^FR[A-Z0-9]{2}\\d{9}$"));

        // Australia - ABN - 11 digits
        TAX_ID_PATTERNS.put(ECountryType.AU, Pattern.compile("^\\d{11}$"));

        // Add more country-specific patterns as needed

        // Default pattern for countries not specifically defined - allow alphanumeric with hyphens, 2-20 chars
        TAX_ID_PATTERNS.put(null, Pattern.compile("^[A-Z0-9\\-]{2,20}$"));
    }

    private String getTaxIdFormatMessage(ECountryType country) {
        return switch (country) {
            case US -> "Should be 9 digits (e.g., 123456789 or 12-3456789).";
            case CA -> "Should be 9 digits.";
            case GB -> "Should start with GB followed by 9 or 12 digits.";
            case DE -> "Should start with DE followed by 9 digits.";
            case FR -> "Should start with FR followed by 2 characters and 9 digits.";
            case AU -> "Should be 11 digits.";
            default -> "Should be 2-20 characters including letters, numbers, and hyphens.";
        };
    }

    @Override
    public boolean isValid(TaxIdentifiable taxIdentifiable, ConstraintValidatorContext context) {
        if (taxIdentifiable == null || taxIdentifiable.getTaxId() == null || taxIdentifiable.getCountry() == null) {
            return true; // Let @NotNull or @NotBlank handle these cases
        }

        String taxId = taxIdentifiable.getTaxId();
        ECountryType country = taxIdentifiable.getCountry();

        // Get the pattern for the specified country or use default if not found
        Pattern pattern = TAX_ID_PATTERNS.getOrDefault(country, TAX_ID_PATTERNS.get(null));

        boolean isValid = pattern.matcher(taxId).matches();

        if (!isValid) {
            // Customize the error message with country-specific information
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                            "Invalid tax ID format for " + country + ". " + getTaxIdFormatMessage(country))
                    .addPropertyNode("taxId")
                    .addConstraintViolation();
        }

        return isValid;
    }
}
