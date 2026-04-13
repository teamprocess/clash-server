package com.process.clash.application.shop.season.service;

import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import com.process.clash.application.user.userexphistory.port.out.UserExpHistoryRepositoryPort;
import com.process.clash.application.user.usergoodshistory.port.out.UserGoodsHistoryRepositoryPort;
import com.process.clash.domain.common.enums.GoodsActingCategory;
import com.process.clash.domain.common.enums.Major;
import com.process.clash.domain.user.user.entity.User;
import com.process.clash.domain.user.user.enums.Role;
import com.process.clash.domain.user.user.enums.UserStatus;
import com.process.clash.domain.user.userexphistory.entity.UserExpHistory;
import com.process.clash.domain.user.userexphistory.enums.ExpActingCategory;
import com.process.clash.domain.user.usergoodshistory.entity.UserGoodsHistory;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SeasonFinishServiceTest {

    @Mock private UserRepositoryPort userRepositoryPort;
    @Mock private UserExpHistoryRepositoryPort userExpHistoryRepositoryPort;
    @Mock private UserGoodsHistoryRepositoryPort userGoodsHistoryRepositoryPort;

    private SeasonFinishService service;

    @BeforeEach
    void setUp() {
        service = new SeasonFinishService(
                userRepositoryPort,
                userExpHistoryRepositoryPort,
                userGoodsHistoryRepositoryPort
        );
    }

    // ===== 기본 동작 =====

    @Test
    @DisplayName("유저가 없으면 아무것도 저장하지 않는다")
    void resetAllUsersExp_noUsers_doesNothing() {
        when(userRepositoryPort.findAllOrderByTotalExpDesc()).thenReturn(List.of());

        service.resetAllUsersExp();

        verify(userRepositoryPort, never()).saveAll(any());
        verify(userExpHistoryRepositoryPort, never()).saveAll(any());
        verify(userGoodsHistoryRepositoryPort, never()).saveAll(any());
    }

    @Test
    @DisplayName("EXP가 0인 유저는 처리 대상에서 제외한다")
    void resetAllUsersExp_userWithZeroExp_isSkipped() {
        User user = createUser(1L, 0, 0);
        when(userRepositoryPort.findAllOrderByTotalExpDesc()).thenReturn(List.of(user));

        service.resetAllUsersExp();

        verify(userRepositoryPort, never()).saveAll(any());
        verify(userExpHistoryRepositoryPort, never()).saveAll(any());
        verify(userGoodsHistoryRepositoryPort, never()).saveAll(any());
    }

    // ===== 유저 업데이트 =====

    @Test
    @DisplayName("시즌 리셋 후 유저의 EXP는 0이다")
    void resetAllUsersExp_updatedUser_hasZeroExp() {
        User user = createUser(1L, 10_000, 0);
        when(userRepositoryPort.findAllOrderByTotalExpDesc()).thenReturn(List.of(user));

        service.resetAllUsersExp();

        ArgumentCaptor<List<User>> captor = ArgumentCaptor.forClass(List.class);
        verify(userRepositoryPort).saveAll(captor.capture());
        assertThat(captor.getValue().get(0).totalExp()).isEqualTo(0);
    }

    @Test
    @DisplayName("시즌 리셋 후 유저의 RankTier는 NONE이다")
    void resetAllUsersExp_updatedUser_hasNoneRankTier() {
        User user = createUser(1L, 100_000, 0);
        when(userRepositoryPort.findAllOrderByTotalExpDesc()).thenReturn(List.of(user));

        service.resetAllUsersExp();

        ArgumentCaptor<List<User>> captor = ArgumentCaptor.forClass(List.class);
        verify(userRepositoryPort).saveAll(captor.capture());
        assertThat(captor.getValue().get(0).currentRankTier()).isEqualTo(RankTier.NONE);
    }

    @Test
    @DisplayName("시즌 리셋 후 유저의 ExpTier는 UNRANKED이다")
    void resetAllUsersExp_updatedUser_hasUnrankedExpTier() {
        User user = createUser(1L, 50_000, 0);
        when(userRepositoryPort.findAllOrderByTotalExpDesc()).thenReturn(List.of(user));

        service.resetAllUsersExp();

        ArgumentCaptor<List<User>> captor = ArgumentCaptor.forClass(List.class);
        verify(userRepositoryPort).saveAll(captor.capture());
        assertThat(captor.getValue().get(0).currentExpTier()).isEqualTo(ExpTier.UNRANKED);
    }

    // ===== 쿠키 지급 =====

    @Test
    @DisplayName("EXP의 10%를 반올림한 쿠키를 지급한다")
    void resetAllUsersExp_cookieReward_isTenPercentRounded() {
        User user = createUser(1L, 135, 0); // 135 * 0.1 = 13.5 → 14
        when(userRepositoryPort.findAllOrderByTotalExpDesc()).thenReturn(List.of(user));

        service.resetAllUsersExp();

        ArgumentCaptor<List<User>> captor = ArgumentCaptor.forClass(List.class);
        verify(userRepositoryPort).saveAll(captor.capture());
        assertThat(captor.getValue().get(0).totalCookie()).isEqualTo(14);
    }

    @Test
    @DisplayName("기존 쿠키에 보상 쿠키를 더한다")
    void resetAllUsersExp_cookieReward_addedToExistingCookies() {
        User user = createUser(1L, 100, 50); // 100 * 0.1 = 10 → totalCookie = 60
        when(userRepositoryPort.findAllOrderByTotalExpDesc()).thenReturn(List.of(user));

        service.resetAllUsersExp();

        ArgumentCaptor<List<User>> captor = ArgumentCaptor.forClass(List.class);
        verify(userRepositoryPort).saveAll(captor.capture());
        assertThat(captor.getValue().get(0).totalCookie()).isEqualTo(60);
    }

    @Test
    @DisplayName("0.5 이상은 올림한다")
    void resetAllUsersExp_cookieReward_roundsHalfUp() {
        User user = createUser(1L, 5, 0); // 5 * 0.1 = 0.5 → 1
        when(userRepositoryPort.findAllOrderByTotalExpDesc()).thenReturn(List.of(user));

        service.resetAllUsersExp();

        ArgumentCaptor<List<User>> captor = ArgumentCaptor.forClass(List.class);
        verify(userRepositoryPort).saveAll(captor.capture());
        assertThat(captor.getValue().get(0).totalCookie()).isEqualTo(1);
    }

    @Test
    @DisplayName("반올림 결과 쿠키가 0이면 GoodsHistory를 저장하지 않는다")
    void resetAllUsersExp_zeroCookieReward_doesNotSaveGoodsHistory() {
        User user = createUser(1L, 4, 0); // 4 * 0.1 = 0.4 → 0
        when(userRepositoryPort.findAllOrderByTotalExpDesc()).thenReturn(List.of(user));

        service.resetAllUsersExp();

        verify(userGoodsHistoryRepositoryPort, never()).saveAll(any());
    }

    // ===== 이력 기록 =====

    @Test
    @DisplayName("UserExpHistory에 SEASON_RESET 카테고리로 저장한다")
    void resetAllUsersExp_expHistory_hasSEASON_RESET_category() {
        User user = createUser(1L, 1_000, 0);
        when(userRepositoryPort.findAllOrderByTotalExpDesc()).thenReturn(List.of(user));

        service.resetAllUsersExp();

        ArgumentCaptor<List<UserExpHistory>> captor = ArgumentCaptor.forClass(List.class);
        verify(userExpHistoryRepositoryPort).saveAll(captor.capture());
        assertThat(captor.getValue().get(0).actingCategory()).isEqualTo(ExpActingCategory.SEASON_RESET);
    }

    @Test
    @DisplayName("UserExpHistory의 earnExp는 감소한 EXP의 음수값이다")
    void resetAllUsersExp_expHistory_earnExpIsNegativeTotalExp() {
        User user = createUser(1L, 5_000, 0);
        when(userRepositoryPort.findAllOrderByTotalExpDesc()).thenReturn(List.of(user));

        service.resetAllUsersExp();

        ArgumentCaptor<List<UserExpHistory>> captor = ArgumentCaptor.forClass(List.class);
        verify(userExpHistoryRepositoryPort).saveAll(captor.capture());
        assertThat(captor.getValue().get(0).earnExp()).isEqualTo(-5_000);
    }

    @Test
    @DisplayName("UserExpHistory의 날짜는 오늘이다")
    void resetAllUsersExp_expHistory_dateIsToday() {
        User user = createUser(1L, 1_000, 0);
        when(userRepositoryPort.findAllOrderByTotalExpDesc()).thenReturn(List.of(user));

        service.resetAllUsersExp();

        ArgumentCaptor<List<UserExpHistory>> captor = ArgumentCaptor.forClass(List.class);
        verify(userExpHistoryRepositoryPort).saveAll(captor.capture());
        assertThat(captor.getValue().get(0).date()).isEqualTo(LocalDate.now());
    }

    @Test
    @DisplayName("UserGoodsHistory에 SEASON_REWARD 카테고리로 저장한다")
    void resetAllUsersExp_goodsHistory_hasSEASON_REWARD_category() {
        User user = createUser(1L, 1_000, 0); // reward = 100
        when(userRepositoryPort.findAllOrderByTotalExpDesc()).thenReturn(List.of(user));

        service.resetAllUsersExp();

        ArgumentCaptor<List<UserGoodsHistory>> captor = ArgumentCaptor.forClass(List.class);
        verify(userGoodsHistoryRepositoryPort).saveAll(captor.capture());
        assertThat(captor.getValue().get(0).goodsActingCategory()).isEqualTo(GoodsActingCategory.SEASON_REWARD);
    }

    @Test
    @DisplayName("UserGoodsHistory의 variation은 지급된 쿠키 수량이다")
    void resetAllUsersExp_goodsHistory_variationIsCookieReward() {
        User user = createUser(1L, 1_000, 0); // 1000 * 0.1 = 100
        when(userRepositoryPort.findAllOrderByTotalExpDesc()).thenReturn(List.of(user));

        service.resetAllUsersExp();

        ArgumentCaptor<List<UserGoodsHistory>> captor = ArgumentCaptor.forClass(List.class);
        verify(userGoodsHistoryRepositoryPort).saveAll(captor.capture());
        assertThat(captor.getValue().get(0).variation()).isEqualTo(100);
    }

    // ===== 다중 유저 =====

    @Test
    @DisplayName("EXP가 있는 유저만 처리한다")
    void resetAllUsersExp_mixedUsers_onlyProcessesUsersWithExp() {
        User withExp = createUser(1L, 1_000, 0);
        User withoutExp = createUser(2L, 0, 0);
        when(userRepositoryPort.findAllOrderByTotalExpDesc()).thenReturn(List.of(withExp, withoutExp));

        service.resetAllUsersExp();

        ArgumentCaptor<List<User>> captor = ArgumentCaptor.forClass(List.class);
        verify(userRepositoryPort).saveAll(captor.capture());
        assertThat(captor.getValue()).hasSize(1);
        assertThat(captor.getValue().get(0).id()).isEqualTo(1L);
    }

    @Test
    @DisplayName("여러 유저의 ExpHistory를 모두 저장한다")
    void resetAllUsersExp_multipleUsers_savesAllExpHistories() {
        User user1 = createUser(1L, 1_000, 0);
        User user2 = createUser(2L, 2_000, 0);
        when(userRepositoryPort.findAllOrderByTotalExpDesc()).thenReturn(List.of(user1, user2));

        service.resetAllUsersExp();

        ArgumentCaptor<List<UserExpHistory>> captor = ArgumentCaptor.forClass(List.class);
        verify(userExpHistoryRepositoryPort).saveAll(captor.capture());
        assertThat(captor.getValue()).hasSize(2);
    }

    // ===== helpers =====

    private User createUser(Long id, int totalExp, int totalCookie) {
        return new User(
                id, Instant.now(), Instant.now(),
                "user" + id, "user" + id + "@test.com", "User" + id,
                "pw", Role.USER, "", totalExp, totalCookie,
                Major.NONE, UserStatus.ACTIVE, null, false, RankTier.NONE,
                ExpTier.fromExp(totalExp)
        );
    }
}