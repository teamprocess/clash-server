package com.process.clash.application.compete.my.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.compete.my.data.CompareMyActivityData;
import com.process.clash.application.github.port.out.GitHubDailyStatsQueryPort;
import com.process.clash.application.record.port.out.RecordSessionRepositoryPort;
import com.process.clash.application.user.userexphistory.port.out.UserExpHistoryRepositoryPort;
import com.process.clash.application.user.userstudytime.port.out.UserStudyTimeRepositoryPort;
import com.process.clash.infrastructure.config.record.RecordProperties;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CompareMyActivityServiceTest {

    private static final ZoneId TEST_ZONE = ZoneId.of("Asia/Seoul");
    private static final int BOUNDARY_HOUR = 6;
    private static final Long USER_ID = 1L;
    private static final RecordProperties RECORD_PROPS = new RecordProperties("Asia/Seoul", BOUNDARY_HOUR);

    @Mock private GitHubDailyStatsQueryPort gitHubDailyStatsQueryPort;
    @Mock private UserStudyTimeRepositoryPort userStudyTimeRepositoryPort;
    @Mock private UserExpHistoryRepositoryPort userExpHistoryRepositoryPort;
    @Mock private RecordSessionRepositoryPort recordSessionRepositoryPort;

    private CompareMyActivityService service;

    @BeforeEach
    void setUp() {
        service = new CompareMyActivityService(
            gitHubDailyStatsQueryPort,
            userStudyTimeRepositoryPort,
            userExpHistoryRepositoryPort,
            recordSessionRepositoryPort,
            RECORD_PROPS,
            TEST_ZONE
        );
        lenient().when(userExpHistoryRepositoryPort.findAverageExpByUserIdAndCategoryAndPeriod(any(), any(), any())).thenReturn(0.0);
        lenient().when(userStudyTimeRepositoryPort.findAverageStudyTimeByUserIdAndPeriod(any(), any(), any())).thenReturn(0.0);
        lenient().when(recordSessionRepositoryPort.getTotalStudyTimeInSeconds(any(), any(), any())).thenReturn(0L);
        lenient().when(gitHubDailyStatsQueryPort.findAverageContributionByUserIdAndPeriod(any(), any(), any())).thenReturn(0.0);
        lenient().when(gitHubDailyStatsQueryPort.findTotalCommitCountByUserIdAndPeriod(any(), any(), any())).thenReturn(0.0);
    }

    private CompareMyActivityData.Command command(String category) {
        return CompareMyActivityData.Command.of(new Actor(USER_ID), category);
    }

    /** dayBoundaryHour 기준으로 조정된 오늘 날짜 */
    private LocalDate boundaryToday() {
        ZonedDateTime now = ZonedDateTime.now(TEST_ZONE);
        LocalDate today = now.toLocalDate();
        return now.getHour() < BOUNDARY_HOUR ? today.minusDays(1) : today;
    }

    // ===== TODAY =====

    @Test
    @DisplayName("TODAY: EXP 조회는 boundary 기준 오늘([boundaryToday, boundaryToday+1)) 날짜를 사용한다")
    void today_expUsesBoundaryDate() {
        LocalDate expectedBoundaryToday = boundaryToday();

        service.execute(command("TODAY"));

        ArgumentCaptor<LocalDate> start = ArgumentCaptor.forClass(LocalDate.class);
        ArgumentCaptor<LocalDate> end   = ArgumentCaptor.forClass(LocalDate.class);
        verify(userExpHistoryRepositoryPort)
            .findAverageExpByUserIdAndCategoryAndPeriod(eq(USER_ID), start.capture(), end.capture());

        assertThat(start.getValue()).isEqualTo(expectedBoundaryToday);
        assertThat(end.getValue()).isEqualTo(expectedBoundaryToday.plusDays(1));
    }

    @Test
    @DisplayName("TODAY: GitHub 조회는 캘린더 기준 오늘([calendarToday, calendarToday+1)) 날짜를 사용한다")
    void today_gitHubUsesCalendarDate() {
        LocalDate expectedCalendarToday = LocalDate.now(TEST_ZONE);

        service.execute(command("TODAY"));

        ArgumentCaptor<LocalDate> start = ArgumentCaptor.forClass(LocalDate.class);
        ArgumentCaptor<LocalDate> end   = ArgumentCaptor.forClass(LocalDate.class);
        verify(gitHubDailyStatsQueryPort)
            .findAverageContributionByUserIdAndPeriod(eq(USER_ID), start.capture(), end.capture());

        assertThat(start.getValue()).isEqualTo(expectedCalendarToday);
        assertThat(end.getValue()).isEqualTo(expectedCalendarToday.plusDays(1));
    }

    // ===== YESTERDAY =====

    @Test
    @DisplayName("YESTERDAY: EXP 조회는 boundary 기준 어제([boundaryToday-1, boundaryToday)) 날짜를 사용한다")
    void yesterday_expUsesOneDayBeforeBoundaryDate() {
        LocalDate expectedBoundaryToday = boundaryToday();

        service.execute(command("YESTERDAY"));

        ArgumentCaptor<LocalDate> start = ArgumentCaptor.forClass(LocalDate.class);
        ArgumentCaptor<LocalDate> end   = ArgumentCaptor.forClass(LocalDate.class);
        verify(userExpHistoryRepositoryPort)
            .findAverageExpByUserIdAndCategoryAndPeriod(eq(USER_ID), start.capture(), end.capture());

        assertThat(start.getValue()).isEqualTo(expectedBoundaryToday.minusDays(1));
        assertThat(end.getValue()).isEqualTo(expectedBoundaryToday);
    }

    @Test
    @DisplayName("YESTERDAY: GitHub 조회는 캘린더 기준 어제([calendarToday-1, calendarToday)) 날짜를 사용한다")
    void yesterday_gitHubUsesOneDayBeforeCalendarDate() {
        LocalDate expectedCalendarToday = LocalDate.now(TEST_ZONE);

        service.execute(command("YESTERDAY"));

        ArgumentCaptor<LocalDate> start = ArgumentCaptor.forClass(LocalDate.class);
        ArgumentCaptor<LocalDate> end   = ArgumentCaptor.forClass(LocalDate.class);
        verify(gitHubDailyStatsQueryPort)
            .findAverageContributionByUserIdAndPeriod(eq(USER_ID), start.capture(), end.capture());

        assertThat(start.getValue()).isEqualTo(expectedCalendarToday.minusDays(1));
        assertThat(end.getValue()).isEqualTo(expectedCalendarToday);
    }

    // ===== LAST_WEEK =====

    @Test
    @DisplayName("LAST_WEEK: EXP 조회는 boundary 기준 1주 전 날짜 범위([boundaryToday-7, boundaryToday))를 사용한다")
    void lastWeek_expUsesOneWeekBeforeBoundaryDate() {
        LocalDate expectedBoundaryToday = boundaryToday();

        service.execute(command("LAST_WEEK"));

        ArgumentCaptor<LocalDate> start = ArgumentCaptor.forClass(LocalDate.class);
        ArgumentCaptor<LocalDate> end   = ArgumentCaptor.forClass(LocalDate.class);
        verify(userExpHistoryRepositoryPort)
            .findAverageExpByUserIdAndCategoryAndPeriod(eq(USER_ID), start.capture(), end.capture());

        assertThat(start.getValue()).isEqualTo(expectedBoundaryToday.minusWeeks(1));
        assertThat(end.getValue()).isEqualTo(expectedBoundaryToday);
    }

    @Test
    @DisplayName("LAST_WEEK: GitHub 조회는 캘린더 기준 1주 전 날짜 범위([calendarToday-7, calendarToday))를 사용한다")
    void lastWeek_gitHubUsesOneWeekBeforeCalendarDate() {
        LocalDate expectedCalendarToday = LocalDate.now(TEST_ZONE);

        service.execute(command("LAST_WEEK"));

        ArgumentCaptor<LocalDate> start = ArgumentCaptor.forClass(LocalDate.class);
        ArgumentCaptor<LocalDate> end   = ArgumentCaptor.forClass(LocalDate.class);
        verify(gitHubDailyStatsQueryPort)
            .findAverageContributionByUserIdAndPeriod(eq(USER_ID), start.capture(), end.capture());

        assertThat(start.getValue()).isEqualTo(expectedCalendarToday.minusWeeks(1));
        assertThat(end.getValue()).isEqualTo(expectedCalendarToday);
    }

    // ===== LAST_MONTH =====

    @Test
    @DisplayName("LAST_MONTH: EXP 조회는 boundary 기준 1달 전 날짜 범위([boundaryToday-1month, boundaryToday))를 사용한다")
    void lastMonth_expUsesOneMonthBeforeBoundaryDate() {
        LocalDate expectedBoundaryToday = boundaryToday();

        service.execute(command("LAST_MONTH"));

        ArgumentCaptor<LocalDate> start = ArgumentCaptor.forClass(LocalDate.class);
        ArgumentCaptor<LocalDate> end   = ArgumentCaptor.forClass(LocalDate.class);
        verify(userExpHistoryRepositoryPort)
            .findAverageExpByUserIdAndCategoryAndPeriod(eq(USER_ID), start.capture(), end.capture());

        assertThat(start.getValue()).isEqualTo(expectedBoundaryToday.minusMonths(1));
        assertThat(end.getValue()).isEqualTo(expectedBoundaryToday);
    }

    @Test
    @DisplayName("LAST_MONTH: GitHub 조회는 캘린더 기준 1달 전 날짜 범위([calendarToday-1month, calendarToday))를 사용한다")
    void lastMonth_gitHubUsesOneMonthBeforeCalendarDate() {
        LocalDate expectedCalendarToday = LocalDate.now(TEST_ZONE);

        service.execute(command("LAST_MONTH"));

        ArgumentCaptor<LocalDate> start = ArgumentCaptor.forClass(LocalDate.class);
        ArgumentCaptor<LocalDate> end   = ArgumentCaptor.forClass(LocalDate.class);
        verify(gitHubDailyStatsQueryPort)
            .findAverageContributionByUserIdAndPeriod(eq(USER_ID), start.capture(), end.capture());

        assertThat(start.getValue()).isEqualTo(expectedCalendarToday.minusMonths(1));
        assertThat(end.getValue()).isEqualTo(expectedCalendarToday);
    }
}
