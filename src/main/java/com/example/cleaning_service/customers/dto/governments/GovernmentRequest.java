package com.example.cleaning_service.customers.dto.governments;

import com.example.cleaning_service.customers.enums.ECountryType;
import com.example.cleaning_service.customers.enums.EDay;
import com.example.cleaning_service.customers.enums.EPaymentType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

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
        String phone,
        String email,
        String city,
        String state,
        String zip,
        @NotNull ECountryType country,
        String notes
) {
}
