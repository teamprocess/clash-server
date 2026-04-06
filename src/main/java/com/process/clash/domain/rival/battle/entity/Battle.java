package com.process.clash.domain.rival.battle.entity;

import com.process.clash.domain.rival.battle.enums.BattleStatus;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public record Battle(
        Long id,
        Instant createdAt,
        Instant updatedAt,
        Instant startedAt,
        Instant endAt,
        int duration,
        BattleStatus battleStatus,
        Long winnerId,
        Long rivalId,
        Long applicantId
) {

    public static Battle createDefault(int duration, Long rivalId, Long applicantId) {

        return new Battle(
                null,
                null,
                null,
                null,
                null,
                duration,
                BattleStatus.PENDING,
                null,
                rivalId,
                applicantId
        );
    }

    public Battle accept(Instant now) {
        Instant endAt = now.plus(this.duration, ChronoUnit.DAYS);

        return new Battle(
                this.id,
                this.createdAt,
                this.updatedAt,
                now,
                endAt,
                this.duration,
                BattleStatus.IN_PROGRESS,
                this.winnerId,
                this.rivalId,
                this.applicantId
        );
    }

    /**
     * 배틀이 IN_PROGRESS 상태이며 종료 시각이 지났는지 확인한다.
     */
    public boolean isExpiredInProgress(Instant now) {
        return this.battleStatus == BattleStatus.IN_PROGRESS
                && this.endAt != null
                && this.endAt.isBefore(now);
    }

    /**
     * 조회 시점에 만료된 배틀을 DONE 상태로 보정한다.
     * DB 업데이트 없이 반환 값만 보정하는 fallback 용도로 사용한다.
     */
    public Battle resolveStatus(Instant now) {
        if (isExpiredInProgress(now)) {
            return finish();
        }
        return this;
    }

    public Battle reject() {

        return new Battle(
                this.id,
                this.createdAt,
                this.updatedAt,
                this.startedAt,
                this.endAt,
                this.duration,
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
                this.startedAt,
                this.endAt,
                this.duration,
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
                this.startedAt,
                this.endAt,
                this.duration,
                BattleStatus.DONE,
                this.winnerId,
                this.rivalId,
                this.applicantId
        );
    }

    public Battle finishWithWinner(Long winnerId) {

        return new Battle(
                this.id,
                this.createdAt,
                this.updatedAt,
                this.startedAt,
                this.endAt,
                this.duration,
                BattleStatus.DONE,
                winnerId,
                this.rivalId,
                this.applicantId
        );
    }
}