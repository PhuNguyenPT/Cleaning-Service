package com.example.cleaning_service.customers.dto.governments;

import com.example.cleaning_service.commons.BusinessEntityRequest;
import com.example.cleaning_service.customers.dto.AbstractCustomerRequest;
import com.example.cleaning_service.customers.dto.OrganizationDetailsRequest;
import com.example.cleaning_service.customers.enums.ECountryType;
import com.example.cleaning_service.validator.IRegistrationNumberIdentifiable;
import com.example.cleaning_service.validator.ITaxIdentifiable;
import com.example.cleaning_service.validator.ValidRegistrationNumber;
import com.example.cleaning_service.validator.ValidTaxId;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

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
public record GovernmentUpdateRequest (
        OrganizationDetailsRequest organizationDetails,

        @Size(min = 2, max = 100, message = "Contractor name must be between 2 and 100 characters")
        @Pattern(regexp = "^[a-zA-Z\\s-]+$", message = "Contractor name must contain only letters, spaces, and hyphens")
        String contractorName,
        @Size(min = 2, max = 100, message = "Department name must be between 2 and 100 characters")
        @Pattern(regexp = "^[a-zA-Z\\s-]+$", message = "Department name must contain only letters, spaces, and hyphens")
        String departmentName,
        Boolean isTaxExempt,
        Boolean requiresEmergencyCleaning,

        AbstractCustomerRequest customerDetails,

        BusinessEntityRequest businessEntityDetails
) implements ITaxIdentifiable, IRegistrationNumberIdentifiable
{
    @Override
    public String getRegistrationNumber() {
        return organizationDetails.registrationNumber();
    }

    @Override
    public String getTaxId() {
        return organizationDetails.taxId();
    }

    @Override
    public ECountryType getCountry() {
        return businessEntityDetails.country();
    }
}