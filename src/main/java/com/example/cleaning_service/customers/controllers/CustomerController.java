package com.example.cleaning_service.customers.controllers;

import com.example.cleaning_service.customers.api.ICustomer;
import com.example.cleaning_service.customers.entities.Company;
import com.example.cleaning_service.customers.entities.Government;
import com.example.cleaning_service.customers.entities.IndividualCustomer;
import com.example.cleaning_service.customers.entities.NonProfitOrg;
import com.example.cleaning_service.customers.services.CustomerService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
@SecurityRequirement(name = "bearerAuth")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/companies")
    public ResponseEntity<Company> createCompany(@RequestBody Company company) {
        return ResponseEntity.ok(customerService.createCompany(company));
    }

    @PostMapping("/governments")
    public ResponseEntity<Government> createGovernment(@RequestBody Government government) {
        return ResponseEntity.ok(customerService.createGovernment(government));
    }

    @PostMapping("/non-profits")
    public ResponseEntity<NonProfitOrg> createNonProfit(@RequestBody NonProfitOrg nonProfitOrg) {
        return ResponseEntity.ok(customerService.createNonProfit(nonProfitOrg));
    }

    @PostMapping("/individuals")
    public ResponseEntity<IndividualCustomer> createIndividualCustomer(@RequestBody IndividualCustomer individualCustomer) {
        return ResponseEntity.ok(customerService.createIndividualCustomer(individualCustomer));
    }

    @GetMapping
    public ResponseEntity<List<ICustomer>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }
}
