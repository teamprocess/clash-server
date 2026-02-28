package com.process.clash.application.ranking.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.github.port.out.GitHubDailyStatsQueryPort;
import com.process.clash.application.ranking.data.GetRankingData;
import com.process.clash.application.ranking.data.UserRanking;
import com.process.clash.application.record.port.out.RecordSessionRepositoryPort;
import com.process.clash.application.user.userexphistory.port.out.UserExpHistoryRepositoryPort;
import com.process.clash.domain.common.enums.PeriodCategory;
import com.process.clash.domain.common.enums.TargetCategory;
import com.process.clash.infrastructure.config.RecordProperties;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetRankingServiceTest {

    private static final ZoneId TEST_ZONE = ZoneId.of("Asia/Seoul");
    private static final int BOUNDARY_HOUR = 6;
    private static final Long USER_ID = 1L;
    private static final RecordProperties RECORD_PROPS = new RecordProperties("Asia/Seoul", BOUNDARY_HOUR);

    @Mock private UserExpHistoryRepositoryPort userExpHistoryRepositoryPort;
    @Mock private GitHubDailyStatsQueryPort gitHubDailyStatsQueryPort;
    @Mock private RecordSessionRepositoryPort recordSessionRepositoryPort;

    private GetRankingService service;

    @BeforeEach
    void setUp() {
        service = new GetRankingService(
            userExpHistoryRepositoryPort,
            gitHubDailyStatsQueryPort,
            recordSessionRepositoryPort,
            TEST_ZONE,
            RECORD_PROPS
        );
    }

    /** boundary-adjusted today (dayBoundaryHour 기준 오늘) */
    private LocalDate boundaryToday() {
        ZonedDateTime now = ZonedDateTime.now(TEST_ZONE);
        LocalDate today = now.toLocalDate();
        return now.getHour() < BOUNDARY_HOUR ? today.minusDays(1) : today;
    }

    private GetRankingData.Command activeTimeCommand(PeriodCategory period) {
        return GetRankingData.Command.from(new Actor(USER_ID), TargetCategory.ACTIVE_TIME, period);
    }

    private GetRankingData.Command expCommand(PeriodCategory period) {
        return GetRankingData.Command.from(new Actor(USER_ID), TargetCategory.EXP, period);
    }

    private GetRankingData.Command gitHubCommand(PeriodCategory period) {
        return GetRankingData.Command.from(new Actor(USER_ID), TargetCategory.GITHUB, period);
    }

    // ===== ACTIVE_TIME =====

    @Test
    @DisplayName("ACTIVE_TIME + DAY: startDate는 boundary-today의 06:00이다")
    void activeTime_day_startDateIsBoundaryTodayAt06() {
        when(recordSessionRepositoryPort.findStudyTimeRankingByUserIdAndPeriod(any(), any(), any()))
            .thenReturn(List.of());

        LocalDate expectedBoundaryToday = boundaryToday();

        service.execute(activeTimeCommand(PeriodCategory.DAY));

        ArgumentCaptor<LocalDateTime> start = ArgumentCaptor.forClass(LocalDateTime.class);
        ArgumentCaptor<LocalDateTime> end   = ArgumentCaptor.forClass(LocalDateTime.class);
        verify(recordSessionRepositoryPort)
            .findStudyTimeRankingByUserIdAndPeriod(eq(USER_ID), start.capture(), end.capture());

        assertThat(start.getValue()).isEqualTo(expectedBoundaryToday.atTime(BOUNDARY_HOUR, 0));
        assertThat(start.getValue()).isBefore(end.getValue());
    }

    @Test
    @DisplayName("ACTIVE_TIME + WEEK: startDate는 boundary-today 기준 1주 전 06:00이다")
    void activeTime_week_startDateIsOneWeekBeforeBoundaryTodayAt06() {
        when(recordSessionRepositoryPort.findStudyTimeRankingByUserIdAndPeriod(any(), any(), any()))
            .thenReturn(List.of());

        LocalDate expectedBoundaryToday = boundaryToday();

        service.execute(activeTimeCommand(PeriodCategory.WEEK));

        ArgumentCaptor<LocalDateTime> start = ArgumentCaptor.forClass(LocalDateTime.class);
        ArgumentCaptor<LocalDateTime> end   = ArgumentCaptor.forClass(LocalDateTime.class);
        verify(recordSessionRepositoryPort)
            .findStudyTimeRankingByUserIdAndPeriod(eq(USER_ID), start.capture(), end.capture());

        assertThat(start.getValue())
            .isEqualTo(expectedBoundaryToday.minusWeeks(1).atTime(BOUNDARY_HOUR, 0));
        assertThat(start.getValue()).isBefore(end.getValue());
    }

    @Test
    @DisplayName("ACTIVE_TIME + MONTH: startDate는 boundary-today 기준 1달 전 06:00이다")
    void activeTime_month_startDateIsOneMonthBeforeBoundaryTodayAt06() {
        when(recordSessionRepositoryPort.findStudyTimeRankingByUserIdAndPeriod(any(), any(), any()))
            .thenReturn(List.of());

        LocalDate expectedBoundaryToday = boundaryToday();

        service.execute(activeTimeCommand(PeriodCategory.MONTH));

        ArgumentCaptor<LocalDateTime> start = ArgumentCaptor.forClass(LocalDateTime.class);
        ArgumentCaptor<LocalDateTime> end   = ArgumentCaptor.forClass(LocalDateTime.class);
        verify(recordSessionRepositoryPort)
            .findStudyTimeRankingByUserIdAndPeriod(eq(USER_ID), start.capture(), end.capture());

        assertThat(start.getValue())
            .isEqualTo(expectedBoundaryToday.minusMonths(1).atTime(BOUNDARY_HOUR, 0));
    }

    @Test
    @DisplayName("ACTIVE_TIME + YEAR: startDate는 boundary-today 기준 1년 전 06:00이다")
    void activeTime_year_startDateIsOneYearBeforeBoundaryTodayAt06() {
        when(recordSessionRepositoryPort.findStudyTimeRankingByUserIdAndPeriod(any(), any(), any()))
            .thenReturn(List.of());

        LocalDate expectedBoundaryToday = boundaryToday();

        service.execute(activeTimeCommand(PeriodCategory.YEAR));

        ArgumentCaptor<LocalDateTime> start = ArgumentCaptor.forClass(LocalDateTime.class);
        ArgumentCaptor<LocalDateTime> end   = ArgumentCaptor.forClass(LocalDateTime.class);
        verify(recordSessionRepositoryPort)
            .findStudyTimeRankingByUserIdAndPeriod(eq(USER_ID), start.capture(), end.capture());

        assertThat(start.getValue())
            .isEqualTo(expectedBoundaryToday.minusYears(1).atTime(BOUNDARY_HOUR, 0));
    }

    @Test
    @DisplayName("ACTIVE_TIME 카테고리는 요청자의 userId로 저장소를 조회한다")
    void activeTime_callsRepositoryWithRequesterId() {
        when(recordSessionRepositoryPort.findStudyTimeRankingByUserIdAndPeriod(any(), any(), any()))
            .thenReturn(List.of());

        service.execute(activeTimeCommand(PeriodCategory.DAY));

        verify(recordSessionRepositoryPort)
            .findStudyTimeRankingByUserIdAndPeriod(eq(USER_ID), any(), any());
    }

    @Test
    @DisplayName("ACTIVE_TIME 카테고리는 저장소에서 받은 랭킹 결과를 그대로 반환한다")
    void activeTime_returnsRankingsFromRepository() {
        List<UserRanking> expected = List.of(
            new UserRanking(2L, "유저B", "", false, "userB", 7200L),
            new UserRanking(3L, "유저C", "", true,  "userC", 3600L)
        );
        when(recordSessionRepositoryPort.findStudyTimeRankingByUserIdAndPeriod(any(), any(), any()))
            .thenReturn(expected);

        GetRankingData.Result result = service.execute(activeTimeCommand(PeriodCategory.DAY));

        assertThat(result.rankings()).isEqualTo(expected);
    }

    // ===== EXP =====

    @Test
    @DisplayName("EXP + DAY: startDate와 endDate 모두 boundary-today이다 (BETWEEN 포함 조회)")
    void exp_day_startAndEndAreBoundaryToday() {
        when(userExpHistoryRepositoryPort.findExpRankingByUserIdAndPeriod(any(), any(), any()))
            .thenReturn(List.of());

        LocalDate expectedBoundaryToday = boundaryToday();

        service.execute(expCommand(PeriodCategory.DAY));

        ArgumentCaptor<LocalDate> start = ArgumentCaptor.forClass(LocalDate.class);
        ArgumentCaptor<LocalDate> end   = ArgumentCaptor.forClass(LocalDate.class);
        verify(userExpHistoryRepositoryPort)
            .findExpRankingByUserIdAndPeriod(eq(USER_ID), start.capture(), end.capture());

        assertThat(start.getValue()).isEqualTo(expectedBoundaryToday);
        assertThat(end.getValue()).isEqualTo(expectedBoundaryToday);
    }

    @Test
    @DisplayName("EXP + WEEK: startDate는 boundary-today 기준 1주 전이고, endDate는 boundary-today이다")
    void exp_week_startIsOneWeekBeforeBoundaryToday() {
        when(userExpHistoryRepositoryPort.findExpRankingByUserIdAndPeriod(any(), any(), any()))
            .thenReturn(List.of());

        LocalDate expectedBoundaryToday = boundaryToday();

        service.execute(expCommand(PeriodCategory.WEEK));

        ArgumentCaptor<LocalDate> start = ArgumentCaptor.forClass(LocalDate.class);
        ArgumentCaptor<LocalDate> end   = ArgumentCaptor.forClass(LocalDate.class);
        verify(userExpHistoryRepositoryPort)
            .findExpRankingByUserIdAndPeriod(eq(USER_ID), start.capture(), end.capture());

        assertThat(start.getValue()).isEqualTo(expectedBoundaryToday.minusWeeks(1));
        assertThat(end.getValue()).isEqualTo(expectedBoundaryToday);
    }

    // ===== GITHUB =====

    @Test
    @DisplayName("GITHUB + DAY: startDate와 endDate 모두 캘린더 기준(00시) 오늘이다")
    void gitHub_day_startAndEndAreCalendarToday() {
        when(gitHubDailyStatsQueryPort.findGitHubRankingByUserIdAndPeriod(any(), any(), any()))
            .thenReturn(List.of());

        LocalDate expectedCalendarToday = LocalDate.now(TEST_ZONE);

        service.execute(gitHubCommand(PeriodCategory.DAY));

        ArgumentCaptor<LocalDate> start = ArgumentCaptor.forClass(LocalDate.class);
        ArgumentCaptor<LocalDate> end   = ArgumentCaptor.forClass(LocalDate.class);
        verify(gitHubDailyStatsQueryPort)
            .findGitHubRankingByUserIdAndPeriod(eq(USER_ID), start.capture(), end.capture());

        assertThat(start.getValue()).isEqualTo(expectedCalendarToday);
        assertThat(end.getValue()).isEqualTo(expectedCalendarToday);
    }

    @Test
    @DisplayName("GITHUB + WEEK: startDate는 캘린더 기준(00시) 오늘에서 1주 전이고, endDate는 오늘이다")
    void gitHub_week_startIsOneWeekBeforeCalendarToday() {
        when(gitHubDailyStatsQueryPort.findGitHubRankingByUserIdAndPeriod(any(), any(), any()))
            .thenReturn(List.of());

        LocalDate expectedCalendarToday = LocalDate.now(TEST_ZONE);

        service.execute(gitHubCommand(PeriodCategory.WEEK));

        ArgumentCaptor<LocalDate> start = ArgumentCaptor.forClass(LocalDate.class);
        ArgumentCaptor<LocalDate> end   = ArgumentCaptor.forClass(LocalDate.class);
        verify(gitHubDailyStatsQueryPort)
            .findGitHubRankingByUserIdAndPeriod(eq(USER_ID), start.capture(), end.capture());

        assertThat(start.getValue()).isEqualTo(expectedCalendarToday.minusWeeks(1));
        assertThat(end.getValue()).isEqualTo(expectedCalendarToday);
    }
}
