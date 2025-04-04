package com.example.cleaning_service.customers.controllers;

import com.example.cleaning_service.customers.dto.companies.CompanyDetailsResponseModel;
import com.example.cleaning_service.customers.dto.companies.CompanyRequest;
import com.example.cleaning_service.customers.dto.companies.CompanyResponseModel;
import com.example.cleaning_service.customers.dto.companies.CompanyUpdateRequest;
import com.example.cleaning_service.customers.services.CompanyService;
import com.example.cleaning_service.security.entities.user.User;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/companies")
@Tag(name = "User Companies", description = "Companies management APIs")
@SecurityRequirement(name = "bearerAuth")
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
    @PreAuthorize("hasRole('USER')")
    @PostMapping(produces = "application/hal+json")
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
    @PreAuthorize("hasRole('USER')")
    @GetMapping(path = "/{id}", produces = "application/hal+json")
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
    @PreAuthorize("hasRole('USER')")
    @PutMapping(path = "/{id}", produces = "application/hal+json")
    @ResponseStatus(HttpStatus.OK)
    public CompanyDetailsResponseModel updateCompany(@PathVariable UUID id,
                                                     @RequestBody @Valid CompanyUpdateRequest updateRequest,
                                                     @AuthenticationPrincipal User user) {
        return companyService.updateCompanyDetailsById(id, updateRequest, user);
    }

    /**
     * Deletes a company by ID for the authenticated user.
     *
     * @param id The UUID of the company.
     */
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping(path = "/{id}", produces = "application/hal+json")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompany(@PathVariable UUID id,
                              @AuthenticationPrincipal User user) {
        companyService.deleteCompanyById(id, user);
    }
}
