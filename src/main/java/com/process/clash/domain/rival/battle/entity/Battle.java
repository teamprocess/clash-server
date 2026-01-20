package com.process.clash.domain.rival.battle.entity;

import com.process.clash.domain.rival.battle.enums.BattleStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record Battle(
        Long id,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDate startDate,
        LocalDate endDate,
        BattleStatus battleStatus,
        Long winnerId,
        Long rivalId
) {
}
