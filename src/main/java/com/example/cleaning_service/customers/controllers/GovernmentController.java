package com.example.cleaning_service.customers.controllers;

import com.example.cleaning_service.customers.dto.governments.GovernmentDetailsResponseModel;
import com.example.cleaning_service.customers.dto.governments.GovernmentRequest;
import com.example.cleaning_service.customers.dto.governments.GovernmentResponseModel;
import com.example.cleaning_service.customers.dto.governments.GovernmentUpdateRequest;
import com.example.cleaning_service.customers.services.GovernmentService;
import com.example.cleaning_service.security.entities.user.User;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/governments")
public class GovernmentController {
    private final GovernmentService governmentService;

    public GovernmentController(GovernmentService governmentService) {
        this.governmentService = governmentService;
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GovernmentResponseModel createGovernment(@RequestBody @Valid GovernmentRequest governmentRequest,
                                                    @AuthenticationPrincipal User user) {
        return governmentService.createGovernment(governmentRequest, user);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public GovernmentDetailsResponseModel getGovernmentById(@PathVariable UUID id,
                                                            @AuthenticationPrincipal User user) {
        return governmentService.getGovernmentDetailsResponseModelById(id, user);
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public GovernmentDetailsResponseModel updateGovernmentById(@PathVariable UUID id,
                                                               @RequestBody @Valid GovernmentUpdateRequest updateRequest,
                                                               @AuthenticationPrincipal User user) {
        return governmentService.updateCompanyDetailsById(id, updateRequest, user);
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGovernmentById(@PathVariable UUID id, @AuthenticationPrincipal User user) {
        governmentService.deleteGovernmentById(id, user);
    }
}
