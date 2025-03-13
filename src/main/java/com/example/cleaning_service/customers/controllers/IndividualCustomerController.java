package com.example.cleaning_service.customers.controllers;

import com.example.cleaning_service.customers.dto.inidividuals.IndividualCustomerDetailsResponseModel;
import com.example.cleaning_service.customers.dto.inidividuals.IndividualCustomerRequest;
import com.example.cleaning_service.customers.dto.inidividuals.IndividualCustomerResponseModel;
import com.example.cleaning_service.customers.dto.inidividuals.IndividualCustomerUpdateRequest;
import com.example.cleaning_service.customers.services.IndividualCustomerService;
import com.example.cleaning_service.security.entities.user.User;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/individuals")
public class IndividualCustomerController {
    private final IndividualCustomerService individualCustomerService;

    public IndividualCustomerController(IndividualCustomerService individualCustomerService) {
        this.individualCustomerService = individualCustomerService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public IndividualCustomerResponseModel createIndividualCustomer(
            @Valid IndividualCustomerRequest individualCustomerRequest,
            @AuthenticationPrincipal User user
            ) {
        return individualCustomerService.createIndividualCustomer(individualCustomerRequest, user);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public IndividualCustomerDetailsResponseModel getIndividualCustomerById(@PathVariable UUID id,
                                                                            @AuthenticationPrincipal User user) {
        return individualCustomerService.getIndividualCustomerById(id, user);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public IndividualCustomerDetailsResponseModel updateIndividualCustomerById(@PathVariable UUID id,
                                                                           @RequestBody @Valid IndividualCustomerUpdateRequest updateRequest,
                                                                           @AuthenticationPrincipal User user) {
        return individualCustomerService.updateIndividualCustomerDetailsById(id, updateRequest, user);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteIndividualCustomerById(@PathVariable UUID id, @AuthenticationPrincipal User user) {
        individualCustomerService.deleteIndividualCustomerById(id, user);
    }
}
