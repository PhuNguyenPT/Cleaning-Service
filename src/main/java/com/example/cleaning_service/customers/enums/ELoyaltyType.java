package com.example.cleaning_service.customers.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(enumAsRef = true, description = "Loyalty type")
public enum ELoyaltyType {
    STANDARD,
    BRONZE,
    SILVER,
    GOLD,
    PLATINUM
}
