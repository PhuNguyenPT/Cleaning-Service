package com.example.cleaning_service.customers.repositories;

import com.example.cleaning_service.customers.entities.IndividualCustomer;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IndividualCustomerRepository extends JpaRepository<IndividualCustomer, UUID> {
    boolean existsByTaxId(@NotBlank String s);

    boolean existsByRegistrationNumber(@NotBlank String s);

    boolean existsByEmail(@Email(
            regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$",
            message = "Invalid email format. " +
                    "Expected format: local-part@domain.extension. " +
                    "- Local part: letters, numbers, dots (.), underscores (_), percent (%), plus (+), or hyphens (-). " +
                    "- Domain: letters, numbers, dots (.) and hyphens (-). " +
                    "- Extension: 2 to 6 letters (e.g., .com, .net, .org). " +
                    "Examples: user@example.com, john.doe@company.org."
    ) String email);
}
