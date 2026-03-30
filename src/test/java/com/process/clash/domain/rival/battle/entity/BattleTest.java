package com.process.clash.domain.rival.battle.entity;

import com.process.clash.domain.rival.battle.enums.BattleStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;

class BattleTest {

    @Test
    @DisplayName("승인 즉시 startedAt이 설정되고 IN_PROGRESS 상태가 된다")
    void accept_setsStartedAtAndReturnsInProgress() {
        Battle battle = pendingBattle(7);
        Instant now = Instant.now();

        Battle result = battle.accept(now);

        assertThat(result.battleStatus()).isEqualTo(BattleStatus.IN_PROGRESS);
        assertThat(result.startedAt()).isEqualTo(now);
        assertThat(result.endAt()).isEqualTo(now.plus(7, ChronoUnit.DAYS));
    }

    @Test
    @DisplayName("resolveStatus: endAt이 지난 IN_PROGRESS 배틀은 DONE으로 보정된다")
    void resolveStatus_returnsDone_whenInProgressAndExpired() {
        Instant startedAt = Instant.now().minus(8, ChronoUnit.DAYS);
        Instant endAt = Instant.now().minus(1, ChronoUnit.DAYS);
        Battle battle = inProgressBattle(7, startedAt, endAt);

        Battle result = battle.resolveStatus(Instant.now());

        assertThat(result.battleStatus()).isEqualTo(BattleStatus.DONE);
    }

    @Test
    @DisplayName("resolveStatus: endAt이 아직 남은 IN_PROGRESS 배틀은 상태 그대로 반환된다")
    void resolveStatus_returnsUnchanged_whenInProgressAndNotExpired() {
        Instant startedAt = Instant.now().minus(1, ChronoUnit.DAYS);
        Instant endAt = Instant.now().plus(6, ChronoUnit.DAYS);
        Battle battle = inProgressBattle(7, startedAt, endAt);

        Battle result = battle.resolveStatus(Instant.now());

        assertThat(result.battleStatus()).isEqualTo(BattleStatus.IN_PROGRESS);
    }

    @Test
    @DisplayName("cancel() 호출 시 CANCELED 상태가 된다")
    void cancel_returnsCanceled() {
        Battle battle = pendingBattle(7);

        Battle result = battle.cancel();

        assertThat(result.battleStatus()).isEqualTo(BattleStatus.CANCELED);
    }

    @Test
    @DisplayName("finish() 호출 시 IN_PROGRESS 배틀이 DONE 상태가 된다")
    void finish_returnsDone_fromInProgress() {
        Instant startedAt = Instant.now().minus(7, ChronoUnit.DAYS);
        Instant endAt = Instant.now().minus(1, ChronoUnit.DAYS);
        Battle battle = inProgressBattle(7, startedAt, endAt);

        Battle result = battle.finish();

        assertThat(result.battleStatus()).isEqualTo(BattleStatus.DONE);
    }

    private Battle pendingBattle(int duration) {
        return new Battle(1L, Instant.now(), Instant.now(), null, null,
                duration, BattleStatus.PENDING, null, 10L, 20L);
    }

    private Battle inProgressBattle(int duration, Instant startedAt, Instant endAt) {
        return new Battle(1L, Instant.now(), Instant.now(), startedAt, endAt,
                duration, BattleStatus.IN_PROGRESS, null, 10L, 20L);
    }
}