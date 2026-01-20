package com.process.clash.domain.user.userrankhistory.entity;

import com.process.clash.domain.user.userrankhistory.enums.ExpTier;
import com.process.clash.domain.user.userrankhistory.enums.RankTier;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record UserRankHistory(
        Long id,
        LocalDateTime createdAt,
        LocalDate date,
        int rank,
        ExpTier expTier,
        RankTier rankTier,
        Long userId,
        Long seasonId
) {
}
