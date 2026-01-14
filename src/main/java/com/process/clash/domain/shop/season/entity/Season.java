package com.process.clash.domain.shop.season.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record Season(
        Long id,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String title,
        LocalDate startDate,
        LocalDate endDate,
        Boolean isActive
) {
    public static Season createDefault(String title, LocalDate startDate, LocalDate endDate) {
        return new Season(
                null,
                null,
                null,
                title,
                startDate,
                endDate,
                false
        );
    }
}
