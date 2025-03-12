package com.example.cleaning_service.customers.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(enumAsRef = true, description = "Type of Company")
public enum ECompanyType {
    START_UP,
    SMALL_BUSINESS,
    MEDIUM_BUSINESS,
    ENTERPRISE
}
