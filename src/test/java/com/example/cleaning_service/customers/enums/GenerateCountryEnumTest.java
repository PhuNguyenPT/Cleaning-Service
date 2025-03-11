package com.example.cleaning_service.customers.enums;

import org.junit.jupiter.api.Test;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class GenerateCountryEnumTest {

    @Test
    void testGenerateCountryEnum() {
        String[] countryCodes = Locale.getISOCountries();

        // Ensure the list is not empty
        assertTrue(countryCodes.length > 0, "ISO country codes should not be empty");

        for (String countryCode : countryCodes) {
            // Create a Locale instance using the recommended method
            Locale locale = Locale.of("", countryCode);
            String countryName = locale.getDisplayCountry();

            // Ensure country code is uppercase and 2 letters
            assertEquals(2, countryCode.length(), "Country code should be 2 characters");
            assertTrue(countryCode.matches("[A-Z]{2}"), "Country code should be uppercase letters");

            // Ensure country name is not empty
            assertNotNull(countryName, "Country name should not be null");
            assertFalse(countryName.isBlank(), "Country name should not be blank");

            System.out.println(countryCode + "(\"" + countryName + "\"),");
        }
    }
}
