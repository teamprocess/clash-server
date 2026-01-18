package com.process.clash.domain.shop.season.entity;

import com.process.clash.application.shop.season.exception.exception.badrequest.InvalidDateRangeException;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record Season(
        Long id,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String name,
        LocalDate startDate,
        LocalDate endDate
) {
    public Season {
        if (startDate != null && endDate != null && endDate.isBefore(startDate)) {
            throw new InvalidDateRangeException();
        }
    }

    public static Season createDefault(String name, LocalDate startDate, LocalDate endDate) {
        return new Season(
                null,
                null,
                null,
                name,
                startDate,
                endDate
        );
    }

    public Boolean isActive() {
        LocalDate today = LocalDate.now();
        return !today.isBefore(startDate) && !today.isAfter(endDate);
    }
}
