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
import java.time.LocalDateTime;
import java.time.ZoneId;
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
    private static final Long USER_ID = 1L;

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
            TEST_ZONE
        );
    }

    private GetRankingData.Command command(PeriodCategory period) {
        return GetRankingData.Command.from(new Actor(USER_ID), TargetCategory.ACTIVE_TIME, period);
    }

    private void captureAndVerify(
        ArgumentCaptor<LocalDateTime> startCaptor,
        ArgumentCaptor<LocalDateTime> endCaptor
    ) {
        verify(recordSessionRepositoryPort)
            .findStudyTimeRankingByUserIdAndPeriod(eq(USER_ID), startCaptor.capture(), endCaptor.capture());
    }

    @Test
    @DisplayName("ACTIVE_TIME + DAY: startDate는 endDate보다 정확히 1일 이전이다")
    void activeTime_day_startDateIsOneDayBeforeEndDate() {
        when(recordSessionRepositoryPort.findStudyTimeRankingByUserIdAndPeriod(any(), any(), any()))
            .thenReturn(List.of());

        service.execute(command(PeriodCategory.DAY));

        ArgumentCaptor<LocalDateTime> start = ArgumentCaptor.forClass(LocalDateTime.class);
        ArgumentCaptor<LocalDateTime> end   = ArgumentCaptor.forClass(LocalDateTime.class);
        captureAndVerify(start, end);

        assertThat(start.getValue()).isEqualTo(end.getValue().minusDays(1));
    }

    @Test
    @DisplayName("ACTIVE_TIME + WEEK: startDate는 endDate보다 정확히 1주 이전이다")
    void activeTime_week_startDateIsOneWeekBeforeEndDate() {
        when(recordSessionRepositoryPort.findStudyTimeRankingByUserIdAndPeriod(any(), any(), any()))
            .thenReturn(List.of());

        service.execute(command(PeriodCategory.WEEK));

        ArgumentCaptor<LocalDateTime> start = ArgumentCaptor.forClass(LocalDateTime.class);
        ArgumentCaptor<LocalDateTime> end   = ArgumentCaptor.forClass(LocalDateTime.class);
        captureAndVerify(start, end);

        assertThat(start.getValue()).isEqualTo(end.getValue().minusWeeks(1));
    }

    @Test
    @DisplayName("ACTIVE_TIME + MONTH: startDate는 endDate보다 정확히 1달 이전이다")
    void activeTime_month_startDateIsOneMonthBeforeEndDate() {
        when(recordSessionRepositoryPort.findStudyTimeRankingByUserIdAndPeriod(any(), any(), any()))
            .thenReturn(List.of());

        service.execute(command(PeriodCategory.MONTH));

        ArgumentCaptor<LocalDateTime> start = ArgumentCaptor.forClass(LocalDateTime.class);
        ArgumentCaptor<LocalDateTime> end   = ArgumentCaptor.forClass(LocalDateTime.class);
        captureAndVerify(start, end);

        assertThat(start.getValue()).isEqualTo(end.getValue().minusMonths(1));
    }

    @Test
    @DisplayName("ACTIVE_TIME + YEAR: startDate는 endDate보다 정확히 1년 이전이다")
    void activeTime_year_startDateIsOneYearBeforeEndDate() {
        when(recordSessionRepositoryPort.findStudyTimeRankingByUserIdAndPeriod(any(), any(), any()))
            .thenReturn(List.of());

        service.execute(command(PeriodCategory.YEAR));

        ArgumentCaptor<LocalDateTime> start = ArgumentCaptor.forClass(LocalDateTime.class);
        ArgumentCaptor<LocalDateTime> end   = ArgumentCaptor.forClass(LocalDateTime.class);
        captureAndVerify(start, end);

        assertThat(start.getValue()).isEqualTo(end.getValue().minusYears(1));
    }

    @Test
    @DisplayName("ACTIVE_TIME 카테고리는 요청자의 userId로 저장소를 조회한다")
    void activeTime_callsRepositoryWithRequesterId() {
        when(recordSessionRepositoryPort.findStudyTimeRankingByUserIdAndPeriod(any(), any(), any()))
            .thenReturn(List.of());

        service.execute(command(PeriodCategory.DAY));

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

        GetRankingData.Result result = service.execute(command(PeriodCategory.DAY));

        assertThat(result.rankings()).isEqualTo(expected);
    }
}
