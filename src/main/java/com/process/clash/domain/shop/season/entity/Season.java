package com.process.clash.domain.shop.season.entity;

import jakarta.validation.ValidationException;
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
    public Season {
        if (startDate != null && endDate != null && endDate.isBefore(startDate)) {
            throw new ValidationException("종료일은 시작일보다 이전일 수 없습니다.");
        }
    }

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
