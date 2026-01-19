package com.process.clash.domain.user.github.usergithubinfo.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record UserGitHubInfo(
        Long id,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDate date,
        int contributionCount,
        Long userId
) {
}
