package com.example.cleaning_service.customers.dto.companies;

import com.example.cleaning_service.customers.enums.ECompanyType;
import com.example.cleaning_service.customers.enums.ECountryType;
import com.example.cleaning_service.customers.enums.EDay;
import com.example.cleaning_service.customers.enums.EPaymentType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

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
public record CompanyRequest(
        @NotNull ECompanyType companyType,

        @NotBlank String taxId,

        @NotBlank String registrationNumber,

        String billingAddress,
        EPaymentType paymentMethod,
        Set<EDay> preferredDays,

        @NotBlank String companyName,
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
