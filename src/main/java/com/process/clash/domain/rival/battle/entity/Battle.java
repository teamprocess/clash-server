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

    public static Battle createDefault(LocalDate startDate, LocalDate endDate, Long rivalId) {

        return new Battle(
                null,
                null,
                null,
                startDate,
                endDate,
                BattleStatus.PENDING,
                null,
                rivalId
        );
    }

    public Battle accept() {

        BattleStatus status = BattleStatus.IN_PROGRESS;

        if (LocalDate.now().isAfter(this.endDate)) {
            status = BattleStatus.DONE;
        }

        return new Battle(
                this.id,
                this.createdAt,
                this.updatedAt,
                this.startDate,
                this.endDate,
                status,
                this.winnerId,
                this.rivalId
        );
    }
}
