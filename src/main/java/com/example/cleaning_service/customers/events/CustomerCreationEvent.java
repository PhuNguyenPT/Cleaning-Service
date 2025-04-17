package com.example.cleaning_service.customers.events;

import com.example.cleaning_service.customers.dto.accounts.AccountRequest;

public record CustomerCreationEvent(AccountRequest accountRequest) {
}
