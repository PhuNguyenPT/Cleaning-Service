package com.example.cleaning_service.customers.configs;

import com.example.cleaning_service.customers.assemblers.accounts.AccountDetailsModelAssembler;
import com.example.cleaning_service.customers.assemblers.accounts.AccountModelAssembler;
import com.example.cleaning_service.customers.assemblers.accounts.AdminAccountDetailsModelAssembler;
import com.example.cleaning_service.customers.assemblers.accounts.AdminAccountModelAssembler;
import com.example.cleaning_service.customers.assemblers.companies.AdminCompanyDetailsModelAssembler;
import com.example.cleaning_service.customers.assemblers.companies.CompanyDetailsModelAssembler;
import com.example.cleaning_service.customers.assemblers.companies.CompanyModelAssembler;
import com.example.cleaning_service.customers.assemblers.governments.AdminGovernmentDetailsModelAssembler;
import com.example.cleaning_service.customers.assemblers.governments.GovernmentDetailsModelAssembler;
import com.example.cleaning_service.customers.assemblers.governments.GovernmentModelAssembler;
import com.example.cleaning_service.customers.assemblers.individuals.AdminIndividualCustomerDetailsModelAssembler;
import com.example.cleaning_service.customers.assemblers.individuals.IndividualCustomerDetailsModelAssembler;
import com.example.cleaning_service.customers.assemblers.individuals.IndividualCustomerModelAssembler;
import com.example.cleaning_service.customers.assemblers.non_profit_org.AdminNonProfitOrgDetailsModelAssembler;
import com.example.cleaning_service.customers.assemblers.non_profit_org.NonProfitOrgDetailModelAssembler;
import com.example.cleaning_service.customers.assemblers.non_profit_org.NonProfitOrgModelAssembler;
import com.example.cleaning_service.customers.controllers.*;
import com.example.cleaning_service.customers.dto.accounts.AccountDetailsResponseModel;
import com.example.cleaning_service.customers.dto.accounts.AccountResponseModel;
import com.example.cleaning_service.customers.dto.companies.CompanyDetailsResponseModel;
import com.example.cleaning_service.customers.dto.companies.CompanyResponseModel;
import com.example.cleaning_service.customers.dto.governments.GovernmentDetailsResponseModel;
import com.example.cleaning_service.customers.dto.governments.GovernmentResponseModel;
import com.example.cleaning_service.customers.dto.individuals.IndividualCustomerDetailsResponseModel;
import com.example.cleaning_service.customers.dto.individuals.IndividualCustomerResponseModel;
import com.example.cleaning_service.customers.dto.non_profit_org.NonProfitOrgDetailsResponseModel;
import com.example.cleaning_service.customers.dto.non_profit_org.NonProfitOrgResponseModel;
import com.example.cleaning_service.customers.mappers.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomerModelAssemblerConfig {

    @Bean(name = {"accountDetailsModelAssembler"})
    AccountDetailsModelAssembler accountDetailsModelAssembler(AccountMapper accountMapper) {
        return new AccountDetailsModelAssembler(AccountController.class, AccountDetailsResponseModel.class, accountMapper);
    }

    @Bean(name = {"accountModelAssembler"})
    AccountModelAssembler accountModelAssembler(AccountMapper accountMapper) {
        return new AccountModelAssembler(AccountController.class, AccountResponseModel.class, accountMapper);
    }

    @Bean(name = {"adminAccountDetailsModelAssembler"})
    AdminAccountDetailsModelAssembler adminAccountDetailsModelAssembler(AccountMapper accountMapper) {
        return new AdminAccountDetailsModelAssembler(AdminCustomerController.class, AccountDetailsResponseModel.class, accountMapper);
    }

    @Bean(name = {"adminAccountModelAssembler"})
    AdminAccountModelAssembler adminAccountModelAssembler(AccountMapper accountMapper) {
        return new AdminAccountModelAssembler(AdminCustomerController.class, AccountResponseModel.class, accountMapper);
    }

    @Bean(name = {"adminCompanyDetailsModelAssembler"})
    AdminCompanyDetailsModelAssembler adminCompanyDetailsModelAssembler(CompanyMapper companyMapper) {
        return new AdminCompanyDetailsModelAssembler(AdminCustomerController.class, CompanyDetailsResponseModel.class, companyMapper);
    }

    @Bean(name = {"companyDetailsModelAssembler"})
    CompanyDetailsModelAssembler companyDetailsModelAssembler(CompanyMapper companyMapper) {
        return new CompanyDetailsModelAssembler(CompanyController.class, CompanyDetailsResponseModel.class, companyMapper);
    }

    @Bean(name = {"companyModelAssembler"})
    CompanyModelAssembler companyModelAssembler(CompanyMapper companyMapper) {
        return new CompanyModelAssembler(CompanyController.class, CompanyResponseModel.class, companyMapper);
    }

    @Bean(name = {"adminGovernmentDetailsModelAssembler"})
    AdminGovernmentDetailsModelAssembler adminGovernmentDetailsModelAssembler(GovernmentMapper governmentMapper) {
        return new AdminGovernmentDetailsModelAssembler(AdminCustomerController.class, GovernmentDetailsResponseModel.class, governmentMapper);
    }

    @Bean(name = {"governmentDetailsModelAssembler"})
    GovernmentDetailsModelAssembler governmentDetailsModelAssembler(GovernmentMapper governmentMapper) {
        return new GovernmentDetailsModelAssembler(GovernmentController.class, GovernmentDetailsResponseModel.class, governmentMapper);
    }

    @Bean(name = {"governmentModelAssembler"})
    GovernmentModelAssembler governmentModelAssembler(GovernmentMapper governmentMapper) {
        return new GovernmentModelAssembler(GovernmentController.class, GovernmentResponseModel.class, governmentMapper);
    }

    @Bean(name = {"adminIndividualCustomerDetailsModelAssembler"})
    AdminIndividualCustomerDetailsModelAssembler adminIndividualCustomerDetailsModelAssembler(IndividualCustomerMapper individualCustomerMapper) {
        return new AdminIndividualCustomerDetailsModelAssembler(AdminCustomerController.class, IndividualCustomerDetailsResponseModel.class, individualCustomerMapper);
    }

    @Bean(name = {"individualCustomerDetailsModelAssembler"})
    IndividualCustomerDetailsModelAssembler individualCustomerDetailsModelAssembler(IndividualCustomerMapper individualCustomerMapper) {
        return new IndividualCustomerDetailsModelAssembler(IndividualCustomerController.class, IndividualCustomerDetailsResponseModel.class, individualCustomerMapper);
    }

    @Bean(name = {"individualCustomerModelAssembler"})
    IndividualCustomerModelAssembler individualCustomerModelAssembler(IndividualCustomerMapper individualCustomerMapper) {
        return new IndividualCustomerModelAssembler(IndividualCustomerController.class, IndividualCustomerResponseModel.class, individualCustomerMapper);
    }

    @Bean(name = {"adminNonProfitOrgDetailsModelAssembler"})
    AdminNonProfitOrgDetailsModelAssembler adminNonProfitOrgDetailsModelAssembler(NonProfitOrgMapper nonProfitOrgMapper) {
        return new AdminNonProfitOrgDetailsModelAssembler(AdminCustomerController.class, NonProfitOrgDetailsResponseModel.class, nonProfitOrgMapper);
    }

    @Bean(name = {"nonProfitOrgDetailModelAssembler"})
    NonProfitOrgDetailModelAssembler nonProfitOrgDetailModelAssembler(NonProfitOrgMapper nonProfitOrgMapper) {
        return new NonProfitOrgDetailModelAssembler(NonProfitOrgController.class, NonProfitOrgDetailsResponseModel.class, nonProfitOrgMapper);
    }

    @Bean(name = {"nonProfitOrgModelAssembler"})
    NonProfitOrgModelAssembler nonProfitOrgModelAssembler(NonProfitOrgMapper nonProfitOrgMapper) {
        return new NonProfitOrgModelAssembler(NonProfitOrgController.class, NonProfitOrgResponseModel.class, nonProfitOrgMapper);
    }
}
