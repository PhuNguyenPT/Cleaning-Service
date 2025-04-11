package com.example.cleaning_service.customers.controllers;

import com.example.cleaning_service.customers.dto.inidividuals.IndividualCustomerDetailsResponseModel;
import com.example.cleaning_service.customers.dto.inidividuals.IndividualCustomerRequest;
import com.example.cleaning_service.customers.dto.inidividuals.IndividualCustomerResponseModel;
import com.example.cleaning_service.customers.dto.inidividuals.IndividualCustomerUpdateRequest;
import com.example.cleaning_service.customers.services.IndividualCustomerService;
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
@RequestMapping("/individuals")
@Tag(name = "User Individual Customers", description = "Individual Customers management APIs")
@SecurityRequirement(name = "bearerAuth")
public class IndividualCustomerController {
    private final IndividualCustomerService individualCustomerService;

    public IndividualCustomerController(IndividualCustomerService individualCustomerService) {
        this.individualCustomerService = individualCustomerService;
    }

    @Operation(summary = "Create an Individual Customer", description = "Creates a new individual customer associated with the authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Individual customer created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PreAuthorize("hasRole('USER')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public IndividualCustomerResponseModel createIndividualCustomer(
            @RequestBody @Valid IndividualCustomerRequest individualCustomerRequest,
            @AuthenticationPrincipal User user
    ) {
        return individualCustomerService.createIndividualCustomer(individualCustomerRequest, user);
    }

    @Operation(summary = "Get Individual Customer by ID", description = "Retrieves an individual customer by ID for the authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Individual customer retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Individual customer not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public IndividualCustomerDetailsResponseModel getIndividualCustomerById(@PathVariable UUID id,
                                                                            @AuthenticationPrincipal User user) {
        return individualCustomerService.getIndividualCustomerDetailsById(id, user);
    }

    @Operation(summary = "Update Individual Customer", description = "Updates an individual customer's details. Only non-null fields are updated.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Individual customer updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "403", description = "User not authorized to update this individual customer"),
            @ApiResponse(responseCode = "404", description = "Individual customer not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public IndividualCustomerDetailsResponseModel updateIndividualCustomerById(@PathVariable UUID id,
                                                                               @RequestBody @Valid IndividualCustomerUpdateRequest updateRequest,
                                                                               @AuthenticationPrincipal User user) {
        return individualCustomerService.updateIndividualCustomerDetailsById(id, updateRequest, user);
    }

    @Operation(summary = "Delete Individual Customer", description = "Deletes an individual customer by ID for the authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Individual customer deleted successfully"),
            @ApiResponse(responseCode = "403", description = "User not authorized to delete this individual customer"),
            @ApiResponse(responseCode = "404", description = "Individual customer not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteIndividualCustomerById(@PathVariable UUID id, @AuthenticationPrincipal User user) {
        individualCustomerService.deleteIndividualCustomerById(id, user);
    }
}