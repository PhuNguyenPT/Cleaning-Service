package com.example.cleaning_service.customers.dto.companies;

import com.example.cleaning_service.validator.RegistrationNumberIdentifiable;
import com.example.cleaning_service.validator.TaxIdentifiable;
import com.example.cleaning_service.customers.enums.ECompanyType;
import com.example.cleaning_service.customers.enums.ECountryType;
import com.example.cleaning_service.customers.enums.EDay;
import com.example.cleaning_service.customers.enums.EPaymentType;
import com.example.cleaning_service.validator.ValidRegistrationNumber;
import com.example.cleaning_service.validator.ValidTaxId;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.util.Set;

@Schema(description = "Request DTO for creating a company",
        example = """
        {
          "companyType": "START_UP",
          "taxId": "123456789",
          "registrationNumber": "US12345678",
          "billingAddress": "123 Finance Avenue, Suite 500, San Francisco, CA 94105, US",
          "paymentMethod": "CASH",
          "preferredDays": ["MONDAY"],
          "companyName": "TechCorp Inc.",
          "address": "456 Elm St",
          "phone": "+18005551234",
          "email": "contact@techcorp.com",
          "city": "New York",
          "state": "NY",
          "zip": "10001",
          "country": "US",
          "notes": "Preferred customer, requires monthly invoicing."
        }
        """
)
@ValidTaxId
@ValidRegistrationNumber
public record CompanyRequest(
        @NotNull ECompanyType companyType,

        @NotBlank String taxId,

        @NotBlank String registrationNumber,

        @Size(min = 10, max = 255, message = "Billing address must be between 10 and 255 characters")
        String billingAddress,
        EPaymentType paymentMethod,
        Set<EDay> preferredDays,

        @NotBlank @Size(min = 2, max = 100, message = "Company name must be between 2 and 100 characters")
        String companyName,
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
