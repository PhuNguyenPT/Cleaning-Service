package com.example.cleaning_service.customers.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(enumAsRef = true, description = "Organizational type")
public enum EOrganizationType {
    INDIVIDUAL,
    COMPANY,
    NON_PROFIT,
    GOVERNMENT
}
