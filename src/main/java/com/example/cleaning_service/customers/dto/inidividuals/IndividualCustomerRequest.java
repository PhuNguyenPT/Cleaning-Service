package com.example.cleaning_service.customers.dto.inidividuals;

import com.example.cleaning_service.customers.enums.ECountryType;
import com.example.cleaning_service.customers.enums.EDay;
import com.example.cleaning_service.customers.enums.EPaymentType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

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
        String phone,
        String email,
        String city,
        String state,
        String zip,
        @NotNull ECountryType country,
        String notes
        ) {
}
