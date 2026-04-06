package com.process.clash.domain.rival.battle.entity;

import com.process.clash.domain.rival.battle.enums.BattleStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;

class BattleTest {

    @Test
    @DisplayName("승인 즉시 startedAt이 설정되고, endAt은 수락 시각으로부터 duration일(72시간 단위) 후로 설정된다")
    void accept_setsStartedAtAndReturnsInProgress() {
        Battle battle = pendingBattle(7);
        Instant now = Instant.now();
        ZoneId zoneId = ZoneId.of("Asia/Seoul");

        Battle result = battle.accept(now, zoneId);

        Instant expectedEndAt = now.plus(7, ChronoUnit.DAYS);

        assertThat(result.battleStatus()).isEqualTo(BattleStatus.IN_PROGRESS);
        assertThat(result.startedAt()).isEqualTo(now);
        assertThat(result.endAt()).isEqualTo(expectedEndAt);
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
    @DisplayName("reject() 호출 시 REJECTED 상태가 된다")
    void reject_returnsRejected() {
        Battle battle = pendingBattle(7);

        Battle result = battle.reject();

        assertThat(result.battleStatus()).isEqualTo(BattleStatus.REJECTED);
    }

    @Test
    @DisplayName("resolveStatus: endAt이 null인 배틀(PENDING)은 상태 그대로 반환된다")
    void resolveStatus_returnsUnchanged_whenEndAtIsNull() {
        Battle battle = pendingBattle(7);

        Battle result = battle.resolveStatus(Instant.now());

        assertThat(result.battleStatus()).isEqualTo(BattleStatus.PENDING);
    }

    @Test
    @DisplayName("resolveStatus: 이미 DONE 상태인 배틀은 변경되지 않는다")
    void resolveStatus_returnsUnchanged_whenAlreadyDone() {
        Instant startedAt = Instant.now().minus(8, ChronoUnit.DAYS);
        Instant endAt = Instant.now().minus(1, ChronoUnit.DAYS);
        Battle battle = new Battle(1L, Instant.now(), Instant.now(), startedAt, endAt,
                7, BattleStatus.DONE, null, 10L, 20L);

        Battle result = battle.resolveStatus(Instant.now());

        assertThat(result.battleStatus()).isEqualTo(BattleStatus.DONE);
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