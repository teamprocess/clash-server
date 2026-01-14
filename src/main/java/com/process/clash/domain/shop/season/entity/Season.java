package com.process.clash.domain.shop.season.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record Season(
        Long id,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String title,
        LocalDate startDate,
        LocalDate endDate
) {
    public static Season createDefault(String title, LocalDate startDate, LocalDate endDate) {
        return new Season(
                null,
                null,
                null,
                title,
                startDate,
                endDate
        );
    }

    public Boolean isActive() {
        LocalDate today = LocalDate.now();
        return !today.isBefore(startDate) && !today.isAfter(endDate);
    }
}
