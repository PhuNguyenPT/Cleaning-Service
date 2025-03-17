package com.example.cleaning_service.commons;

import com.example.cleaning_service.customers.enums.ECountryType;
import jakarta.validation.constraints.Email;

public record BusinessEntityRequest(
        String name,
        String address,
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
        String city,
        String state,
        String zip,
        ECountryType country,
        String notes
) {}