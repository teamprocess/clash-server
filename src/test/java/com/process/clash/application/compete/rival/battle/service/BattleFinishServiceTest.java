package com.process.clash.application.compete.rival.battle.service;

import com.process.clash.application.compete.rival.battle.port.out.BattleRepositoryPort;
import com.process.clash.application.compete.rival.rival.port.out.RivalRepositoryPort;
import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import com.process.clash.application.user.userexphistory.port.out.UserExpHistoryRepositoryPort;
import com.process.clash.application.user.usergoodshistory.port.out.UserGoodsHistoryRepositoryPort;
import com.process.clash.domain.common.enums.GoodsActingCategory;
import com.process.clash.domain.rival.battle.entity.Battle;
import com.process.clash.domain.rival.battle.enums.BattleStatus;
import com.process.clash.domain.rival.rival.entity.Rival;
import com.process.clash.domain.rival.rival.enums.RivalLinkingStatus;
import com.process.clash.domain.user.user.entity.User;
import com.process.clash.domain.user.usergoodshistory.entity.UserGoodsHistory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BattleFinishServiceTest {

    private static final Long RIVAL_ID = 10L;
    private static final Long FIRST_USER_ID = 20L;
    private static final Long SECOND_USER_ID = 30L;

    @Mock
    private BattleRepositoryPort battleRepositoryPort;

    @Mock
    private RivalRepositoryPort rivalRepositoryPort;

    @Mock
    private UserExpHistoryRepositoryPort userExpHistoryRepositoryPort;

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @Mock
    private UserGoodsHistoryRepositoryPort userGoodsHistoryRepositoryPort;

    private BattleFinishService battleFinishService;

    @BeforeEach
    void setUp() {
        battleFinishService = new BattleFinishService(
                battleRepositoryPort,
                rivalRepositoryPort,
                userExpHistoryRepositoryPort,
                userRepositoryPort,
                userGoodsHistoryRepositoryPort
        );
    }

    @Test
    @DisplayName("종료 시각이 지난 IN_PROGRESS 배틀을 DONE으로 전환한다")
    void finishExpiredBattles_transitionsInProgressToDone() {
        Instant startedAt = Instant.now().minus(8, ChronoUnit.DAYS);
        Instant endAt = Instant.now().minus(1, ChronoUnit.DAYS);
        Battle inProgressBattle = new Battle(1L, Instant.now(), Instant.now(),
                startedAt, endAt, 7, BattleStatus.IN_PROGRESS, null, RIVAL_ID, FIRST_USER_ID);

        Rival rival = new Rival(RIVAL_ID, Instant.now(), Instant.now(),
                RivalLinkingStatus.ACCEPTED, FIRST_USER_ID, SECOND_USER_ID);

        User winner = mockUser(FIRST_USER_ID, 0);
        User loser = mockUser(SECOND_USER_ID, 0);

        when(battleRepositoryPort.findExpiredInProgressBattles()).thenReturn(List.of(inProgressBattle));
        when(rivalRepositoryPort.findByIdIn(Set.of(RIVAL_ID))).thenReturn(List.of(rival));
        when(userExpHistoryRepositoryPort.findAverageExpForBattles(eq(FIRST_USER_ID), anyList()))
                .thenReturn(Map.of(1L, 10.0));
        when(userExpHistoryRepositoryPort.findAverageExpForBattles(eq(SECOND_USER_ID), anyList()))
                .thenReturn(Map.of(1L, 5.0));
        when(userRepositoryPort.findByIdIn(anySet())).thenReturn(List.of(winner, loser));

        battleFinishService.finishExpiredBattles();

        ArgumentCaptor<List<Battle>> captor = ArgumentCaptor.forClass(List.class);
        verify(battleRepositoryPort).saveAll(captor.capture());
        assertThat(captor.getValue()).allMatch(b -> b.battleStatus() == BattleStatus.DONE);
    }

    @Test
    @DisplayName("7일 배틀 종료 시 승자에게 700, 패자에게 350 쿠키를 지급한다")
    void finishExpiredBattles_grants700CookiesToWinnerAnd350ToLoser_for7DayBattle() {
        Instant startedAt = Instant.now().minus(8, ChronoUnit.DAYS);
        Instant endAt = Instant.now().minus(1, ChronoUnit.DAYS);
        Battle inProgressBattle = new Battle(1L, Instant.now(), Instant.now(),
                startedAt, endAt, 7, BattleStatus.IN_PROGRESS, null, RIVAL_ID, FIRST_USER_ID);

        Rival rival = new Rival(RIVAL_ID, Instant.now(), Instant.now(),
                RivalLinkingStatus.ACCEPTED, FIRST_USER_ID, SECOND_USER_ID);

        User winner = mockUser(FIRST_USER_ID, 0);
        User loser = mockUser(SECOND_USER_ID, 0);

        when(battleRepositoryPort.findExpiredInProgressBattles()).thenReturn(List.of(inProgressBattle));
        when(rivalRepositoryPort.findByIdIn(Set.of(RIVAL_ID))).thenReturn(List.of(rival));
        when(userExpHistoryRepositoryPort.findAverageExpForBattles(eq(FIRST_USER_ID), anyList()))
                .thenReturn(Map.of(1L, 10.0));
        when(userExpHistoryRepositoryPort.findAverageExpForBattles(eq(SECOND_USER_ID), anyList()))
                .thenReturn(Map.of(1L, 5.0));
        when(userRepositoryPort.findByIdIn(anySet())).thenReturn(List.of(winner, loser));

        battleFinishService.finishExpiredBattles();

        ArgumentCaptor<List<UserGoodsHistory>> historyCaptor = ArgumentCaptor.forClass(List.class);
        verify(userGoodsHistoryRepositoryPort).saveAll(historyCaptor.capture());

        List<UserGoodsHistory> histories = historyCaptor.getValue();
        assertThat(histories).hasSize(2);
        assertThat(histories).anySatisfy(h -> {
            assertThat(h.userId()).isEqualTo(FIRST_USER_ID);
            assertThat(h.goodsActingCategory()).isEqualTo(GoodsActingCategory.BATTLE_WIN_REWARD);
            assertThat(h.variation()).isEqualTo(700);
        });
        assertThat(histories).anySatisfy(h -> {
            assertThat(h.userId()).isEqualTo(SECOND_USER_ID);
            assertThat(h.goodsActingCategory()).isEqualTo(GoodsActingCategory.BATTLE_LOSE_REWARD);
            assertThat(h.variation()).isEqualTo(350);
        });
    }

    @Test
    @DisplayName("5일 배틀 종료 시 승자에게 500, 패자에게 250 쿠키를 지급한다")
    void finishExpiredBattles_grants500CookiesToWinnerAnd250ToLoser_for5DayBattle() {
        Instant startedAt = Instant.now().minus(6, ChronoUnit.DAYS);
        Instant endAt = Instant.now().minus(1, ChronoUnit.DAYS);
        Battle inProgressBattle = new Battle(2L, Instant.now(), Instant.now(),
                startedAt, endAt, 5, BattleStatus.IN_PROGRESS, null, RIVAL_ID, FIRST_USER_ID);

        Rival rival = new Rival(RIVAL_ID, Instant.now(), Instant.now(),
                RivalLinkingStatus.ACCEPTED, FIRST_USER_ID, SECOND_USER_ID);

        User winner = mockUser(FIRST_USER_ID, 0);
        User loser = mockUser(SECOND_USER_ID, 0);

        when(battleRepositoryPort.findExpiredInProgressBattles()).thenReturn(List.of(inProgressBattle));
        when(rivalRepositoryPort.findByIdIn(Set.of(RIVAL_ID))).thenReturn(List.of(rival));
        when(userExpHistoryRepositoryPort.findAverageExpForBattles(eq(FIRST_USER_ID), anyList()))
                .thenReturn(Map.of(2L, 10.0));
        when(userExpHistoryRepositoryPort.findAverageExpForBattles(eq(SECOND_USER_ID), anyList()))
                .thenReturn(Map.of(2L, 5.0));
        when(userRepositoryPort.findByIdIn(anySet())).thenReturn(List.of(winner, loser));

        battleFinishService.finishExpiredBattles();

        ArgumentCaptor<List<UserGoodsHistory>> historyCaptor = ArgumentCaptor.forClass(List.class);
        verify(userGoodsHistoryRepositoryPort).saveAll(historyCaptor.capture());

        List<UserGoodsHistory> histories = historyCaptor.getValue();
        assertThat(histories).anySatisfy(h -> {
            assertThat(h.userId()).isEqualTo(FIRST_USER_ID);
            assertThat(h.variation()).isEqualTo(500);
        });
        assertThat(histories).anySatisfy(h -> {
            assertThat(h.userId()).isEqualTo(SECOND_USER_ID);
            assertThat(h.variation()).isEqualTo(250);
        });
    }

    @Test
    @DisplayName("3일 배틀 종료 시 승자에게 300, 패자에게 150 쿠키를 지급한다")
    void finishExpiredBattles_grants300CookiesToWinnerAnd150ToLoser_for3DayBattle() {
        Instant startedAt = Instant.now().minus(4, ChronoUnit.DAYS);
        Instant endAt = Instant.now().minus(1, ChronoUnit.DAYS);
        Battle inProgressBattle = new Battle(3L, Instant.now(), Instant.now(),
                startedAt, endAt, 3, BattleStatus.IN_PROGRESS, null, RIVAL_ID, FIRST_USER_ID);

        Rival rival = new Rival(RIVAL_ID, Instant.now(), Instant.now(),
                RivalLinkingStatus.ACCEPTED, FIRST_USER_ID, SECOND_USER_ID);

        User winner = mockUser(FIRST_USER_ID, 0);
        User loser = mockUser(SECOND_USER_ID, 0);

        when(battleRepositoryPort.findExpiredInProgressBattles()).thenReturn(List.of(inProgressBattle));
        when(rivalRepositoryPort.findByIdIn(Set.of(RIVAL_ID))).thenReturn(List.of(rival));
        when(userExpHistoryRepositoryPort.findAverageExpForBattles(eq(FIRST_USER_ID), anyList()))
                .thenReturn(Map.of(3L, 10.0));
        when(userExpHistoryRepositoryPort.findAverageExpForBattles(eq(SECOND_USER_ID), anyList()))
                .thenReturn(Map.of(3L, 5.0));
        when(userRepositoryPort.findByIdIn(anySet())).thenReturn(List.of(winner, loser));

        battleFinishService.finishExpiredBattles();

        ArgumentCaptor<List<UserGoodsHistory>> historyCaptor = ArgumentCaptor.forClass(List.class);
        verify(userGoodsHistoryRepositoryPort).saveAll(historyCaptor.capture());

        List<UserGoodsHistory> histories = historyCaptor.getValue();
        assertThat(histories).anySatisfy(h -> {
            assertThat(h.userId()).isEqualTo(FIRST_USER_ID);
            assertThat(h.variation()).isEqualTo(300);
        });
        assertThat(histories).anySatisfy(h -> {
            assertThat(h.userId()).isEqualTo(SECOND_USER_ID);
            assertThat(h.variation()).isEqualTo(150);
        });
    }

    @Test
    @DisplayName("7일 무승부 배틀은 양측 모두에게 525 쿠키를 지급한다")
    void finishExpiredBattles_grants525CookiesToBoth_whenDraw_for7DayBattle() {
        Instant startedAt = Instant.now().minus(8, ChronoUnit.DAYS);
        Instant endAt = Instant.now().minus(1, ChronoUnit.DAYS);
        Battle inProgressBattle = new Battle(1L, Instant.now(), Instant.now(),
                startedAt, endAt, 7, BattleStatus.IN_PROGRESS, null, RIVAL_ID, FIRST_USER_ID);

        Rival rival = new Rival(RIVAL_ID, Instant.now(), Instant.now(),
                RivalLinkingStatus.ACCEPTED, FIRST_USER_ID, SECOND_USER_ID);

        User first = mockUser(FIRST_USER_ID, 0);
        User second = mockUser(SECOND_USER_ID, 0);

        when(battleRepositoryPort.findExpiredInProgressBattles()).thenReturn(List.of(inProgressBattle));
        when(rivalRepositoryPort.findByIdIn(Set.of(RIVAL_ID))).thenReturn(List.of(rival));
        when(userExpHistoryRepositoryPort.findAverageExpForBattles(eq(FIRST_USER_ID), anyList()))
                .thenReturn(Map.of(1L, 10.0));
        when(userExpHistoryRepositoryPort.findAverageExpForBattles(eq(SECOND_USER_ID), anyList()))
                .thenReturn(Map.of(1L, 10.0));
        when(userRepositoryPort.findByIdIn(anySet())).thenReturn(List.of(first, second));

        battleFinishService.finishExpiredBattles();

        ArgumentCaptor<List<UserGoodsHistory>> historyCaptor = ArgumentCaptor.forClass(List.class);
        verify(userGoodsHistoryRepositoryPort).saveAll(historyCaptor.capture());

        List<UserGoodsHistory> histories = historyCaptor.getValue();
        assertThat(histories).hasSize(2);
        assertThat(histories).allSatisfy(h -> {
            assertThat(h.goodsActingCategory()).isEqualTo(GoodsActingCategory.BATTLE_DRAW_REWARD);
            assertThat(h.variation()).isEqualTo(525); // (700 + 350) / 2
        });
    }

    @Test
    @DisplayName("종료할 배틀이 없으면 saveAll을 호출하지 않는다")
    void finishExpiredBattles_doesNotCallSaveAll_whenNoBattlesToProcess() {
        when(battleRepositoryPort.findExpiredInProgressBattles()).thenReturn(List.of());

        battleFinishService.finishExpiredBattles();

        verify(battleRepositoryPort, never()).saveAll(any());
    }

    private User mockUser(Long id, int totalCookie) {
        User user = mock(User.class);
        when(user.id()).thenReturn(id);
        when(user.addCookie(anyInt())).thenReturn(user);
        return user;
    }
}