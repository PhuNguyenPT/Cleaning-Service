package com.example.cleaning_service.customers.dto.non_profit_org;

import com.example.cleaning_service.customers.dto.DuplicatedValidatable;
import com.example.cleaning_service.customers.enums.ECountryType;
import com.example.cleaning_service.customers.enums.EDay;
import com.example.cleaning_service.customers.enums.EPaymentType;
import com.example.cleaning_service.validator.IRegistrationNumberIdentifiable;
import com.example.cleaning_service.validator.ITaxIdentifiable;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.util.Set;


@Schema(description = "Request DTO for creating a non-profit organization",
        example = """
        {
          "taxId": "52-1693387",
          "registrationNumber": "52-1693387",
          "billingAddress": "789 Charity Lane, Suite 200, Los Angeles, CA 90015, US",
          "paymentMethod": "BANK_TRANSFER",
          "preferredDays": ["TUESDAY", "THURSDAY"],
          "organizationName": "Helping Hands Foundation",
          "address": "123 Main St, Los Angeles, CA 90015",
          "phone": "+12135556789",
          "email": "info@helpinghands.org",
          "city": "Los Angeles",
          "state": "CA",
          "zip": "90015",
          "country": "US",
          "notes": "Requires eco-friendly cleaning products."
        }
        """
)
public record NonProfitOrgRequest(
        @NotBlank String taxId,
        @NotBlank String registrationNumber,

        @Size(min = 10, max = 255, message = "Billing address must be between 10 and 255 characters")
        String billingAddress,
        EPaymentType paymentMethod,
        Set<EDay> preferredDays,

        @NotBlank @Size(min = 2, max = 100, message = "Company name must be between 2 and 100 characters")
        String organizationName,
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
)  implements ITaxIdentifiable, IRegistrationNumberIdentifiable, DuplicatedValidatable
{}
