package com.process.clash.application.compete.rival.battle.service;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.compete.rival.battle.data.FindAllBattleInfoData;
import com.process.clash.application.compete.rival.battle.port.out.BattleRepositoryPort;
import com.process.clash.application.compete.rival.rival.port.out.RivalRepositoryPort;
import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import com.process.clash.application.user.userexphistory.port.out.UserExpHistoryRepositoryPort;
import com.process.clash.domain.common.enums.Major;
import com.process.clash.domain.rival.battle.entity.Battle;
import com.process.clash.domain.rival.battle.enums.BattleStatus;
import com.process.clash.domain.rival.rival.entity.Rival;
import com.process.clash.domain.rival.rival.enums.RivalLinkingStatus;
import com.process.clash.domain.user.user.entity.User;
import com.process.clash.domain.user.user.enums.Role;
import com.process.clash.domain.user.user.enums.UserStatus;
import com.process.clash.domain.user.userrankhistory.enums.ExpTier;
import com.process.clash.domain.user.userrankhistory.enums.RankTier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindAllBattleInfoServiceTest {

    @Mock
    private BattleRepositoryPort battleRepositoryPort;

    @Mock
    private RivalRepositoryPort rivalRepositoryPort;

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @Mock
    private UserExpHistoryRepositoryPort userExpHistoryRepositoryPort;

    private FindAllBattleInfoService findAllBattleInfoService;

    private static final Long CURRENT_USER_ID = 1L;
    private static final Long ENEMY_USER_ID = 2L;
    private static final Long RIVAL_ID = 10L;
    private static final Long BATTLE_ID = 100L;

    @BeforeEach
    void setUp() {
        findAllBattleInfoService = new FindAllBattleInfoService(
                battleRepositoryPort,
                rivalRepositoryPort,
                userRepositoryPort,
                userExpHistoryRepositoryPort
        );
    }

    @Test
    @DisplayName("DONE 배틀에서 현재 유저가 승자이면 WON을 반환한다")
    void execute_returnsWon_whenDoneAndCurrentUserIsWinner() {
        stubCommonDependencies(List.of(battle(BattleStatus.DONE, CURRENT_USER_ID)));

        FindAllBattleInfoData.Result result = findAllBattleInfoService.execute(command());

        assertThat(result.battles().get(0).result()).isEqualTo("WON");
        assertThat(result.battles().get(0).enemy().rankTier()).isEqualTo(RankTier.NONE);
        assertThat(result.battles().get(0).enemy().expTier()).isEqualTo(ExpTier.UNRANKED);
    }

    @Test
    @DisplayName("DONE 배틀에서 상대방이 승자이면 LOST를 반환한다")
    void execute_returnsLost_whenDoneAndEnemyIsWinner() {
        stubCommonDependencies(List.of(battle(BattleStatus.DONE, ENEMY_USER_ID)));

        FindAllBattleInfoData.Result result = findAllBattleInfoService.execute(command());

        assertThat(result.battles().get(0).result()).isEqualTo("LOST");
    }

    @Test
    @DisplayName("DONE 배틀에서 승자가 없으면 DRAWN을 반환한다")
    void execute_returnsDrawn_whenDoneAndNoWinner() {
        stubCommonDependencies(List.of(battle(BattleStatus.DONE, null)));

        FindAllBattleInfoData.Result result = findAllBattleInfoService.execute(command());

        assertThat(result.battles().get(0).result()).isEqualTo("DRAWN");
    }

    @Test
    @DisplayName("IN_PROGRESS 배틀에서 내 평균 경험치가 높으면 WINNING을 반환한다")
    void execute_returnsWinning_whenInProgressAndCurrentUserLeading() {
        stubCommonDependencies(List.of(inProgressBattle()));
        when(userExpHistoryRepositoryPort.findAverageExpForBattles(eq(CURRENT_USER_ID), anyList()))
                .thenReturn(Map.of(BATTLE_ID, 10.0));
        when(userExpHistoryRepositoryPort.findAverageExpForBattles(eq(ENEMY_USER_ID), anyList()))
                .thenReturn(Map.of(BATTLE_ID, 5.0));

        FindAllBattleInfoData.Result result = findAllBattleInfoService.execute(command());

        assertThat(result.battles().get(0).result()).isEqualTo("WINNING");
    }

    @Test
    @DisplayName("IN_PROGRESS 배틀에서 상대방 평균 경험치가 높으면 LOSING을 반환한다")
    void execute_returnsLosing_whenInProgressAndEnemyLeading() {
        stubCommonDependencies(List.of(inProgressBattle()));
        when(userExpHistoryRepositoryPort.findAverageExpForBattles(eq(CURRENT_USER_ID), anyList()))
                .thenReturn(Map.of(BATTLE_ID, 5.0));
        when(userExpHistoryRepositoryPort.findAverageExpForBattles(eq(ENEMY_USER_ID), anyList()))
                .thenReturn(Map.of(BATTLE_ID, 10.0));

        FindAllBattleInfoData.Result result = findAllBattleInfoService.execute(command());

        assertThat(result.battles().get(0).result()).isEqualTo("LOSING");
    }

    @Test
    @DisplayName("IN_PROGRESS 배틀에서 경험치가 동점이면 DRAWING을 반환한다")
    void execute_returnsDrawing_whenInProgressAndTied() {
        stubCommonDependencies(List.of(inProgressBattle()));
        when(userExpHistoryRepositoryPort.findAverageExpForBattles(eq(CURRENT_USER_ID), anyList()))
                .thenReturn(Map.of(BATTLE_ID, 5.0));
        when(userExpHistoryRepositoryPort.findAverageExpForBattles(eq(ENEMY_USER_ID), anyList()))
                .thenReturn(Map.of(BATTLE_ID, 5.0));

        FindAllBattleInfoData.Result result = findAllBattleInfoService.execute(command());

        assertThat(result.battles().get(0).result()).isEqualTo("DRAWING");
    }

    @Test
    @DisplayName("NOT_STARTED 배틀은 NOT_STARTED를 반환한다")
    void execute_returnsNotStarted_whenNotStarted() {
        stubCommonDependencies(List.of(battle(BattleStatus.NOT_STARTED, null)));

        FindAllBattleInfoData.Result result = findAllBattleInfoService.execute(command());

        assertThat(result.battles().get(0).result()).isEqualTo("NOT_STARTED");
    }

    @Test
    @DisplayName("CANCELED 배틀은 CANCELED를 반환한다")
    void execute_returnsCanceled_whenCanceled() {
        stubCommonDependencies(List.of(battle(BattleStatus.CANCELED, null)));

        FindAllBattleInfoData.Result result = findAllBattleInfoService.execute(command());

        assertThat(result.battles().get(0).result()).isEqualTo("CANCELED");
    }

    // ── fixtures ──────────────────────────────────────────────────────────────

    private FindAllBattleInfoData.Command command() {
        return FindAllBattleInfoData.Command.from(new Actor(CURRENT_USER_ID));
    }

    private Battle battle(BattleStatus status, Long winnerId) {
        return new Battle(BATTLE_ID, Instant.now(), Instant.now(),
                LocalDate.now().minusDays(1), LocalDate.now().plusDays(6),
                status, winnerId, RIVAL_ID, CURRENT_USER_ID);
    }

    private Battle inProgressBattle() {
        return battle(BattleStatus.IN_PROGRESS, null);
    }

    private void stubCommonDependencies(List<Battle> battles) {
        Rival rival = new Rival(RIVAL_ID, Instant.now(), Instant.now(),
                RivalLinkingStatus.ACCEPTED, CURRENT_USER_ID, ENEMY_USER_ID);
        User enemyUser = new User(ENEMY_USER_ID, Instant.now(), Instant.now(),
                "enemy", "enemy@example.com", "Enemy", "",
                Role.USER, "", 0, 0, Major.NONE, UserStatus.ACTIVE, null,
                RankTier.NONE, ExpTier.UNRANKED);

        when(battleRepositoryPort.findByUserIdWithOutRejected(CURRENT_USER_ID)).thenReturn(battles);
        when(rivalRepositoryPort.findByIdIn(Set.of(RIVAL_ID))).thenReturn(List.of(rival));
        when(userRepositoryPort.findByIdIn(Set.of(ENEMY_USER_ID))).thenReturn(List.of(enemyUser));
    }
}