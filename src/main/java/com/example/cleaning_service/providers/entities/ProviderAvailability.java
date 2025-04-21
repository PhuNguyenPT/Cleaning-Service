package com.example.cleaning_service.providers.entities;

import com.example.cleaning_service.customers.enums.EDay;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.Objects;

@Setter
@Getter
@Embeddable
public class ProviderAvailability {

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week")
    private EDay dayOfWeek;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    public ProviderAvailability() {
    }

    public ProviderAvailability(EDay dayOfWeek, LocalTime startTime, LocalTime endTime) {
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProviderAvailability that = (ProviderAvailability) o;

        if (dayOfWeek != that.dayOfWeek) return false;
        if (!Objects.equals(startTime, that.startTime)) return false;
        return Objects.equals(endTime, that.endTime);
    }

    @Override
    public int hashCode() {
        int result = dayOfWeek != null ? dayOfWeek.hashCode() : 0;
        result = 31 * result + (startTime != null ? startTime.hashCode() : 0);
        result = 31 * result + (endTime != null ? endTime.hashCode() : 0);
        return result;
    }
}