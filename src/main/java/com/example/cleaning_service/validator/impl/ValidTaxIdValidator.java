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

    private static final Pattern DEFAULT_PATTERN = Pattern.compile("^[A-Za-z0-9-]{2,20}$");

    static {
        // United States - EIN (XX-XXXXXXX) or SSN (XXX-XX-XXXX)
        // Also support 501(c)(3) exempt organizations format
        TAX_ID_PATTERNS.put(ECountryType.US, Pattern.compile(
                "^\\d{2}-\\d{7}$|" +                // EIN with hyphen
                        "^\\d{9}$|" +                       // EIN without hyphen
                        "^\\d{3}-\\d{2}-\\d{4}$|" +         // SSN with hyphens
                        "^501\\(c\\)\\(3\\)-\\d{1,10}$"     // 501(c)(3) exempt orgs
        ));

        // Canada - Business Number (BN) - 9 digits, can be formatted as XX-XXX-XXX
        TAX_ID_PATTERNS.put(ECountryType.CA, Pattern.compile("^\\d{9}$|^\\d{2}-\\d{3}-\\d{3}$"));

        // United Kingdom - VAT number - GB followed by 9 or 12 digits
        // Also support UTR (Unique Taxpayer Reference) - 10 digits
        TAX_ID_PATTERNS.put(ECountryType.GB, Pattern.compile(
                "^GB\\d{9}$|" +              // VAT number (9 digits)
                        "^GB\\d{12}$|" +             // VAT number (12 digits)
                        "^\\d{10}$"                  // UTR (10 digits)
        ));

        // Germany - VAT number (USt-IdNr) - DE followed by 9 digits
        // Also support Steuernummer format - varies by state but typically 10-13 digits with slashes
        TAX_ID_PATTERNS.put(ECountryType.DE, Pattern.compile(
                "^DE\\d{9}$|" +                         // VAT number
                        "^\\d{3}/\\d{3}/\\d{5}$|" +             // Steuernummer (with slashes)
                        "^\\d{2}/\\d{3}/\\d{4}/\\d{4}$"         // Alternative Steuernummer format
        ));

        // France - VAT number (TVA) - FR followed by 11 characters
        // Also support SIRET (14 digits) and SIREN (9 digits)
        TAX_ID_PATTERNS.put(ECountryType.FR, Pattern.compile(
                "^FR[A-Z0-9]{2}\\d{9}$|" +      // TVA (VAT number)
                        "^\\d{14}$|" +                  // SIRET
                        "^\\d{9}$"                      // SIREN
        ));

        // Australia - ABN (11 digits) or ACN (9 digits)
        // May include optional spaces
        TAX_ID_PATTERNS.put(ECountryType.AU, Pattern.compile(
                "^\\d{11}$|" +                       // ABN without spaces
                        "^\\d{2}\\s\\d{3}\\s\\d{3}\\s\\d{3}$|" + // ABN with spaces
                        "^\\d{9}$|" +                        // ACN without spaces
                        "^\\d{3}\\s\\d{3}\\s\\d{3}$"         // ACN with spaces
        ));
    }

    private String getTaxIdFormatMessage(ECountryType country) {
        return switch (country) {
            case US -> "Should be an EIN (XX-XXXXXXX), SSN (XXX-XX-XXXX), or 501(c)(3) format.";
            case CA -> "Should be a 9-digit Business Number, with or without hyphens (e.g., 123456789 or 12-345-678).";
            case GB -> "Should be a VAT number (GB followed by 9 or 12 digits) or a 10-digit UTR.";
            case DE -> "Should be a VAT number (DE followed by 9 digits) or Steuernummer with appropriate format.";
            case FR -> "Should be a TVA (FR followed by 11 characters), 14-digit SIRET, or 9-digit SIREN.";
            case AU -> "Should be an 11-digit ABN or a 9-digit ACN, with or without spaces.";
            default -> "Should be 2-20 characters including letters, numbers, and hyphens.";
        };
    }

    @Override
    public boolean isValid(ITaxIdentifiable taxIdentifiable, ConstraintValidatorContext context) {
        if (taxIdentifiable == null) {
            logger.warn("Validation skipped: entity is null.");
            return true;
        }

        String taxId = taxIdentifiable.taxId();
        ECountryType country = taxIdentifiable.country();

        if (taxId == null && country == null) {
            logger.warn("Validation skipped: taxId is null and country is null.");
            return true;
        }

        if (taxId == null || taxId.isBlank()) {
            logger.warn("Validation failed: tax ID is null or empty.");
            return false;
        }

        if (country == null) {
            logger.warn("Validation failed: country is null.");
            return false;
        }

        logger.info("Validating tax ID '{}' for country '{}'", taxId, country);

        // Get the pattern for the specified country or use the default pattern
        Pattern pattern = TAX_ID_PATTERNS.getOrDefault(country, DEFAULT_PATTERN);

        boolean isValid = pattern.matcher(taxId).matches();

        if (!isValid) {
            String errorMessage = "Invalid tax ID format for " + country + ". " + getTaxIdFormatMessage(country);
            logger.warn("Validation failed: {}", errorMessage);
        } else {
            logger.info("Validation passed for tax ID '{}'", taxId);
        }

        return isValid;
    }
}