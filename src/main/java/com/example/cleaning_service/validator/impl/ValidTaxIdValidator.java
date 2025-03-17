package com.example.cleaning_service.validator.impl;

import com.example.cleaning_service.validator.ITaxIdentifiable;
import com.example.cleaning_service.customers.enums.ECountryType;
import com.example.cleaning_service.validator.ValidTaxId;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.EnumMap;
import java.util.Map;
import java.util.regex.Pattern;

public class ValidTaxIdValidator implements ConstraintValidator<ValidTaxId, ITaxIdentifiable> {
    private static final Logger logger = LoggerFactory.getLogger(ValidTaxIdValidator.class);

    private static final Map<ECountryType, Pattern> TAX_ID_PATTERNS = new EnumMap<>(ECountryType.class);

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
    public boolean isValid(ITaxIdentifiable taxIdentifiable, ConstraintValidatorContext context) {
        if (taxIdentifiable == null || (taxIdentifiable.getTaxId() == null && taxIdentifiable.getCountry() == null)) {
            logger.warn("Validation skipped: entity or both tax ID and country are null.");
            return true; // Let @NotNull or @NotBlank handle these cases
        }

        String taxId = taxIdentifiable.getTaxId();
        ECountryType country = taxIdentifiable.getCountry();

        logger.info("Validating tax ID '{}' for country '{}'", taxId, country);

        // Get the pattern for the specified country or use default if not found
        Pattern pattern = TAX_ID_PATTERNS.getOrDefault(country, TAX_ID_PATTERNS.get(null));
        logger.info("Pattern for country '{}' for tax ID '{}' is '{}'", country, taxId, pattern);

        boolean isValid = pattern.matcher(taxId).matches();

        if (!isValid) {
            String errorMessage = "Invalid tax ID format for " + country + ". " + getTaxIdFormatMessage(country);
            logger.warn("Validation failed: {}", errorMessage);

            // Customize the error message with country-specific information
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(errorMessage)
                    .addPropertyNode("taxId")
                    .addConstraintViolation();
        }
        logger.info("Validation passed for tax ID '{}'", taxId);

        return isValid;
    }
}