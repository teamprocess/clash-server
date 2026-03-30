package com.process.clash.application.compete.my.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.compete.my.data.AnalyzeMyPeriodData;
import com.process.clash.application.github.port.out.GitHubDailyStatsQueryPort;
import com.process.clash.application.record.v2.port.out.RecordSessionV2RepositoryPort;
import com.process.clash.application.shop.season.exception.exception.notfound.SeasonNotFoundException;
import com.process.clash.application.shop.season.port.out.SeasonRepositoryPort;
import com.process.clash.application.user.userexphistory.port.out.UserExpHistoryRepositoryPort;
import com.process.clash.domain.common.enums.PeriodCategory;
import com.process.clash.domain.common.enums.TargetCategory;
import com.process.clash.domain.shop.season.entity.Season;
import com.process.clash.infrastructure.config.record.RecordProperties;
import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
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
class AnalyzeMyPeriodServiceTest {

    private static final ZoneId TEST_ZONE = ZoneId.of("Asia/Seoul");
    private static final int BOUNDARY_HOUR = 6;
    private static final Long USER_ID = 1L;
    private static final RecordProperties RECORD_PROPS = new RecordProperties("Asia/Seoul", BOUNDARY_HOUR);

    @Mock private GitHubDailyStatsQueryPort githubDailyStatsQueryPort;
    @Mock private UserExpHistoryRepositoryPort userExpHistoryRepositoryPort;
    @Mock private RecordSessionV2RepositoryPort recordSessionRepositoryPort;
    @Mock private SeasonRepositoryPort seasonRepositoryPort;

    private AnalyzeMyPeriodService service;

