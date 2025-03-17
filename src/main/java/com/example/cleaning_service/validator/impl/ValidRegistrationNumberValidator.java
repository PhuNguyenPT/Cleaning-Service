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

    static {
        // United States - 2 letters followed by 8 digits
        REGISTRATION_NUMBER_PATTERNS.put(ECountryType.US, Pattern.compile("^[A-Z]{2}\\d{8}$"));

        // Canada - 9 alphanumeric characters
        REGISTRATION_NUMBER_PATTERNS.put(ECountryType.CA, Pattern.compile("^[A-Z0-9]{9}$"));

        // United Kingdom - 8 digits
        REGISTRATION_NUMBER_PATTERNS.put(ECountryType.GB, Pattern.compile("^\\d{8}$"));

        // Germany - HRB followed by up to 8 digits
        REGISTRATION_NUMBER_PATTERNS.put(ECountryType.DE, Pattern.compile("^HRB\\d{1,8}$"));

        // France - 9 digits, may include spaces
        REGISTRATION_NUMBER_PATTERNS.put(ECountryType.FR, Pattern.compile("^\\d{3}\\s?\\d{3}\\s?\\d{3}$"));

        // Australia - ACN format: 9 digits with optional spaces
        REGISTRATION_NUMBER_PATTERNS.put(ECountryType.AU, Pattern.compile("^\\d{3}\\s?\\d{3}\\s?\\d{3}$"));
    }

    private String getRegistrationNumberFormatMessage(ECountryType country) {
        return switch (country) {
            case US -> "Should be 2 uppercase letters followed by 8 digits (e.g., US12345678).";
            case CA -> "Should be 9 alphanumeric characters.";
            case GB -> "Should be 8 digits.";
            case DE -> "Should start with HRB followed by 1-8 digits.";
            case FR -> "Should be 9 digits, may include spaces (e.g., 123 456 789).";
            case AU -> "Should be in ACN format: 9 digits with optional spaces (e.g., 123 456 789).";
            default -> "Should be 5-15 characters including letters, numbers, and hyphens.";
        };
    }

    @Override
    public boolean isValid(IRegistrationNumberIdentifiable entity, ConstraintValidatorContext context) {
        if (entity == null || (entity.getRegistrationNumber() == null && entity.getCountry() == null)) {
            logger.warn("Validation skipped: entity or both registration number and country are null.");
            return true; // Let @NotNull or @NotBlank handle these cases
        }

        String registrationNumber = entity.getRegistrationNumber();
        ECountryType country = entity.getCountry();

        logger.info("Validating registration number '{}' for country '{}'", registrationNumber, country);

        // Get the pattern for the specified country or use default if not found
        Pattern pattern = REGISTRATION_NUMBER_PATTERNS.getOrDefault(country, REGISTRATION_NUMBER_PATTERNS.get(null));
        logger.info("Pattern for country '{}' for registration number '{}' is '{}'", country, registrationNumber, pattern);

        boolean isValid = pattern.matcher(registrationNumber).matches();

        if (!isValid) {
            String errorMessage = "Invalid registration number format for " + country + ". " + getRegistrationNumberFormatMessage(country);
            logger.warn("Validation failed: {}", errorMessage);

            // Customize the error message with country-specific information
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(errorMessage)
                    .addPropertyNode("registrationNumber")
                    .addConstraintViolation();
        }
        logger.info("Validation passed for registration number '{}'", registrationNumber);

        return isValid;
    }
}