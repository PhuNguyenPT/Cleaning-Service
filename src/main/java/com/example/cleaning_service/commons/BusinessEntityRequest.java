package com.example.cleaning_service.commons;

import com.example.cleaning_service.customers.enums.ECountryType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record BusinessEntityRequest(
        @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
        String name,
        @Size(min = 5, max = 200, message = "Address must be between 5 and 200 characters")
        String address,
        @Pattern(
                regexp = "^\\+?[1-9]\\d{1,14}$",
                message = "Invalid international phone number format. " +
                        "Expected format: optional '+' followed by 2 to 15 digits. " +
                        "Examples: +1234567890, 1234567890."
        )
        String phone,
        @Email(
                regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$",
                message = "Invalid email format. " +
                        "Expected format: local-part@domain.extension. " +
                        "- Local part: letters, numbers, dots (.), underscores (_), percent (%), plus (+), or hyphens (-). " +
                        "- Domain: letters, numbers, dots (.) and hyphens (-). " +
                        "- Extension: 2 to 6 letters (e.g., .com, .net, .org). " +
                        "Examples: user@example.com, john.doe@company.org."
        )
        String email,
        @Pattern(regexp = "^[a-zA-Z\\s-]{2,50}$",
                message = "City name must be 2-50 characters and contain only letters, spaces, and hyphens")
        String city,
        @Pattern(regexp = "^[a-zA-Z\\s-]{2,50}$",
                message = "City name must be 2-50 characters and contain only letters, spaces, and hyphens")
        String state,
        @Pattern(regexp = "^[A-Z0-9\\s-]{2,10}$",
                message = "Postal code format varies by country. Must be 2-10 characters including letters, numbers, spaces, and hyphens.")
        String zip,
        ECountryType country,
        @Size(max = 500, message = "Notes cannot exceed 500 characters")
        String notes
) {}