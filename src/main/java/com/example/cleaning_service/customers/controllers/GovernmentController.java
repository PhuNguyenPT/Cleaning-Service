package com.example.cleaning_service.customers.controllers;

import com.example.cleaning_service.customers.dto.governments.GovernmentDetailsResponseModel;
import com.example.cleaning_service.customers.dto.governments.GovernmentRequest;
import com.example.cleaning_service.customers.dto.governments.GovernmentResponseModel;
import com.example.cleaning_service.customers.dto.governments.GovernmentUpdateRequest;
import com.example.cleaning_service.customers.services.GovernmentService;
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
@RequestMapping("/governments")
@Tag(name = "User Governments", description = "Governments management APIs")
@SecurityRequirement(name = "bearerAuth")
public class GovernmentController {
    private final GovernmentService governmentService;

    public GovernmentController(GovernmentService governmentService) {
        this.governmentService = governmentService;
    }

    @Operation(summary = "Create a government", description = "Creates a new government entity and associates it with the authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Government created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PreAuthorize("hasRole('USER')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GovernmentResponseModel createGovernment(@RequestBody @Valid GovernmentRequest governmentRequest,
                                                    @AuthenticationPrincipal User user) {
        return governmentService.createGovernment(governmentRequest, user);
    }

    @Operation(summary = "Get government by ID", description = "Retrieves government details by its ID for the authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Government details retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Government not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public GovernmentDetailsResponseModel getGovernmentById(@PathVariable UUID id,
                                                            @AuthenticationPrincipal User user) {
        return governmentService.getGovernmentDetailsResponseModelById(id, user);
    }

    @Operation(summary = "Update government details", description = "Updates a government entity's details. Only non-null fields are updated.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Government updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "403", description = "User not authorized to update this government"),
            @ApiResponse(responseCode = "404", description = "Government not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public GovernmentDetailsResponseModel updateGovernmentById(@PathVariable UUID id,
                                                               @RequestBody @Valid GovernmentUpdateRequest updateRequest,
                                                               @AuthenticationPrincipal User user) {
        return governmentService.updateCompanyDetailsById(id, updateRequest, user);
    }

    @Operation(summary = "Delete a government", description = "Deletes a government entity by ID for the authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Government deleted successfully"),
            @ApiResponse(responseCode = "403", description = "User not authorized to delete this government"),
            @ApiResponse(responseCode = "404", description = "Government not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGovernmentById(@PathVariable UUID id, @AuthenticationPrincipal User user) {
        governmentService.deleteGovernmentById(id, user);
    }
}