package com.process.clash.application.ranking.service;

import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import com.process.clash.application.user.userexphistory.port.out.UserExpHistoryRepositoryPort;
import com.process.clash.application.user.usergoodshistory.port.out.UserGoodsHistoryRepositoryPort;
import com.process.clash.domain.common.enums.GoodsActingCategory;
import com.process.clash.domain.user.usergoodshistory.entity.UserGoodsHistory;
import com.process.clash.domain.common.enums.Major;
import com.process.clash.domain.user.user.entity.User;
import com.process.clash.domain.user.user.enums.Role;
import com.process.clash.domain.user.user.enums.UserStatus;
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
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DailyRankingRewardServiceTest {

    @Mock private UserExpHistoryRepositoryPort userExpHistoryRepositoryPort;
    @Mock private UserRepositoryPort userRepositoryPort;
    @Mock private UserGoodsHistoryRepositoryPort userGoodsHistoryRepositoryPort;

    private DailyRankingRewardService service;

    @BeforeEach
    void setUp() {
        service = new DailyRankingRewardService(userExpHistoryRepositoryPort, userRepositoryPort, userGoodsHistoryRepositoryPort);
    }

    @Test
    @DisplayName("전날 EXP가 있는 유저가 없으면 쿠키를 지급하지 않는다")
    void rewardDailyRankingWinners_noTopUsers_doesNotSaveAnyUser() {
        when(userExpHistoryRepositoryPort.findTopUserIdsByDailyExp(any(), eq(3)))
                .thenReturn(List.of());

        service.rewardDailyRankingWinners();

        verify(userRepositoryPort, never()).findByIdForUpdate(any());
        verify(userRepositoryPort, never()).save(any());
        verify(userGoodsHistoryRepositoryPort, never()).save(any());
    }

    @Test
    @DisplayName("어제 날짜로 EXP 랭킹을 조회한다")
    void rewardDailyRankingWinners_queriesWithYesterdayDate() {
        LocalDate yesterday = LocalDate.now(ZoneId.of("Asia/Seoul")).minusDays(1);
        when(userExpHistoryRepositoryPort.findTopUserIdsByDailyExp(any(), eq(3)))
                .thenReturn(List.of());

        service.rewardDailyRankingWinners();

        verify(userExpHistoryRepositoryPort).findTopUserIdsByDailyExp(eq(yesterday), eq(3));
    }

    @Test
    @DisplayName("1위 유저에게 1000 쿠키를 지급한다")
    void rewardDailyRankingWinners_rank1_receives1000Cookies() {
        User rank1 = createUser(1L, 100);
        when(userExpHistoryRepositoryPort.findTopUserIdsByDailyExp(any(), eq(3)))
                .thenReturn(List.of(1L));
        when(userRepositoryPort.findByIdForUpdate(1L)).thenReturn(Optional.of(rank1));

        service.rewardDailyRankingWinners();

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepositoryPort).save(captor.capture());
        assertThat(captor.getValue().totalCookie()).isEqualTo(100 + 1000);
    }

    @Test
    @DisplayName("2위 유저에게 500 쿠키를 지급한다")
    void rewardDailyRankingWinners_rank2_receives500Cookies() {
        User rank1 = createUser(1L, 0);
        User rank2 = createUser(2L, 0);
        when(userExpHistoryRepositoryPort.findTopUserIdsByDailyExp(any(), eq(3)))
                .thenReturn(List.of(1L, 2L));
        when(userRepositoryPort.findByIdForUpdate(1L)).thenReturn(Optional.of(rank1));
        when(userRepositoryPort.findByIdForUpdate(2L)).thenReturn(Optional.of(rank2));

        service.rewardDailyRankingWinners();

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepositoryPort, times(2)).save(captor.capture());
        assertThat(captor.getAllValues().get(1).totalCookie()).isEqualTo(500);
    }

    @Test
    @DisplayName("3위 유저에게 300 쿠키를 지급한다")
    void rewardDailyRankingWinners_rank3_receives300Cookies() {
        User rank1 = createUser(1L, 0);
        User rank2 = createUser(2L, 0);
        User rank3 = createUser(3L, 0);
        when(userExpHistoryRepositoryPort.findTopUserIdsByDailyExp(any(), eq(3)))
                .thenReturn(List.of(1L, 2L, 3L));
        when(userRepositoryPort.findByIdForUpdate(1L)).thenReturn(Optional.of(rank1));
        when(userRepositoryPort.findByIdForUpdate(2L)).thenReturn(Optional.of(rank2));
        when(userRepositoryPort.findByIdForUpdate(3L)).thenReturn(Optional.of(rank3));

        service.rewardDailyRankingWinners();

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepositoryPort, times(3)).save(captor.capture());
        assertThat(captor.getAllValues().get(2).totalCookie()).isEqualTo(300);
    }

    @Test
    @DisplayName("상위 유저가 3명 미만이면 존재하는 유저에게만 쿠키를 지급한다")
    void rewardDailyRankingWinners_fewerThan3Users_rewardsAllExisting() {
        User rank1 = createUser(1L, 0);
        when(userExpHistoryRepositoryPort.findTopUserIdsByDailyExp(any(), eq(3)))
                .thenReturn(List.of(1L));
        when(userRepositoryPort.findByIdForUpdate(1L)).thenReturn(Optional.of(rank1));

        service.rewardDailyRankingWinners();

        verify(userRepositoryPort, times(1)).save(any());
    }

    @Test
    @DisplayName("유저를 찾을 수 없으면 해당 순위를 스킵하고 다음 순위에 보상을 지급한다")
    void rewardDailyRankingWinners_userNotFound_skipsAndContinues() {
        User rank2 = createUser(2L, 0);
        when(userExpHistoryRepositoryPort.findTopUserIdsByDailyExp(any(), eq(3)))
                .thenReturn(List.of(1L, 2L));
        when(userRepositoryPort.findByIdForUpdate(1L)).thenReturn(Optional.empty());
        when(userRepositoryPort.findByIdForUpdate(2L)).thenReturn(Optional.of(rank2));

        service.rewardDailyRankingWinners();

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepositoryPort, times(1)).save(captor.capture());
        assertThat(captor.getValue().totalCookie()).isEqualTo(500);
    }

    @Test
    @DisplayName("쿠키 지급 시 UserGoodsHistory에 DAILY_RANKING_REWARD 카테고리로 기록을 저장한다")
    void rewardDailyRankingWinners_savesGoodsHistory() {
        User rank1 = createUser(1L, 0);
        when(userExpHistoryRepositoryPort.findTopUserIdsByDailyExp(any(), eq(3)))
                .thenReturn(List.of(1L));
        when(userRepositoryPort.findByIdForUpdate(1L)).thenReturn(Optional.of(rank1));

        service.rewardDailyRankingWinners();

        ArgumentCaptor<UserGoodsHistory> captor = ArgumentCaptor.forClass(UserGoodsHistory.class);
        verify(userGoodsHistoryRepositoryPort, times(1)).save(captor.capture());
        UserGoodsHistory history = captor.getValue();
        assertThat(history.goodsActingCategory()).isEqualTo(GoodsActingCategory.DAILY_RANKING_REWARD);
        assertThat(history.variation()).isEqualTo(1000);
        assertThat(history.userId()).isEqualTo(1L);
        assertThat(history.productId()).isNull();
    }

    @Test
    @DisplayName("3명 모두 쿠키를 받으면 UserGoodsHistory 기록도 3건 저장된다")
    void rewardDailyRankingWinners_savesGoodsHistoryForEachWinner() {
        User rank1 = createUser(1L, 0);
        User rank2 = createUser(2L, 0);
        User rank3 = createUser(3L, 0);
        when(userExpHistoryRepositoryPort.findTopUserIdsByDailyExp(any(), eq(3)))
                .thenReturn(List.of(1L, 2L, 3L));
        when(userRepositoryPort.findByIdForUpdate(1L)).thenReturn(Optional.of(rank1));
        when(userRepositoryPort.findByIdForUpdate(2L)).thenReturn(Optional.of(rank2));
        when(userRepositoryPort.findByIdForUpdate(3L)).thenReturn(Optional.of(rank3));

        service.rewardDailyRankingWinners();

        ArgumentCaptor<UserGoodsHistory> captor = ArgumentCaptor.forClass(UserGoodsHistory.class);
        verify(userGoodsHistoryRepositoryPort, times(3)).save(captor.capture());
        List<UserGoodsHistory> histories = captor.getAllValues();
        assertThat(histories.get(0).variation()).isEqualTo(1000);
        assertThat(histories.get(1).variation()).isEqualTo(500);
        assertThat(histories.get(2).variation()).isEqualTo(300);
    }

    // ===== helpers =====

    private User createUser(Long id, int totalCookie) {
        return new User(
                id, Instant.now(), Instant.now(),
                "user" + id, "user" + id + "@test.com", "User" + id,
                "pw", Role.USER, "", 0, totalCookie,
                Major.NONE, UserStatus.ACTIVE, null, false, RankTier.NONE,
                ExpTier.UNRANKED,
                0
        );
    }
}