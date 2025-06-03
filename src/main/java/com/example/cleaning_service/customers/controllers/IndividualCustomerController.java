package com.example.cleaning_service.customers.controllers;

import com.example.cleaning_service.customers.assemblers.individuals.IndividualCustomerDetailsModelAssembler;
import com.example.cleaning_service.customers.assemblers.individuals.IndividualCustomerModelAssembler;
import com.example.cleaning_service.customers.dto.individuals.IndividualCustomerDetailsResponseModel;
import com.example.cleaning_service.customers.dto.individuals.IndividualCustomerRequest;
import com.example.cleaning_service.customers.dto.individuals.IndividualCustomerResponseModel;
import com.example.cleaning_service.customers.dto.individuals.IndividualCustomerUpdateRequest;
import com.example.cleaning_service.customers.entities.IndividualCustomer;
import com.example.cleaning_service.customers.services.IndividualCustomerService;
import com.example.cleaning_service.security.entities.user.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/individuals")
@Tag(name = "User Individual Customers", description = "Individual Customers management APIs")
@SecurityRequirement(name = "bearerAuth")
public class IndividualCustomerController {
    private final IndividualCustomerService individualCustomerService;
    private final IndividualCustomerModelAssembler individualCustomerModelAssembler;
    private final IndividualCustomerDetailsModelAssembler individualCustomerDetailsModelAssembler;

    public IndividualCustomerController(IndividualCustomerService individualCustomerService, IndividualCustomerModelAssembler individualCustomerModelAssembler, IndividualCustomerDetailsModelAssembler individualCustomerDetailsModelAssembler) {
        this.individualCustomerService = individualCustomerService;
        this.individualCustomerModelAssembler = individualCustomerModelAssembler;
        this.individualCustomerDetailsModelAssembler = individualCustomerDetailsModelAssembler;
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
        IndividualCustomer individualCustomer = individualCustomerService.createIndividualCustomer(individualCustomerRequest, user);
        IndividualCustomerResponseModel individualCustomerResponseModel = individualCustomerModelAssembler
                .toModel(individualCustomer);
        log.info("Successfully created individual customer response: {}", individualCustomerResponseModel);

        return individualCustomerResponseModel;
    }

    @Operation(summary = "Get Individual Customer by ID", description = "Retrieves an individual customer by ID for the authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Individual customer retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Individual customer not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public IndividualCustomerDetailsResponseModel getIndividualCustomerById(@PathVariable UUID id,
                                                                            @AuthenticationPrincipal User user) {
        IndividualCustomer individualCustomer = individualCustomerService.getIndividualCustomerDetailsById(id, user);
        IndividualCustomerDetailsResponseModel individualCustomerDetailsResponseModel =
                individualCustomerDetailsModelAssembler.toModel(individualCustomer);
        log.info("Retrieved individual details: {}", individualCustomerDetailsResponseModel);
        return individualCustomerDetailsResponseModel;
    }

    @Operation(summary = "Update Individual Customer", description = "Updates an individual customer's details. Only non-null fields are updated.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Individual customer updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "403", description = "User not authorized to update this individual customer"),
            @ApiResponse(responseCode = "404", description = "Individual customer not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PreAuthorize("hasRole('CUSTOMER')")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public IndividualCustomerDetailsResponseModel updateIndividualCustomerById(@PathVariable UUID id,
                                                                               @RequestBody @Valid IndividualCustomerUpdateRequest updateRequest,
                                                                               @AuthenticationPrincipal User user) {
        IndividualCustomer individualCustomer = individualCustomerService.updateIndividualCustomerDetailsById(id, updateRequest, user);
        return individualCustomerDetailsModelAssembler.toModel(individualCustomer);
    }

    @Operation(summary = "Delete Individual Customer", description = "Deletes an individual customer by ID for the authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Individual customer deleted successfully"),
            @ApiResponse(responseCode = "403", description = "User not authorized to delete this individual customer"),
            @ApiResponse(responseCode = "404", description = "Individual customer not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PreAuthorize("hasRole('CUSTOMER')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteIndividualCustomerById(@PathVariable UUID id, @AuthenticationPrincipal User user) {
        individualCustomerService.deleteIndividualCustomerById(id, user);
    }
}