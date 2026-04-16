package com.process.clash.application.ranking.service;

import com.process.clash.application.shop.season.port.out.SeasonRepositoryPort;
import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import com.process.clash.application.user.userrankhistory.port.out.UserRankHistoryRepositoryPort;
import com.process.clash.domain.common.enums.Major;
import com.process.clash.domain.shop.season.entity.Season;
import com.process.clash.domain.user.user.entity.User;
import com.process.clash.domain.user.user.enums.Role;
import com.process.clash.domain.user.user.enums.UserStatus;
import com.process.clash.domain.user.userrankhistory.entity.UserRankHistory;
import com.process.clash.domain.user.userrankhistory.enums.ExpTier;
import com.process.clash.domain.user.userrankhistory.enums.RankTier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RankingTierUpdateServiceTest {

    @Mock private UserRepositoryPort userRepositoryPort;
    @Mock private UserRankHistoryRepositoryPort userRankHistoryRepositoryPort;
    @Mock private SeasonRepositoryPort seasonRepositoryPort;

    private RankingTierUpdateService service;

    @BeforeEach
    void setUp() {
        service = new RankingTierUpdateService(
                userRepositoryPort,
                userRankHistoryRepositoryPort,
                seasonRepositoryPort
        );
    }

    // ===== 티어 계산 =====

    @Test
    @DisplayName("유저가 없으면 saveAll을 호출하지 않는다")
    void updateRankTiers_noUsers_doesNothing() {
        when(userRepositoryPort.findAllOrderByTotalExpDesc()).thenReturn(List.of());

        service.updateRankTiers();

        verify(userRepositoryPort, never()).saveAll(any());
        verify(userRankHistoryRepositoryPort, never()).saveAll(any());
    }

    @Test
    @DisplayName("1위이고 exp >= 200000이면 AURA를 부여한다")
    void computeRankTier_rank1_expOver200k_returnsAura() {
        User user = createUser(1L, 200_000, RankTier.NONE);
        when(userRepositoryPort.findAllOrderByTotalExpDesc()).thenReturn(List.of(user));
        when(seasonRepositoryPort.findCurrentSeason()).thenReturn(Optional.empty());

        service.updateRankTiers();

        ArgumentCaptor<List<User>> captor = ArgumentCaptor.forClass(List.class);
        verify(userRepositoryPort).saveAll(captor.capture());
        assertThat(captor.getValue().get(0).currentRankTier()).isEqualTo(RankTier.AURA);
    }

    @Test
    @DisplayName("2위이고 exp >= 150000이면 MASTER를 부여한다")
    void computeRankTier_rank2_expOver150k_returnsMaster() {
        User rank1 = createUser(1L, 250_000, RankTier.NONE);
        User rank2 = createUser(2L, 150_000, RankTier.NONE);
        when(userRepositoryPort.findAllOrderByTotalExpDesc()).thenReturn(List.of(rank1, rank2));
        when(seasonRepositoryPort.findCurrentSeason()).thenReturn(Optional.empty());

        service.updateRankTiers();

        ArgumentCaptor<List<User>> captor = ArgumentCaptor.forClass(List.class);
        verify(userRepositoryPort).saveAll(captor.capture());
        assertThat(captor.getValue()).hasSize(2);
        assertThat(captor.getValue().get(0).currentRankTier()).isEqualTo(RankTier.AURA);
        assertThat(captor.getValue().get(1).currentRankTier()).isEqualTo(RankTier.MASTER);
    }

    @Test
    @DisplayName("3위이고 exp >= 150000이면 MASTER를 부여한다")
    void computeRankTier_rank3_expOver150k_returnsMaster() {
        User rank1 = createUser(1L, 250_000, RankTier.NONE);
        User rank2 = createUser(2L, 200_000, RankTier.NONE);
        User rank3 = createUser(3L, 150_000, RankTier.NONE);
        when(userRepositoryPort.findAllOrderByTotalExpDesc()).thenReturn(List.of(rank1, rank2, rank3));
        when(seasonRepositoryPort.findCurrentSeason()).thenReturn(Optional.empty());

        service.updateRankTiers();

        ArgumentCaptor<List<User>> captor = ArgumentCaptor.forClass(List.class);
        verify(userRepositoryPort).saveAll(captor.capture());
        assertThat(captor.getValue()).hasSize(3);
        assertThat(captor.getValue().get(2).currentRankTier()).isEqualTo(RankTier.MASTER);
    }

    @Test
    @DisplayName("4위이면 exp에 관계없이 NONE을 부여한다")
    void computeRankTier_rank4_returnsNone() {
        User rank1 = createUser(1L, 200_000, RankTier.AURA);
        User rank2 = createUser(2L, 180_000, RankTier.MASTER);
        User rank3 = createUser(3L, 150_000, RankTier.MASTER);
        User rank4 = createUser(4L, 140_000, RankTier.NONE);
        when(userRepositoryPort.findAllOrderByTotalExpDesc()).thenReturn(List.of(rank1, rank2, rank3, rank4));
        when(seasonRepositoryPort.findCurrentSeason()).thenReturn(Optional.empty());

        service.updateRankTiers();

        verify(userRepositoryPort, never()).saveAll(any());
    }

    @Test
    @DisplayName("1위이지만 exp < 150000이면 NONE을 부여한다")
    void computeRankTier_rank1_expUnder150k_returnsNone() {
        User user = createUser(1L, 149_999, RankTier.AURA);
        when(userRepositoryPort.findAllOrderByTotalExpDesc()).thenReturn(List.of(user));
        when(seasonRepositoryPort.findCurrentSeason()).thenReturn(Optional.empty());

        service.updateRankTiers();

        ArgumentCaptor<List<User>> captor = ArgumentCaptor.forClass(List.class);
        verify(userRepositoryPort).saveAll(captor.capture());
        assertThat(captor.getValue().get(0).currentRankTier()).isEqualTo(RankTier.NONE);
    }

    @Test
    @DisplayName("1위이고 exp가 150000~199999이면 MASTER를 부여한다")
    void computeRankTier_rank1_expBetween150kAnd200k_returnsMaster() {
        User user = createUser(1L, 199_999, RankTier.NONE);
        when(userRepositoryPort.findAllOrderByTotalExpDesc()).thenReturn(List.of(user));
        when(seasonRepositoryPort.findCurrentSeason()).thenReturn(Optional.empty());

        service.updateRankTiers();

        ArgumentCaptor<List<User>> captor = ArgumentCaptor.forClass(List.class);
        verify(userRepositoryPort).saveAll(captor.capture());
        assertThat(captor.getValue().get(0).currentRankTier()).isEqualTo(RankTier.MASTER);
    }

    @Test
    @DisplayName("2위이지만 exp < 150000이면 NONE을 부여한다")
    void computeRankTier_rank2_expUnder150k_returnsNone() {
        User rank1 = createUser(1L, 250_000, RankTier.NONE);
        User rank2 = createUser(2L, 149_999, RankTier.NONE);
        when(userRepositoryPort.findAllOrderByTotalExpDesc()).thenReturn(List.of(rank1, rank2));
        when(seasonRepositoryPort.findCurrentSeason()).thenReturn(Optional.empty());

        service.updateRankTiers();

        ArgumentCaptor<List<User>> captor = ArgumentCaptor.forClass(List.class);
        verify(userRepositoryPort).saveAll(captor.capture());
        assertThat(captor.getValue()).hasSize(1);
        assertThat(captor.getValue().get(0).currentRankTier()).isEqualTo(RankTier.AURA);
    }

    @Test
    @DisplayName("티어가 변경되지 않은 유저는 saveAll을 호출하지 않는다")
    void updateRankTiers_unchangedTier_doesNotSaveUser() {
        User user = createUser(1L, 200_000, RankTier.AURA);
        when(userRepositoryPort.findAllOrderByTotalExpDesc()).thenReturn(List.of(user));
        when(seasonRepositoryPort.findCurrentSeason()).thenReturn(Optional.empty());

        service.updateRankTiers();

        verify(userRepositoryPort, never()).saveAll(any());
    }

    // ===== 히스토리 저장 =====

    @Test
    @DisplayName("활성 시즌이 있을 때 모든 유저의 RankHistory를 저장한다")
    void updateRankTiers_withActiveSeason_savesAllHistories() {
        User rank1 = createUser(1L, 200_000, RankTier.NONE);
        User rank2 = createUser(2L, 150_000, RankTier.NONE);
        when(userRepositoryPort.findAllOrderByTotalExpDesc()).thenReturn(List.of(rank1, rank2));
        when(seasonRepositoryPort.findCurrentSeason()).thenReturn(Optional.of(createSeason(10L)));

        service.updateRankTiers();

        ArgumentCaptor<List<UserRankHistory>> captor = ArgumentCaptor.forClass(List.class);
        verify(userRankHistoryRepositoryPort).saveAll(captor.capture());
        assertThat(captor.getValue()).hasSize(2);
    }

    @Test
    @DisplayName("활성 시즌이 없을 때 RankHistory를 저장하지 않는다")
    void updateRankTiers_withoutActiveSeason_doesNotSaveHistory() {
        User user = createUser(1L, 200_000, RankTier.NONE);
        when(userRepositoryPort.findAllOrderByTotalExpDesc()).thenReturn(List.of(user));
        when(seasonRepositoryPort.findCurrentSeason()).thenReturn(Optional.empty());

        service.updateRankTiers();

        verify(userRankHistoryRepositoryPort, never()).saveAll(any());
    }

    @Test
    @DisplayName("저장되는 RankHistory의 날짜는 오늘이다")
    void updateRankTiers_historyDate_isToday() {
        User user = createUser(1L, 200_000, RankTier.NONE);
        when(userRepositoryPort.findAllOrderByTotalExpDesc()).thenReturn(List.of(user));
        when(seasonRepositoryPort.findCurrentSeason()).thenReturn(Optional.of(createSeason(10L)));

        service.updateRankTiers();

        ArgumentCaptor<List<UserRankHistory>> captor = ArgumentCaptor.forClass(List.class);
        verify(userRankHistoryRepositoryPort).saveAll(captor.capture());
        assertThat(captor.getValue().get(0).date()).isEqualTo(LocalDate.now());
    }

    @Test
    @DisplayName("RankHistory의 expTier는 totalExp 기준으로 계산된다")
    void updateRankTiers_expTier_isComputedFromTotalExp() {
        User user = createUser(1L, 15_000, RankTier.NONE); // SILVER 범위
        when(userRepositoryPort.findAllOrderByTotalExpDesc()).thenReturn(List.of(user));
        when(seasonRepositoryPort.findCurrentSeason()).thenReturn(Optional.of(createSeason(10L)));

        service.updateRankTiers();

        ArgumentCaptor<List<UserRankHistory>> captor = ArgumentCaptor.forClass(List.class);
        verify(userRankHistoryRepositoryPort).saveAll(captor.capture());
        assertThat(captor.getValue().get(0).expTier()).isEqualTo(ExpTier.SILVER);
    }

    @Test
    @DisplayName("RankHistory에 순위와 rankTier가 올바르게 저장된다")
    void updateRankTiers_historyContainsCorrectRankAndTier() {
        User rank1 = createUser(1L, 200_000, RankTier.NONE);
        User rank2 = createUser(2L, 150_000, RankTier.NONE);
        when(userRepositoryPort.findAllOrderByTotalExpDesc()).thenReturn(List.of(rank1, rank2));
        when(seasonRepositoryPort.findCurrentSeason()).thenReturn(Optional.of(createSeason(10L)));

        service.updateRankTiers();

        ArgumentCaptor<List<UserRankHistory>> captor = ArgumentCaptor.forClass(List.class);
        verify(userRankHistoryRepositoryPort).saveAll(captor.capture());

        List<UserRankHistory> histories = captor.getValue();
        assertThat(histories.get(0).rank()).isEqualTo(1);
        assertThat(histories.get(0).rankTier()).isEqualTo(RankTier.AURA);
        assertThat(histories.get(1).rank()).isEqualTo(2);
        assertThat(histories.get(1).rankTier()).isEqualTo(RankTier.MASTER);
    }

    @Test
    @DisplayName("RankHistory의 seasonId는 현재 시즌 id로 저장된다")
    void updateRankTiers_historySeasonId_matchesCurrentSeason() {
        User user = createUser(1L, 200_000, RankTier.NONE);
        Season season = createSeason(42L);
        when(userRepositoryPort.findAllOrderByTotalExpDesc()).thenReturn(List.of(user));
        when(seasonRepositoryPort.findCurrentSeason()).thenReturn(Optional.of(season));

        service.updateRankTiers();

        ArgumentCaptor<List<UserRankHistory>> captor = ArgumentCaptor.forClass(List.class);
        verify(userRankHistoryRepositoryPort).saveAll(captor.capture());
        assertThat(captor.getValue().get(0).seasonId()).isEqualTo(42L);
    }

    // ===== helpers =====

    private User createUser(Long id, int totalExp, RankTier currentRankTier) {
        return new User(
                id, Instant.now(), Instant.now(),
                "user" + id, "user" + id + "@test.com", "User" + id,
                "pw", Role.USER, "", totalExp, 0,
                Major.NONE, UserStatus.ACTIVE, null, false, currentRankTier,
                ExpTier.fromExp(totalExp),
                0
        );
    }

    private Season createSeason(Long id) {
        return new Season(
                id, Instant.now(), Instant.now(), "시즌" + id,
                LocalDate.now().minusDays(30), LocalDate.now().plusDays(30)
        );
    }
}
