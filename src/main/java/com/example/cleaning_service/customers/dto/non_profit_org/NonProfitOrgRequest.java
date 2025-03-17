package com.example.cleaning_service.customers.dto.non_profit_org;

import com.example.cleaning_service.customers.enums.ECountryType;
import com.example.cleaning_service.customers.enums.EDay;
import com.example.cleaning_service.customers.enums.EPaymentType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.Set;


@Schema(description = "Request DTO for creating a non-profit organization",
        example = """
        {
          "taxId": "456789123",
          "registrationNumber": "USNPO987654",
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

        String billingAddress,
        EPaymentType paymentMethod,
        Set<EDay> preferredDays,

        @NotBlank String organizationName,
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
        String city,
        String state,
        String zip,
        @NotNull ECountryType country,
        String notes
) {
}
