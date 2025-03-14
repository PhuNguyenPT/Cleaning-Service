package com.example.cleaning_service.customers.controllers;

import com.example.cleaning_service.customers.dto.CompanyDetailsResponseModel;
import com.example.cleaning_service.customers.dto.CompanyRequest;
import com.example.cleaning_service.customers.dto.CompanyResponseModel;
import com.example.cleaning_service.customers.dto.CompanyUpdateRequest;
import com.example.cleaning_service.customers.services.CompanyService;
import com.example.cleaning_service.security.entities.user.User;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/companies")
@PreAuthorize("hasRole('USER')")
public class CompanyController {
    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    /**
     * Creates a new company and associates it with the authenticated user.
     *
     * @param companyRequest The request body containing company details.
     * @param user The authenticated user.
     * @return The created company response model.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompanyResponseModel createCompany(@RequestBody @Valid CompanyRequest companyRequest,
                                              @AuthenticationPrincipal User user) {
        return companyService.createCompany(companyRequest, user);
    }

    /**
     * Retrieves company details by ID for the authenticated user.
     *
     * @param id The UUID of the company.
     * @param user The authenticated user.
     * @return The company details response model.
     */
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CompanyDetailsResponseModel getCompanyById(@PathVariable UUID id,
                                                      @AuthenticationPrincipal User user) {
        return companyService.getCompanyDetailsResponseModelById(id, user);
    }

    /**
     * Updates company details. Only non-null fields are updated.
     *
     * @param id The UUID of the company to update.
     * @param updateRequest The request body containing fields to update.
     * @param user The authenticated user.
     * @return The updated company details.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CompanyDetailsResponseModel> updateCompany(@PathVariable UUID id,
                                                                     @RequestBody @Valid CompanyUpdateRequest updateRequest,
                                                                     @AuthenticationPrincipal User user) {
        CompanyDetailsResponseModel updatedCompany = companyService.updateCompanyDetails(id, updateRequest, user);
        return ResponseEntity.ok(updatedCompany);
    }
}
