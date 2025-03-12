package com.example.cleaning_service.customers.services;

import com.example.cleaning_service.customers.entities.ICustomer;
import com.example.cleaning_service.customers.entities.Government;
import com.example.cleaning_service.customers.entities.IndividualCustomer;
import com.example.cleaning_service.customers.entities.NonProfitOrg;
import com.example.cleaning_service.customers.repositories.GovernmentRepository;
import com.example.cleaning_service.customers.repositories.IndividualCustomerRepository;
import com.example.cleaning_service.customers.repositories.NonProfitOrgRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerService {
    private final GovernmentRepository governmentRepository;
    private final NonProfitOrgRepository nonProfitOrgRepository;
    private final IndividualCustomerRepository individualCustomerRepository;

    public CustomerService(GovernmentRepository governmentRepository, NonProfitOrgRepository nonProfitOrgRepository, IndividualCustomerRepository individualCustomerRepository) {
        this.governmentRepository = governmentRepository;
        this.nonProfitOrgRepository = nonProfitOrgRepository;
        this.individualCustomerRepository = individualCustomerRepository;
    }

    public Government createGovernment(Government government) {
        return governmentRepository.save(government);
    }

    public NonProfitOrg createNonProfit(NonProfitOrg nonProfitOrg) {
        return nonProfitOrgRepository.save(nonProfitOrg);
    }

    public IndividualCustomer createIndividualCustomer(IndividualCustomer individualCustomer) {
        return individualCustomerRepository.save(individualCustomer);
    }

    public List<ICustomer> getAllCustomers() {
        return new ArrayList<>();
    }
}

