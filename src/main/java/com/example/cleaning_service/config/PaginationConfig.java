package com.example.cleaning_service.config;

import com.example.cleaning_service.customers.entities.Account;
import com.example.cleaning_service.security.entities.user.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;

@Configuration
public class PaginationConfig {

    @Bean
    public PageableHandlerMethodArgumentResolverCustomizer paginationCustomizer() {
        return resolver -> resolver.setOneIndexedParameters(true);
    }

    @Bean
    public PagedResourcesAssembler<User> pagedResourcesAssemblerUser(PagedResourcesAssembler<User> pagedResourcesAssembler) {
        return pagedResourcesAssembler;
    }

    @Bean
    public PagedResourcesAssembler<Account> pagedResourcesAssemblerAccount(PagedResourcesAssembler<Account> pagedResourcesAssembler) {
        return pagedResourcesAssembler;
    }

}
