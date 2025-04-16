package com.example.cleaning_service.customers.controllers;

import com.example.cleaning_service.customers.dto.companies.CompanyDetailsResponseModel;
import com.example.cleaning_service.customers.dto.companies.CompanyRequest;
import com.example.cleaning_service.customers.dto.companies.CompanyResponseModel;
import com.example.cleaning_service.customers.dto.companies.CompanyUpdateRequest;
import com.example.cleaning_service.customers.services.CompanyService;
import com.example.cleaning_service.security.entities.user.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Create a company", description = "Creates a new company and associates it with the authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Company created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PreAuthorize("hasRole('USER')")
    @PostMapping(produces = "application/hal+json")
    @ResponseStatus(HttpStatus.CREATED)
    public CompanyResponseModel createCompany(@RequestBody @Valid CompanyRequest companyRequest,
                                              @AuthenticationPrincipal User user) {
        return companyService.createCompany(companyRequest, user);
    }

    @Operation(summary = "Get company by ID", description = "Retrieves company details by its ID for the authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Company details retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Company not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping(path = "/{id}", produces = "application/hal+json")
    @ResponseStatus(HttpStatus.OK)
    public CompanyDetailsResponseModel getCompanyById(@PathVariable UUID id,
                                                      @AuthenticationPrincipal User user) {
        return companyService.getCompanyDetailsResponseModelById(id, user);
    }

    @Operation(summary = "Update company details", description = "Updates a company's details. Only non-null fields are updated.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Company updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "403", description = "User not authorized to update this company"),
            @ApiResponse(responseCode = "404", description = "Company not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PreAuthorize("hasRole('CUSTOMER')")
    @PutMapping(path = "/{id}", produces = "application/hal+json")
    @ResponseStatus(HttpStatus.OK)
    public CompanyDetailsResponseModel updateCompany(@PathVariable UUID id,
                                                     @RequestBody @Valid CompanyUpdateRequest updateRequest,
                                                     @AuthenticationPrincipal User user) {
        return companyService.updateCompanyDetailsById(id, updateRequest, user);
    }

    @Operation(summary = "Delete a company", description = "Deletes a company by ID for the authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Company deleted successfully"),
            @ApiResponse(responseCode = "403", description = "User not authorized to delete this company"),
            @ApiResponse(responseCode = "404", description = "Company not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PreAuthorize("hasRole('CUSTOMER')")
    @DeleteMapping(path = "/{id}", produces = "application/hal+json")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompany(@PathVariable UUID id,
                              @AuthenticationPrincipal User user) {
        companyService.deleteCompanyById(id, user);
    }
}