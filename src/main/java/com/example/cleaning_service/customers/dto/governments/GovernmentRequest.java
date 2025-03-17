package com.example.cleaning_service.customers.dto.governments;

import com.example.cleaning_service.customers.enums.ECountryType;
import com.example.cleaning_service.customers.enums.EDay;
import com.example.cleaning_service.customers.enums.EPaymentType;
import com.example.cleaning_service.validator.IRegistrationNumberIdentifiable;
import com.example.cleaning_service.validator.ITaxIdentifiable;
import com.example.cleaning_service.validator.ValidRegistrationNumber;
import com.example.cleaning_service.validator.ValidTaxId;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.Set;

@Schema(description = "Request DTO for creating a government entity",
        example = """
        {
          "taxId": "987654321",
          "registrationNumber": "USGOV123456",
          "contractorName": "John Smith",
          "departmentName": "Public Works",
          "isTaxExempt": true,
          "requiresEmergencyCleaning": false,
          "billingAddress": "100 State Ave, Washington, DC 20500, US",
          "paymentMethod": "BANK_TRANSFER",
          "preferredDays": ["MONDAY", "THURSDAY"],
          "governmentName": "U.S. Department of Public Works",
          "address": "1600 Pennsylvania Ave NW",
          "phone": "+12025550199",
          "email": "contact@publicworks.gov",
          "city": "Washington",
          "state": "DC",
          "zip": "20500",
          "country": "US",
          "notes": "Requires special security clearance"
        }
        """
)
@ValidTaxId
@ValidRegistrationNumber
public record GovernmentRequest(
        @NotBlank String taxId,
        @NotBlank String registrationNumber,

        String contractorName,
        String departmentName,
        boolean isTaxExempt,
        boolean requiresEmergencyCleaning,

        String billingAddress,
        EPaymentType paymentMethod,
        Set<EDay> preferredDays,

        @NotBlank String governmentName,
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
) implements ITaxIdentifiable, IRegistrationNumberIdentifiable
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
