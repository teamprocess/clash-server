package com.process.clash.domain.rival.battle.entity;

import com.process.clash.domain.rival.battle.enums.BattleStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class BattleTest {

    @Test
    @DisplayName("수락 시 startDate 이전이면 NOT_STARTED 상태가 된다")
    void accept_returnsNotStarted_whenBeforeStartDate() {
        LocalDate today = LocalDate.now();
        Battle battle = pendingBattle(today.plusDays(1), today.plusDays(8));

        Battle result = battle.accept(today);

        assertThat(result.battleStatus()).isEqualTo(BattleStatus.NOT_STARTED);
    }

    @Test
    @DisplayName("수락 시 startDate 당일이면 IN_PROGRESS 상태가 된다")
    void accept_returnsInProgress_whenOnStartDate() {
        LocalDate today = LocalDate.now();
        Battle battle = pendingBattle(today, today.plusDays(7));

        Battle result = battle.accept(today);

        assertThat(result.battleStatus()).isEqualTo(BattleStatus.IN_PROGRESS);
    }

    @Test
    @DisplayName("수락 시 startDate 이후 endDate 이전이면 IN_PROGRESS 상태가 된다")
    void accept_returnsInProgress_whenAfterStartDateBeforeEndDate() {
        LocalDate today = LocalDate.now();
        Battle battle = pendingBattle(today.minusDays(1), today.plusDays(6));

        Battle result = battle.accept(today);

        assertThat(result.battleStatus()).isEqualTo(BattleStatus.IN_PROGRESS);
    }

    @Test
    @DisplayName("수락 시 endDate 이후이면 DONE 상태가 된다")
    void accept_returnsDone_whenAfterEndDate() {
        LocalDate today = LocalDate.now();
        Battle battle = pendingBattle(today.minusDays(8), today.minusDays(1));

        Battle result = battle.accept(today);

        assertThat(result.battleStatus()).isEqualTo(BattleStatus.DONE);
    }

    @Test
    @DisplayName("start() 호출 시 IN_PROGRESS 상태가 된다")
    void start_returnsInProgress() {
        LocalDate today = LocalDate.now();
        Battle battle = notStartedBattle(today, today.plusDays(7));

        Battle result = battle.start();

        assertThat(result.battleStatus()).isEqualTo(BattleStatus.IN_PROGRESS);
    }

    private Battle pendingBattle(LocalDate startDate, LocalDate endDate) {
        return new Battle(1L, Instant.now(), Instant.now(), startDate, endDate,
                BattleStatus.PENDING, null, 10L, 20L);
    }

    private Battle notStartedBattle(LocalDate startDate, LocalDate endDate) {
        return new Battle(1L, Instant.now(), Instant.now(), startDate, endDate,
                BattleStatus.NOT_STARTED, null, 10L, 20L);
    }
}