package com.process.clash.domain.rival.battle.entity;

import com.process.clash.domain.rival.battle.enums.BattleStatus;

import java.time.LocalDate;
import java.time.Instant;

public record Battle(
        Long id,
        Instant createdAt,
        Instant updatedAt,
        LocalDate startDate,
        LocalDate endDate,
        BattleStatus battleStatus,
        Long winnerId,
        Long rivalId,
        Long applicantId
) {

    public static Battle createDefault(LocalDate startDate, LocalDate endDate, Long rivalId, Long applicantId) {

        return new Battle(
                null,
                null,
                null,
                startDate,
                endDate,
                BattleStatus.PENDING,
                null,
                rivalId,
                applicantId
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
                this.rivalId,
                this.applicantId
        );
    }

    public Battle reject() {

        return new Battle(
                this.id,
                this.createdAt,
                this.updatedAt,
                this.startDate,
                this.endDate,
                BattleStatus.REJECTED,
                this.winnerId,
                this.rivalId,
                this.applicantId
        );
    }

    public Battle cancel() {

        return new Battle(
                this.id,
                this.createdAt,
                this.updatedAt,
                this.startDate,
                this.endDate,
                BattleStatus.CANCELED,
                this.winnerId,
                this.rivalId,
                this.applicantId
        );
    }

    public Battle finish() {

        return new Battle(
                this.id,
                this.createdAt,
                this.updatedAt,
                this.startDate,
                this.endDate,
                BattleStatus.DONE,
                this.winnerId,
                this.rivalId,
                this.applicantId
        );
    }
}
