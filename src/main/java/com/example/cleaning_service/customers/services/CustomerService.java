package com.example.cleaning_service.customers.services;

import com.example.cleaning_service.customers.ICustomer;
import com.example.cleaning_service.customers.entities.Company;
import com.example.cleaning_service.customers.entities.Government;
import com.example.cleaning_service.customers.entities.IndividualCustomer;
import com.example.cleaning_service.customers.entities.NonProfitOrg;
import com.example.cleaning_service.customers.repositories.CompanyRepository;
import com.example.cleaning_service.customers.repositories.GovernmentRepository;
import com.example.cleaning_service.customers.repositories.IndividualCustomerRepository;
import com.example.cleaning_service.customers.repositories.NonProfitOrgRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerService {
    private final CompanyRepository companyRepository;
    private final GovernmentRepository governmentRepository;
    private final NonProfitOrgRepository nonProfitOrgRepository;
    private final IndividualCustomerRepository individualCustomerRepository;

    public CustomerService(CompanyRepository companyRepository, GovernmentRepository governmentRepository, NonProfitOrgRepository nonProfitOrgRepository, IndividualCustomerRepository individualCustomerRepository) {
        this.companyRepository = companyRepository;
        this.governmentRepository = governmentRepository;
        this.nonProfitOrgRepository = nonProfitOrgRepository;
        this.individualCustomerRepository = individualCustomerRepository;
    }

    public Company createCompany(Company company) {
        return companyRepository.save(company);
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
        List<ICustomer> allCustomers = new ArrayList<>();
        allCustomers.addAll(companyRepository.findAll());
        allCustomers.addAll(governmentRepository.findAll());
        allCustomers.addAll(nonProfitOrgRepository.findAll());
        allCustomers.addAll(individualCustomerRepository.findAll());
        return allCustomers;
    }
}

