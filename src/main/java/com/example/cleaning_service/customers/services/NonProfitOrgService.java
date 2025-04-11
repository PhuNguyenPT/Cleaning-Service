package com.example.cleaning_service.customers.services;

import com.example.cleaning_service.customers.dto.non_profit_org.NonProfitOrgDetailsResponseModel;
import com.example.cleaning_service.customers.dto.non_profit_org.NonProfitOrgRequest;
import com.example.cleaning_service.customers.dto.non_profit_org.NonProfitOrgResponseModel;
import com.example.cleaning_service.customers.dto.non_profit_org.NonProfitOrgUpdateRequest;
import com.example.cleaning_service.security.entities.user.User;

import java.util.UUID;

public interface NonProfitOrgService {
    NonProfitOrgResponseModel createProfitOrg(NonProfitOrgRequest nonProfitOrgRequest, User user);
    NonProfitOrgDetailsResponseModel getNonProfitOrgDetailsResponseModelById(UUID id, User user);
    NonProfitOrgDetailsResponseModel updateNonProfitOrgDetailsById(UUID id, NonProfitOrgUpdateRequest updateRequest, User user);
    void deleteNonProfitOrgById(UUID id, User user);
    NonProfitOrgDetailsResponseModel getAdminNonProfitOrgDetailsResponseModelById(UUID id);
}
