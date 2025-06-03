package com.example.cleaning_service.customers.controllers;

import com.example.cleaning_service.customers.assemblers.accounts.AccountDetailsModelAssembler;
import com.example.cleaning_service.customers.assemblers.accounts.AdminAccountDetailsModelAssembler;
import com.example.cleaning_service.customers.assemblers.accounts.AdminAccountModelAssembler;
import com.example.cleaning_service.customers.assemblers.companies.AdminCompanyDetailsModelAssembler;
import com.example.cleaning_service.customers.assemblers.governments.AdminGovernmentDetailsModelAssembler;
import com.example.cleaning_service.customers.assemblers.individuals.AdminIndividualCustomerDetailsModelAssembler;
import com.example.cleaning_service.customers.assemblers.non_profit_org.AdminNonProfitOrgDetailsModelAssembler;
import com.example.cleaning_service.customers.dto.accounts.AccountDetailsResponseModel;
import com.example.cleaning_service.customers.dto.accounts.AccountResponseModel;
import com.example.cleaning_service.customers.dto.accounts.AccountUpdateRequest;
import com.example.cleaning_service.customers.dto.companies.CompanyDetailsResponseModel;
import com.example.cleaning_service.customers.dto.governments.GovernmentDetailsResponseModel;
import com.example.cleaning_service.customers.dto.individuals.IndividualCustomerDetailsResponseModel;
import com.example.cleaning_service.customers.dto.non_profit_org.NonProfitOrgDetailsResponseModel;
import com.example.cleaning_service.customers.entities.*;
import com.example.cleaning_service.customers.repositories.AccountRepository;
import com.example.cleaning_service.customers.services.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
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
    private final PagedResourcesAssembler<Account> pagedResourcesAssembler;
    private final AdminAccountModelAssembler adminAccountModelAssembler;
    private final OrganizationDetailsService organizationDetailsService;
    private final AccountRepository accountRepository;
    private final AdminAccountDetailsModelAssembler adminAccountDetailsModelAssembler;
    private final AdminCompanyDetailsModelAssembler adminCompanyDetailsModelAssembler;
    private final AdminGovernmentDetailsModelAssembler adminGovernmentDetailsModelAssembler;
    private final AdminIndividualCustomerDetailsModelAssembler adminIndividualCustomerDetailsModelAssembler;
    private final AdminNonProfitOrgDetailsModelAssembler adminNonProfitOrgDetailsModelAssembler;
    private final AccountDetailsModelAssembler accountDetailsModelAssembler;

    public AdminCustomerController(
            AccountService accountService,
            CompanyService companyService,
            GovernmentService governmentService,
            IndividualCustomerService individualCustomerService,
            NonProfitOrgService nonProfitOrgService,
            @Qualifier("pagedResourcesAssemblerAccount") PagedResourcesAssembler<Account> pagedResourcesAssembler,
            AdminAccountModelAssembler adminAccountModelAssembler,
            OrganizationDetailsService organizationDetailsService,
            AccountRepository accountRepository, AdminAccountDetailsModelAssembler adminAccountDetailsModelAssembler,
            AdminCompanyDetailsModelAssembler adminCompanyDetailsModelAssembler,
            AdminGovernmentDetailsModelAssembler adminGovernmentDetailsModelAssembler,
            AdminIndividualCustomerDetailsModelAssembler adminIndividualCustomerDetailsModelAssembler,
            AdminNonProfitOrgDetailsModelAssembler adminNonProfitOrgDetailsModelAssembler,
            AccountDetailsModelAssembler accountDetailsModelAssembler) {
        this.accountService = accountService;
        this.companyService = companyService;
        this.governmentService = governmentService;
        this.individualCustomerService = individualCustomerService;
        this.nonProfitOrgService = nonProfitOrgService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.adminAccountModelAssembler = adminAccountModelAssembler;
        this.organizationDetailsService = organizationDetailsService;
        this.accountRepository = accountRepository;
        this.adminAccountDetailsModelAssembler = adminAccountDetailsModelAssembler;
        this.adminCompanyDetailsModelAssembler = adminCompanyDetailsModelAssembler;
        this.adminGovernmentDetailsModelAssembler = adminGovernmentDetailsModelAssembler;
        this.adminIndividualCustomerDetailsModelAssembler = adminIndividualCustomerDetailsModelAssembler;
        this.adminNonProfitOrgDetailsModelAssembler = adminNonProfitOrgDetailsModelAssembler;
        this.accountDetailsModelAssembler = accountDetailsModelAssembler;
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
        Page<Account> accountPage = accountRepository.findAll(pageable);
        PagedModel<AccountResponseModel> accountDetailsResponseModelPagedModel = pagedResourcesAssembler.toModel(
                accountPage, adminAccountModelAssembler
        );
        log.info("Retrieved account page model {}", accountDetailsResponseModelPagedModel);
        Map<UUID, Link> uuidLinkMap = new HashMap<>();
        organizationDetailsService.addLinksForIOrganization(
                uuidLinkMap,
                accountPage,
                accountDetailsResponseModelPagedModel,
                Account::getId,
                AccountResponseModel::getId,
                Account::getIOrganization,
                organizationDetailsService::getAdminCustomerLinkByIOrganization,
                AccountResponseModel::addSingleLink
        );

        return accountDetailsResponseModelPagedModel;
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
        Account account = accountService.findById(id);
        AccountDetailsResponseModel accountDetailsResponseModel = adminAccountDetailsModelAssembler.toModel(account);
        log.info("Retrieved admin finding: account details model {}", accountDetailsResponseModel);
        return accountDetailsResponseModel;
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
        Account patchedAccount = accountService.patchAccountDetailsById(id, accountUpdateRequest);
        AccountDetailsResponseModel accountDetailsResponseModel = accountDetailsModelAssembler.toModel(patchedAccount);
        log.info("Patched account details model {}", accountDetailsResponseModel);
        return accountDetailsResponseModel;
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
        Company company = companyService.findById(id);
        CompanyDetailsResponseModel companyDetailsResponseModel = adminCompanyDetailsModelAssembler.toModel(company);
        log.info("Successfully retrieved admin company details response model: {}", companyDetailsResponseModel);
        return companyDetailsResponseModel;
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
        Government government = governmentService.findById(id);
        GovernmentDetailsResponseModel governmentDetailsResponseModel = adminGovernmentDetailsModelAssembler.toModel(government);
        log.info("Successfully retrieved admin government details response model: {}", governmentDetailsResponseModel);
        return governmentDetailsResponseModel;
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
        IndividualCustomer individualCustomer = individualCustomerService.findById(id);
        IndividualCustomerDetailsResponseModel individualCustomerDetailsResponseModel =
                adminIndividualCustomerDetailsModelAssembler.toModel(individualCustomer);
        log.info("Successfully retrieved admin individual customer details response model: {}", individualCustomerDetailsResponseModel);
        return individualCustomerDetailsResponseModel;
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
        NonProfitOrg nonProfitOrg = nonProfitOrgService.findById(id);
        NonProfitOrgDetailsResponseModel nonProfitOrgDetailsResponseModel =
                adminNonProfitOrgDetailsModelAssembler.toModel(nonProfitOrg);
        log.info("Successfully retrieved admin non-profit organization details response model: {}", nonProfitOrgDetailsResponseModel);
        return nonProfitOrgDetailsResponseModel;
    }
}
