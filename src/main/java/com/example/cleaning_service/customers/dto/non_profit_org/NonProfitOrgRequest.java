package com.example.cleaning_service.customers.dto.non_profit_org;

import com.example.cleaning_service.customers.enums.ECountryType;
import com.example.cleaning_service.customers.enums.EDay;
import com.example.cleaning_service.customers.enums.EPaymentType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

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
        String phone,
        String email,
        String city,
        String state,
        String zip,
        @NotNull ECountryType country,
        String notes
) {
}
