package com.example.cleaning_service.customers.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(enumAsRef = true, description = "Preferred payment method")
public enum EPaymentType {
    CASH,            // Physical currency
    CREDIT,          // Credit card
    DEBIT,           // Debit card
    BANK_TRANSFER,   // Direct deposit or wire transfer
    PAYPAL,          // Popular online payment system
    APPLE_PAY,       // Apple’s mobile payment system
    GOOGLE_PAY,      // Google’s mobile payment system
    CRYPTOCURRENCY   // Bitcoin, Ethereum, etc. (for crypto-supporting platforms)
}