    @BeforeEach
    void setUp() {
        service = new AnalyzeMyPeriodService(
                githubDailyStatsQueryPort,
                userExpHistoryRepositoryPort,
                recordSessionRepositoryPort,
                seasonRepositoryPort,
                TEST_ZONE,
                RECORD_PROPS
        );
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

    private AnalyzeMyPeriodData.Command command(TargetCategory category, PeriodCategory period) {
        return AnalyzeMyPeriodData.Command.of(new Actor(USER_ID), category, period);
    }

    private Season seasonInProgress() {
        LocalDate today = LocalDate.now(TEST_ZONE);
        return new Season(1L, Instant.now(), Instant.now(), "TEST", today.minusDays(10), today.plusDays(10));
    }

    private Season seasonEnded() {
        LocalDate today = LocalDate.now(TEST_ZONE);
        return new Season(1L, Instant.now(), Instant.now(), "TEST", today.minusDays(20), today.minusDays(1));
    }

    // ===== DAY 기간 날짜 범위 검증 =====

    @Test
    @DisplayName("DAY + EXP: startDate=오늘-6일, endDate=오늘로 findDailyDataByUserIds를 호출한다")
    void day_exp_callsWithCorrectDateRange() {
        LocalDate today = LocalDate.now(TEST_ZONE);

        service.execute(command(TargetCategory.EXP, PeriodCategory.DAY));

        ArgumentCaptor<LocalDate> start = ArgumentCaptor.forClass(LocalDate.class);
        ArgumentCaptor<LocalDate> end = ArgumentCaptor.forClass(LocalDate.class);
        verify(userExpHistoryRepositoryPort)
                .findDailyDataByUserIds(eq(List.of(USER_ID)), start.capture(), end.capture());

        assertThat(start.getValue()).isEqualTo(today.minusDays(6));
        assertThat(end.getValue()).isEqualTo(today);
    }

    @Test
    @DisplayName("DAY + GITHUB: startDate=오늘-6일, endDate=오늘로 findDailyContributionsByUserIds를 호출한다")
    void day_github_callsWithCorrectDateRange() {
        LocalDate today = LocalDate.now(TEST_ZONE);

        service.execute(command(TargetCategory.GITHUB, PeriodCategory.DAY));

        ArgumentCaptor<LocalDate> start = ArgumentCaptor.forClass(LocalDate.class);
        ArgumentCaptor<LocalDate> end = ArgumentCaptor.forClass(LocalDate.class);
        verify(githubDailyStatsQueryPort)
                .findDailyContributionsByUserIds(eq(List.of(USER_ID)), start.capture(), end.capture());

        assertThat(start.getValue()).isEqualTo(today.minusDays(6));
        assertThat(end.getValue()).isEqualTo(today);
    }

    @Test
    @DisplayName("DAY + ACTIVE_TIME: findDailyStudyTimeByUserIds를 호출한다")
    void day_activeTime_callsDailyStudyTime() {
        service.execute(command(TargetCategory.ACTIVE_TIME, PeriodCategory.DAY));

        verify(recordSessionRepositoryPort)
                .findDailyStudyTimeByUserIds(eq(List.of(USER_ID)), any(), any(), any());
    }

    // ===== WEEK 기간 포트 라우팅 검증 =====

    @Test
    @DisplayName("WEEK + EXP: findWeeklyDataByUserIds를 호출한다")
    void week_exp_callsWeeklyData() {
        service.execute(command(TargetCategory.EXP, PeriodCategory.WEEK));

        verify(userExpHistoryRepositoryPort)
                .findWeeklyDataByUserIds(eq(List.of(USER_ID)), any(), any());
    }

    @Test
    @DisplayName("WEEK + GITHUB: findWeeklyContributionsByUserIds를 호출한다")
    void week_github_callsWeeklyContributions() {
        service.execute(command(TargetCategory.GITHUB, PeriodCategory.WEEK));

        verify(githubDailyStatsQueryPort)
                .findWeeklyContributionsByUserIds(eq(List.of(USER_ID)), any(), any());
    }

    @Test
    @DisplayName("WEEK + ACTIVE_TIME: findWeeklyStudyTimeByUserIds를 호출한다")
    void week_activeTime_callsWeeklyStudyTime() {
        service.execute(command(TargetCategory.ACTIVE_TIME, PeriodCategory.WEEK));

        verify(recordSessionRepositoryPort)
                .findWeeklyStudyTimeByUserIds(eq(List.of(USER_ID)), any(), any(), any());
    }

    // ===== MONTH 기간 날짜 범위 검증 =====

    @Test
    @DisplayName("MONTH + EXP: startDate=현재월 1일-6개월, endDate=오늘로 findMonthlyDataByUserIds를 호출한다")
    void month_exp_callsWithCorrectDateRange() {
        LocalDate today = LocalDate.now(TEST_ZONE);
        LocalDate expectedStart = today.withDayOfMonth(1).minusMonths(6);

        service.execute(command(TargetCategory.EXP, PeriodCategory.MONTH));

        ArgumentCaptor<LocalDate> start = ArgumentCaptor.forClass(LocalDate.class);
        ArgumentCaptor<LocalDate> end = ArgumentCaptor.forClass(LocalDate.class);
        verify(userExpHistoryRepositoryPort)
                .findMonthlyDataByUserIds(eq(List.of(USER_ID)), start.capture(), end.capture());

        assertThat(start.getValue()).isEqualTo(expectedStart);
        assertThat(end.getValue()).isEqualTo(today);
    }

    @Test
    @DisplayName("MONTH + GITHUB: startDate=현재월 1일-6개월, endDate=오늘로 findMonthlyContributionsByUserIds를 호출한다")
    void month_github_callsWithCorrectDateRange() {
        LocalDate today = LocalDate.now(TEST_ZONE);
        LocalDate expectedStart = today.withDayOfMonth(1).minusMonths(6);

        service.execute(command(TargetCategory.GITHUB, PeriodCategory.MONTH));

        ArgumentCaptor<LocalDate> start = ArgumentCaptor.forClass(LocalDate.class);
        ArgumentCaptor<LocalDate> end = ArgumentCaptor.forClass(LocalDate.class);
        verify(githubDailyStatsQueryPort)
                .findMonthlyContributionsByUserIds(eq(List.of(USER_ID)), start.capture(), end.capture());

        assertThat(start.getValue()).isEqualTo(expectedStart);
        assertThat(end.getValue()).isEqualTo(today);
    }

    @Test
    @DisplayName("MONTH + ACTIVE_TIME: findMonthlyStudyTimeByUserIds를 호출한다")
    void month_activeTime_callsMonthlyStudyTime() {
        service.execute(command(TargetCategory.ACTIVE_TIME, PeriodCategory.MONTH));

        verify(recordSessionRepositoryPort)
                .findMonthlyStudyTimeByUserIds(eq(List.of(USER_ID)), any(), any(), any());
    }

    // ===== 결과 구조 검증 =====

    @Test
    @DisplayName("DAY: 데이터가 없으면 7개 날짜 모두 point=0.0으로 채워진다")
    void day_emptyData_allDataPointsAreZero() {
        AnalyzeMyPeriodData.Result result = service.execute(command(TargetCategory.EXP, PeriodCategory.DAY));

        assertThat(result.dataPoints()).hasSize(7);
        assertThat(result.dataPoints()).allMatch(dp -> dp.point() == 0.0);
    }

    @Test
    @DisplayName("WEEK: 데이터가 없으면 7개 날짜 모두 point=0.0으로 채워진다")
    void week_emptyData_allDataPointsAreZero() {
        AnalyzeMyPeriodData.Result result = service.execute(command(TargetCategory.EXP, PeriodCategory.WEEK));

        assertThat(result.dataPoints()).hasSize(7);
        assertThat(result.dataPoints()).allMatch(dp -> dp.point() == 0.0);
    }

    @Test
    @DisplayName("MONTH: 데이터가 없으면 7개 날짜 모두 point=0.0으로 채워진다")
    void month_emptyData_allDataPointsAreZero() {
        AnalyzeMyPeriodData.Result result = service.execute(command(TargetCategory.EXP, PeriodCategory.MONTH));

        assertThat(result.dataPoints()).hasSize(7);
        assertThat(result.dataPoints()).allMatch(dp -> dp.point() == 0.0);
    }

    @Test
    @DisplayName("DAY: 포트에서 반환된 데이터가 해당 날짜의 DataPoint에 매핑되고, 나머지 날짜는 0.0으로 채워진다")
    void day_exp_dataMappedCorrectly() {
        LocalDate today = LocalDate.now(TEST_ZONE);
        LocalDate targetDate = today.minusDays(3);
        double expectedPoint = 150.0;

        Object[] row = new Object[]{USER_ID, Date.valueOf(targetDate), expectedPoint};
        List<Object[]> mockData = Collections.singletonList(row);
        when(userExpHistoryRepositoryPort.findDailyDataByUserIds(any(), any(), any()))
                .thenReturn(mockData);

        AnalyzeMyPeriodData.Result result = service.execute(command(TargetCategory.EXP, PeriodCategory.DAY));

        AnalyzeMyPeriodData.DataPoint matched = result.dataPoints().stream()
                .filter(dp -> dp.date().equals(targetDate))
                .findFirst()
                .orElseThrow();
        assertThat(matched.point()).isEqualTo(expectedPoint);

        long zeroCount = result.dataPoints().stream().filter(dp -> dp.point() == 0.0).count();
        assertThat(zeroCount).isEqualTo(6);
    }

    @Test
    @DisplayName("DAY: dataPoints는 오래된 날짜에서 최신 날짜 순으로 정렬된다")
    void day_dataPoints_areInAscendingDateOrder() {
        LocalDate today = LocalDate.now(TEST_ZONE);

        AnalyzeMyPeriodData.Result result = service.execute(command(TargetCategory.EXP, PeriodCategory.DAY));

        List<LocalDate> dates = result.dataPoints().stream().map(AnalyzeMyPeriodData.DataPoint::date).toList();
        assertThat(dates.get(0)).isEqualTo(today.minusDays(6));
        assertThat(dates.get(6)).isEqualTo(today);
    }

    @Test
    @DisplayName("결과의 category와 period가 커맨드와 일치한다")
    void result_categoryAndPeriodMatchCommand() {
        AnalyzeMyPeriodData.Result result = service.execute(command(TargetCategory.GITHUB, PeriodCategory.MONTH));

        assertThat(result.category()).isEqualTo("GITHUB");
        assertThat(result.period()).isEqualTo("MONTH");
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
                .findDailyDataByUserIds(eq(List.of(USER_ID)), start.capture(), end.capture());

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
                .findDailyContributionsByUserIds(eq(List.of(USER_ID)), start.capture(), end.capture());

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
                .findDailyStudyTimeByUserIds(eq(List.of(USER_ID)), any(), any(), any());
    }

    @Test
    @DisplayName("SEASON: 진행 중인 시즌이면 데이터 포인트가 시즌 시작일부터 오늘까지 생성된다")
    void season_inProgress_dataPointsFromSeasonStartToToday() {
        LocalDate today = LocalDate.now(TEST_ZONE);
        Season season = seasonInProgress();
        when(seasonRepositoryPort.findCurrentSeason()).thenReturn(Optional.of(season));

        AnalyzeMyPeriodData.Result result = service.execute(command(TargetCategory.EXP, PeriodCategory.SEASON));

        int expectedSize = (int) (today.toEpochDay() - season.startDate().toEpochDay() + 1);
        assertThat(result.dataPoints()).hasSize(expectedSize);
    }

    @Test
    @DisplayName("SEASON: 종료된 시즌이면 endDate가 시즌 종료일로 제한된다")
    void season_ended_endDateClampedToSeasonEndDate() {
        Season season = seasonEnded();
        when(seasonRepositoryPort.findCurrentSeason()).thenReturn(Optional.of(season));

        service.execute(command(TargetCategory.EXP, PeriodCategory.SEASON));

        ArgumentCaptor<LocalDate> end = ArgumentCaptor.forClass(LocalDate.class);
        verify(userExpHistoryRepositoryPort)
                .findDailyDataByUserIds(eq(List.of(USER_ID)), any(), end.capture());

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