package com.process.clash.domain.github.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record GitHub(
        Long id,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDate date,
        int contributionCount,
        Long userId
) {
}
