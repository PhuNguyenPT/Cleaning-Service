package com.example.cleaning_service.customers.mappers;

import com.example.cleaning_service.customers.entities.CustomerPreferredDay;
import com.example.cleaning_service.customers.enums.EDay;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CustomerPreferredDayMapper {
    public CustomerPreferredDay fromEDayToCustomerPreferredDay(@NotNull EDay eDay) {
        return new CustomerPreferredDay(
                eDay
        );
    }

    public Set<CustomerPreferredDay> fromEDaysToCustomerPreferredDays(@NotNull Set<EDay> eDays) {
        return eDays.stream()
                .map(this::fromEDayToCustomerPreferredDay)
                .collect(Collectors.toSet());
    }

    public EDay fromCustomerPreferredDayToEDay(@NotNull CustomerPreferredDay customerPreferredDay) {
        return customerPreferredDay.getPreferredDay();
    }

    public Set<EDay> fromCustomerPreferredDaysToEDays(@NotNull Set<CustomerPreferredDay> customerPreferredDays) {
        return customerPreferredDays.stream()
                .map(this::fromCustomerPreferredDayToEDay)
                .collect(Collectors.toSet());
    }
}
