package com.example.cleaning_service.validator.impl;

import com.example.cleaning_service.customers.enums.ECountryType;
import com.example.cleaning_service.validator.IRegistrationNumberIdentifiable;
import com.example.cleaning_service.validator.ValidRegistrationNumber;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.EnumMap;
import java.util.Map;
import java.util.regex.Pattern;

public class ValidRegistrationNumberValidator implements ConstraintValidator<ValidRegistrationNumber, IRegistrationNumberIdentifiable> {
    private static final Logger logger = LoggerFactory.getLogger(ValidRegistrationNumberValidator.class);

    private static final Map<ECountryType, Pattern> REGISTRATION_NUMBER_PATTERNS = new EnumMap<>(ECountryType.class);

    // Default pattern allows 5-15 characters, including letters, numbers, and common separators
    private static final Pattern DEFAULT_PATTERN = Pattern.compile("^[A-Za-z0-9-]{5,15}$");

    static {
        // United States - EIN format: XX-XXXXXXX (2 digits, hyphen, 7 digits)
        REGISTRATION_NUMBER_PATTERNS.put(ECountryType.US, Pattern.compile("^\\d{2}-\\d{7}$"));

        // Canada - Business Number (BN): 9 digits with optional hyphens (XX-XXX-XXX)
        REGISTRATION_NUMBER_PATTERNS.put(ECountryType.CA, Pattern.compile("^\\d{9}$|^\\d{2}-\\d{3}-\\d{3}$"));

        // United Kingdom - Companies House: 8 digits
        REGISTRATION_NUMBER_PATTERNS.put(ECountryType.GB, Pattern.compile("^\\d{8}$"));

        // Germany - Handelsregister: HRB followed by up to 8 digits
        REGISTRATION_NUMBER_PATTERNS.put(ECountryType.DE, Pattern.compile("^HRB\\d{1,8}$"));

        // France - SIREN: 9 digits, may include spaces
        REGISTRATION_NUMBER_PATTERNS.put(ECountryType.FR, Pattern.compile("^\\d{3}\\s?\\d{3}\\s?\\d{3}$"));

        // Australia - ACN format: 9 digits with optional spaces and can include optional "ACN" prefix
        REGISTRATION_NUMBER_PATTERNS.put(ECountryType.AU, Pattern.compile("^(?:ACN\\s?)?\\d{3}\\s?\\d{3}\\s?\\d{3}$"));
    }

    private String getRegistrationNumberFormatMessage(ECountryType country) {
        return switch (country) {
            case US -> "Should be EIN format: XX-XXXXXXX (e.g., 12-3456789).";
            case CA -> "Should be a 9-digit Business Number, with or without hyphens (e.g., 123456789 or 12-345-678).";
            case GB -> "Should be 8 digits (Companies House format).";
            case DE -> "Should start with HRB followed by 1-8 digits (e.g., HRB123456).";
            case FR -> "Should be 9 digits, may include spaces (SIREN format, e.g., 123 456 789).";
            case AU -> "Should be in ACN format: 9 digits with optional spaces and optional 'ACN' prefix (e.g., 123 456 789 or ACN 123 456 789).";
            default -> "Should be 5-15 characters including letters, numbers, and hyphens.";
        };
    }

    @Override
    public boolean isValid(IRegistrationNumberIdentifiable entity, ConstraintValidatorContext context) {
        if (entity == null) {
            logger.warn("Validation skipped: entity is null.");
            return true; // Let @NotNull or @NotBlank handle these cases
        }

        String registrationNumber = entity.registrationNumber();
        ECountryType country = entity.country();

        if (registrationNumber == null && country == null) {
            logger.warn("Validation skipped: registrationNumber is null and country is null.");
            return true;
        }

        if (registrationNumber == null || registrationNumber.isBlank()) {
            logger.warn("Validation failed: registration number is null or empty.");
            return false;
        }

        if (country == null) {
            logger.warn("Validation failed: country is null.");
            return false;
        }

        logger.info("Validating registration number '{}' for country '{}'", registrationNumber, country);

        // Get the pattern for the specified country or use the default pattern
        Pattern pattern = REGISTRATION_NUMBER_PATTERNS.getOrDefault(country, DEFAULT_PATTERN);

        boolean isValid = pattern.matcher(registrationNumber).matches();

        if (!isValid) {
            String errorMessage = "Invalid registration number format for " + country + ". " + getRegistrationNumberFormatMessage(country);
            logger.warn("Validation failed: {}", errorMessage);
        } else {
            logger.info("Validation passed for registration number '{}'", registrationNumber);
        }

        return isValid;
    }
}