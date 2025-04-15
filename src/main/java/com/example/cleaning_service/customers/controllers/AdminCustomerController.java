package com.example.cleaning_service.customers.controllers;

import com.example.cleaning_service.customers.dto.accounts.AccountDetailsResponseModel;
import com.example.cleaning_service.customers.dto.accounts.AccountResponseModel;
import com.example.cleaning_service.customers.dto.accounts.AccountUpdateRequest;
import com.example.cleaning_service.customers.dto.companies.CompanyDetailsResponseModel;
import com.example.cleaning_service.customers.dto.governments.GovernmentDetailsResponseModel;
import com.example.cleaning_service.customers.dto.individuals.IndividualCustomerDetailsResponseModel;
import com.example.cleaning_service.customers.dto.non_profit_org.NonProfitOrgDetailsResponseModel;
import com.example.cleaning_service.customers.services.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/admin")
@Tag(name = "Admin Accounts", description = "Account management APIs")
@SecurityRequirement(name = "bearerAuth")
public class AdminCustomerController {

    private final AccountService accountService;
    private final CompanyService companyService;
    private final GovernmentService governmentService;
    private final IndividualCustomerService individualCustomerService;
    private final NonProfitOrgService nonProfitOrgService;

    public AdminCustomerController(
            AccountService accountService,
            CompanyService companyService,
            GovernmentService governmentService,
            IndividualCustomerService individualCustomerService,
            NonProfitOrgService nonProfitOrgService
    ) {
        this.accountService = accountService;
        this.companyService = companyService;
        this.governmentService = governmentService;
        this.individualCustomerService = individualCustomerService;
        this.nonProfitOrgService = nonProfitOrgService;
    }

    @Operation(
            summary = "Get all accounts (admin)",
            description = "Admin-only endpoint to retrieve a paginated list of all accounts",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Accounts retrieved successfully"
                    ),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden")
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "/accounts", produces = {"application/hal+json"})
    @ResponseStatus(HttpStatus.OK)
    public PagedModel<AccountResponseModel> getAdminAccountDetailsPageModelByPageable(
            @Parameter(description = "Pagination parameters") @ParameterObject Pageable pageable
    ) {
        return accountService.getAdminAccountDetailsPageModelByPageable(pageable);
    }

    @Operation(
            summary = "Get account details by ID (admin)",
            description = "Admin-only endpoint to retrieve detailed account information by ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Account details retrieved successfully"
                    ),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Account not found")
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "/accounts/{id}", produces = {"application/hal+json"})
    @ResponseStatus(HttpStatus.OK)
    public AccountDetailsResponseModel getAdminAccountDetailsResponseModelById(
            @Parameter(description = "Account ID", required = true) @PathVariable UUID id
    ) {
        return accountService.getAdminAccountDetailsResponseModelById(id);
    }

    @Operation(
            summary = "Update account details (admin)",
            description = "Admin-only endpoint to update account information by ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Account updated successfully"
                    ),
                    @ApiResponse(responseCode = "400", description = "Invalid request data"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Account not found")
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping(path = "/accounts/{id}", produces = {"application/hal+json"})
    @ResponseStatus(HttpStatus.OK)
    public AccountDetailsResponseModel patchAccountDetailsById(
            @Parameter(description = "Account ID", required = true) @PathVariable UUID id,
            @Parameter(description = "Account data to update") @RequestBody @Valid AccountUpdateRequest accountUpdateRequest
    ) {
        return accountService.patchAccountDetailsById(id, accountUpdateRequest);
    }

    @Operation(
            summary = "Get company details by ID (admin)",
            description = "Admin-only endpoint to retrieve detailed company information by ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Company details retrieved successfully"
                    ),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Company not found")
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "/companies/{id}", produces = {"application/hal+json"})
    @ResponseStatus(HttpStatus.OK)
    public CompanyDetailsResponseModel getAdminCompanyDetailsResponseModelById(@PathVariable UUID id) {
        return companyService.getAdminCompanyDetailsResponseModelById(id);
    }

    @Operation(
            summary = "Get government details by ID (admin)",
            description = "Admin-only endpoint to retrieve detailed government information by ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Government details retrieved successfully"
                    ),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Government not found")
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "/governments/{id}", produces = {"application/hal+json"})
    @ResponseStatus(HttpStatus.OK)
    public GovernmentDetailsResponseModel getAdminGovernmentDetailsResponseModelById(@PathVariable UUID id) {
        return governmentService.getAdminGovernmentDetailsResponseModelById(id);
    }

    @Operation(
            summary = "Get individual customer details by ID (admin)",
            description = "Admin-only endpoint to retrieve detailed individual customer information by ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Individual customer details retrieved successfully"
                    ),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Individual customer not found")
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "/individuals/{id}", produces = {"application/hal+json"})
    @ResponseStatus(HttpStatus.OK)
    public IndividualCustomerDetailsResponseModel getAdminIndividualCustomerDetailsResponseModelById(@PathVariable UUID id) {
        return individualCustomerService.getAdminIndividualCustomerDetailsResponseModelById(id);
    }

    @Operation(
            summary = "Get non-profit organization details by ID (admin)",
            description = "Admin-only endpoint to retrieve detailed non-profit organization information by ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Non-profit organization details retrieved successfully"
                    ),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Non-profit organization not found")
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "/non-profit-organizations/{id}", produces = {"application/hal+json"})
    @ResponseStatus(HttpStatus.OK)
    public NonProfitOrgDetailsResponseModel getAdminNonProfitOrgDetailsResponseModelById(@PathVariable UUID id) {
        return nonProfitOrgService.getAdminNonProfitOrgDetailsResponseModelById(id);
    }
}
