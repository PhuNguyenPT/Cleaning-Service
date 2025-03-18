package com.example.cleaning_service.customers.dto.governments;

import com.example.cleaning_service.customers.dto.DuplicatedValidatable;
import com.example.cleaning_service.customers.enums.ECountryType;
import com.example.cleaning_service.customers.enums.EDay;
import com.example.cleaning_service.customers.enums.EPaymentType;
import com.example.cleaning_service.validator.IRegistrationNumberIdentifiable;
import com.example.cleaning_service.validator.ITaxIdentifiable;
import com.example.cleaning_service.validator.ValidRegistrationNumber;
import com.example.cleaning_service.validator.ValidTaxId;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.util.Set;

@Schema(description = "Request DTO for creating a government entity",
        example = """
        {
          "taxId": "12-3456789",
          "registrationNumber": "12-3456789",
          "contractorName": "Jane Smith",
          "departmentName": "Environmental Protection",
          "isTaxExempt": true,
          "requiresEmergencyCleaning": false,
          "billingAddress": "1200 Pennsylvania Avenue NW, Washington, DC 20460, US",
          "paymentMethod": "BANK_TRANSFER",
          "preferredDays": ["TUESDAY", "FRIDAY"],
          "governmentName": "Environmental Protection Agency",
          "address": "1200 Pennsylvania Avenue NW",
          "phone": "+12025551000",
          "email": "contact@epa.gov",
          "city": "Washington",
          "state": "DC",
          "zip": "20460",
          "country": "US",
          "notes": "Requires background checks for all cleaning staff and eco-friendly cleaning products only."
        }
        """
)
@ValidTaxId
@ValidRegistrationNumber
public record GovernmentRequest(
        @NotBlank String taxId,
        @NotBlank String registrationNumber,

        @Size(min = 2, max = 100, message = "Contractor name must be between 2 and 100 characters")
        @Pattern(regexp = "^[a-zA-Z\\s-]+$", message = "Contractor name must contain only letters, spaces, and hyphens")
        String contractorName,
        @Size(min = 2, max = 100, message = "Department name must be between 2 and 100 characters")
        @Pattern(regexp = "^[a-zA-Z\\s-]+$", message = "Department name must contain only letters, spaces, and hyphens")
        String departmentName,
        Boolean isTaxExempt,
        Boolean requiresEmergencyCleaning,

        @Size(min = 10, max = 255, message = "Billing address must be between 10 and 255 characters")
        String billingAddress,
        EPaymentType paymentMethod,
        Set<EDay> preferredDays,

        @NotBlank @Size(min = 2, max = 100, message = "Company name must be between 2 and 100 characters")
        String governmentName,
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
        @NotNull ECountryType country,
        @Size(max = 500, message = "Notes cannot exceed 500 characters")
        String notes
) implements ITaxIdentifiable, IRegistrationNumberIdentifiable, DuplicatedValidatable
{}
