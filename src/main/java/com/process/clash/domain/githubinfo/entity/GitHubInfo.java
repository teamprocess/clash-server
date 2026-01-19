package com.process.clash.domain.githubinfo.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record GitHubInfo(
        Long id,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDate date,
        int contributionCount,
        Long userId
) {
}
