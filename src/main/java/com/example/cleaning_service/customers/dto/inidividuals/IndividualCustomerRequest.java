package com.example.cleaning_service.customers.dto.inidividuals;

import com.example.cleaning_service.customers.enums.ECountryType;
import com.example.cleaning_service.customers.enums.EDay;
import com.example.cleaning_service.customers.enums.EPaymentType;
import com.example.cleaning_service.validator.RegistrationNumberIdentifiable;
import com.example.cleaning_service.validator.TaxIdentifiable;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.Set;

@Schema(description = "Request DTO for creating an individual customer",
        example = """
        {
          "taxId": "123456789",
          "registrationNumber": "US987654321",
          "billingAddress": "789 Main Street, Apt 12B, Chicago, IL 60616, US",
          "paymentMethod": "CREDIT_CARD",
          "preferredDays": ["TUESDAY", "FRIDAY"],
          "customerName": "John Doe",
          "address": "456 Elm St",
          "phone": "+13125557890",
          "email": "john.doe@example.com",
          "city": "Chicago",
          "state": "IL",
          "zip": "60616",
          "country": "US",
          "notes": "Prefers afternoon appointments"
        }
        """
)
public record IndividualCustomerRequest(
        String taxId,
        String registrationNumber,

        String billingAddress,
        EPaymentType paymentMethod,
        Set<EDay> preferredDays,

        @NotBlank String customerName,
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
        ) implements TaxIdentifiable, RegistrationNumberIdentifiable
        {
        @Override
        public String getRegistrationNumber() {
                return registrationNumber;
        }

        @Override
        public String getTaxId() {
                return taxId;
        }

        @Override
        public ECountryType getCountry() {
                return country;
        }
}
