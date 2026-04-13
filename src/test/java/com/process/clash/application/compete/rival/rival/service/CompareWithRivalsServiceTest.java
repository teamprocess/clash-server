package com.process.clash.application.compete.rival.rival.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.compete.rival.rival.data.CompareWithRivalsData;
import com.process.clash.application.compete.rival.rival.port.out.RivalRepositoryPort;
import com.process.clash.application.github.port.out.GitHubDailyStatsQueryPort;
import com.process.clash.application.record.v2.port.out.RecordSessionV2RepositoryPort;
import com.process.clash.application.shop.season.exception.exception.notfound.SeasonNotFoundException;
import com.process.clash.application.shop.season.port.out.SeasonRepositoryPort;
import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import com.process.clash.application.user.userexphistory.port.out.UserExpHistoryRepositoryPort;
import com.process.clash.domain.common.enums.Major;
import com.process.clash.domain.common.enums.PeriodCategory;
import com.process.clash.domain.common.enums.TargetCategory;
import com.process.clash.domain.shop.season.entity.Season;
import com.process.clash.domain.user.user.entity.User;
import com.process.clash.domain.user.user.enums.Role;
import com.process.clash.domain.user.user.enums.UserStatus;
import com.process.clash.domain.user.userrankhistory.enums.ExpTier;
import com.process.clash.domain.user.userrankhistory.enums.RankTier;
import com.process.clash.infrastructure.config.record.RecordProperties;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CompareWithRivalsServiceTest {

    private static final ZoneId TEST_ZONE = ZoneId.of("Asia/Seoul");
    private static final int BOUNDARY_HOUR = 6;
    private static final Long USER_ID = 1L;
    private static final Long RIVAL_ID = 2L;
    private static final RecordProperties RECORD_PROPS = new RecordProperties("Asia/Seoul", BOUNDARY_HOUR);

    @Mock private GitHubDailyStatsQueryPort githubDailyStatsQueryPort;
    @Mock private RivalRepositoryPort rivalRepositoryPort;
    @Mock private UserExpHistoryRepositoryPort userExpHistoryRepositoryPort;
    @Mock private UserRepositoryPort userRepositoryPort;
    @Mock private RecordSessionV2RepositoryPort recordSessionRepositoryPort;
    @Mock private SeasonRepositoryPort seasonRepositoryPort;

    private CompareWithRivalsService service;

    @BeforeEach
    void setUp() {
        service = new CompareWithRivalsService(
                githubDailyStatsQueryPort,
                rivalRepositoryPort,
                userExpHistoryRepositoryPort,
                userRepositoryPort,
                recordSessionRepositoryPort,
                seasonRepositoryPort,
                TEST_ZONE,
                RECORD_PROPS
        );
        // rivalIds는 서비스 내부에서 add()로 수정되므로 매 호출마다 새 리스트를 반환
        lenient().when(rivalRepositoryPort.findOpponentIdByUserId(USER_ID))
                .thenAnswer(invocation -> new ArrayList<>(List.of(RIVAL_ID)));
        lenient().when(userRepositoryPort.findAllByIds(any()))
                .thenReturn(List.of(createUser(USER_ID), createUser(RIVAL_ID)));
        lenient().when(userExpHistoryRepositoryPort.findDailyDataByUserIds(any(), any(), any())).thenReturn(List.of());
        lenient().when(userExpHistoryRepositoryPort.findWeeklyDataByUserIds(any(), any(), any())).thenReturn(List.of());
        lenient().when(userExpHistoryRepositoryPort.findMonthlyDataByUserIds(any(), any(), any())).thenReturn(List.of());
        lenient().when(githubDailyStatsQueryPort.findDailyContributionsByUserIds(any(), any(), any())).thenReturn(List.of());
        lenient().when(githubDailyStatsQueryPort.findWeeklyContributionsByUserIds(any(), any(), any())).thenReturn(List.of());
        lenient().when(githubDailyStatsQueryPort.findMonthlyContributionsByUserIds(any(), any(), any())).thenReturn(List.of());
        lenient().when(recordSessionRepositoryPort.findDailyStudyTimeByUserIds(any(), any(), any(), any())).thenReturn(List.of());
        lenient().when(recordSessionRepositoryPort.findWeeklyStudyTimeByUserIds(any(), any(), any(), any())).thenReturn(List.of());
        lenient().when(recordSessionRepositoryPort.findMonthlyStudyTimeByUserIds(any(), any(), any(), any())).thenReturn(List.of());
        lenient().when(seasonRepositoryPort.findCurrentSeason()).thenReturn(Optional.empty());
    }

    private CompareWithRivalsData.Command command(TargetCategory category, PeriodCategory period) {
        return CompareWithRivalsData.Command.of(new Actor(USER_ID), category, period);
    }

    private Season seasonInProgress() {
        LocalDate today = LocalDate.now(TEST_ZONE);
        return new Season(1L, Instant.now(), Instant.now(), "TEST", today.minusDays(10), today.plusDays(10));
    }

    private Season seasonEnded() {
        LocalDate today = LocalDate.now(TEST_ZONE);
        return new Season(1L, Instant.now(), Instant.now(), "TEST", today.minusDays(20), today.minusDays(1));
    }

    private User createUser(Long id) {
        return new User(
                id, Instant.now(), Instant.now(),
                "user" + id, "user" + id + "@test.com", "User" + id,
                "password", Role.USER, "", 0, 0,
                Major.NONE, UserStatus.ACTIVE, null, false, RankTier.NONE, ExpTier.UNRANKED
        );
    }

    // ===== SEASON 기간 날짜 범위 검증 =====

    @Test
    @DisplayName("SEASON + EXP: startDate=시즌시작일, endDate=오늘로 findDailyDataByUserIds를 호출한다")
    void season_exp_callsDailyDataWithSeasonDateRange() {
        LocalDate today = LocalDate.now(TEST_ZONE);
        Season season = seasonInProgress();
        when(seasonRepositoryPort.findCurrentSeason()).thenReturn(Optional.of(season));

        service.execute(command(TargetCategory.EXP, PeriodCategory.SEASON));

        ArgumentCaptor<LocalDate> start = ArgumentCaptor.forClass(LocalDate.class);
        ArgumentCaptor<LocalDate> end = ArgumentCaptor.forClass(LocalDate.class);
        verify(userExpHistoryRepositoryPort)
                .findDailyDataByUserIds(any(), start.capture(), end.capture());

        assertThat(start.getValue()).isEqualTo(season.startDate());
        assertThat(end.getValue()).isEqualTo(today);
    }

    @Test
    @DisplayName("SEASON + GITHUB: startDate=시즌시작일, endDate=오늘로 findDailyContributionsByUserIds를 호출한다")
    void season_github_callsDailyContributionsWithSeasonDateRange() {
        LocalDate today = LocalDate.now(TEST_ZONE);
        Season season = seasonInProgress();
        when(seasonRepositoryPort.findCurrentSeason()).thenReturn(Optional.of(season));

        service.execute(command(TargetCategory.GITHUB, PeriodCategory.SEASON));

        ArgumentCaptor<LocalDate> start = ArgumentCaptor.forClass(LocalDate.class);
        ArgumentCaptor<LocalDate> end = ArgumentCaptor.forClass(LocalDate.class);
        verify(githubDailyStatsQueryPort)
                .findDailyContributionsByUserIds(any(), start.capture(), end.capture());

        assertThat(start.getValue()).isEqualTo(season.startDate());
        assertThat(end.getValue()).isEqualTo(today);
    }

    @Test
    @DisplayName("SEASON + ACTIVE_TIME: findDailyStudyTimeByUserIds를 호출한다")
    void season_activeTime_callsDailyStudyTime() {
        Season season = seasonInProgress();
        when(seasonRepositoryPort.findCurrentSeason()).thenReturn(Optional.of(season));

        service.execute(command(TargetCategory.ACTIVE_TIME, PeriodCategory.SEASON));

        verify(recordSessionRepositoryPort)
                .findDailyStudyTimeByUserIds(any(), any(), any(), any());
    }

    @Test
    @DisplayName("SEASON: 진행 중인 시즌이면 데이터 포인트가 시즌 시작일부터 오늘까지 생성된다")
    void season_inProgress_dataPointsFromSeasonStartToToday() {
        LocalDate today = LocalDate.now(TEST_ZONE);
        Season season = seasonInProgress();
        when(seasonRepositoryPort.findCurrentSeason()).thenReturn(Optional.of(season));

        CompareWithRivalsData.Result result = service.execute(command(TargetCategory.EXP, PeriodCategory.SEASON));

        int expectedSize = (int) (today.toEpochDay() - season.startDate().toEpochDay() + 1);
        result.totalData().forEach(td -> assertThat(td.dataPoint()).hasSize(expectedSize));
    }

    @Test
    @DisplayName("SEASON: 종료된 시즌이면 endDate가 시즌 종료일로 제한된다")
    void season_ended_endDateClampedToSeasonEndDate() {
        Season season = seasonEnded();
        when(seasonRepositoryPort.findCurrentSeason()).thenReturn(Optional.of(season));

        service.execute(command(TargetCategory.EXP, PeriodCategory.SEASON));

        ArgumentCaptor<LocalDate> end = ArgumentCaptor.forClass(LocalDate.class);
        verify(userExpHistoryRepositoryPort)
                .findDailyDataByUserIds(any(), any(), end.capture());

        assertThat(end.getValue()).isEqualTo(season.endDate());
    }

    @Test
    @DisplayName("SEASON: 현재 시즌이 없으면 SeasonNotFoundException이 발생한다")
    void season_noCurrentSeason_throwsSeasonNotFoundException() {
        when(seasonRepositoryPort.findCurrentSeason()).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.execute(command(TargetCategory.EXP, PeriodCategory.SEASON)))
                .isInstanceOf(SeasonNotFoundException.class);
    }
}