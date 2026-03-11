package com.process.clash.application.compete.my.service;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.compete.my.data.GetMyGrowthRateData;
import com.process.clash.application.compete.my.data.UserEarnedExp;
import com.process.clash.application.compete.my.exception.exception.badrequest.InvalidDayCategoryException;
import com.process.clash.application.user.userexphistory.port.out.UserExpHistoryRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetMyGrowthRateServiceTest {

    @Mock
    private UserExpHistoryRepositoryPort userExpHistoryRepositoryPort;

    private GetMyGrowthRateService service; //테스트 대상

    @BeforeEach
    void setUp(){
        service = new GetMyGrowthRateService(userExpHistoryRepositoryPort);
    }

    @Test
    @DisplayName("DAY 카테고리: 13개 데이터가 있으면 12개의 차이값 DataPoint를 반환한다")
    void execute_day_returns12DataPoints() {
        // given
        Actor actor = new Actor(1L);
        GetMyGrowthRateData.Command command = new GetMyGrowthRateData.Command(actor, "DAY");

        List<UserEarnedExp> rawData = createDayRawData(100, 120, 110, 110, 110, 160, 170, 110, 500, 320, 160, 10, 160);

        when(userExpHistoryRepositoryPort.findUserDailyEarnedExpByUserIdAndPeriod(
                eq(1L), any(), any(), any()
        )).thenReturn(rawData);

        // when
        GetMyGrowthRateData.Result result = service.execute(command);

        // then
        assertThat(result.dataPoint()).hasSize(12);
        assertThat(result.dataPoint().get(0).rate()).isEqualTo(20L);  // 120 - 100
        assertThat(result.dataPoint().get(1).rate()).isEqualTo(-10L); // 110 - 120
    }

    @Test
    @DisplayName("WEEK 카테고리: 13개 데이터가 있으면 12개의 차이값 DataPoint를 반환한다")
    void execute_week_returns12DataPoints() {
        // given
        Actor actor = new Actor(1L);
        GetMyGrowthRateData.Command command = new GetMyGrowthRateData.Command(actor, "WEEK");

        List<UserEarnedExp> rawData = createWeekRawData(100, 120, 110, 110, 110, 160, 170, 110, 500, 320, 160, 10, 160);

        when(userExpHistoryRepositoryPort.findUserWeeklyEarnedExpByUserIdAndPeriod(
                eq(1L), any(), any(), any()
        )).thenReturn(rawData);

        // when
        GetMyGrowthRateData.Result result = service.execute(command);

        // then
        assertThat(result.dataPoint()).hasSize(12);
        assertThat(result.dataPoint().get(0).rate()).isEqualTo(20L);  // 120 - 100
        assertThat(result.dataPoint().get(1).rate()).isEqualTo(-10L); // 110 - 120
    }

    @Test
    @DisplayName("MONTH 카테고리: 13개 데이터가 있으면 12개의 차이값 DataPoint를 반환한다")
    void execute_month_returns12DataPoints() {
        // given
        Actor actor = new Actor(1L);
        GetMyGrowthRateData.Command command = new GetMyGrowthRateData.Command(actor, "MONTH");

        List<UserEarnedExp> rawData = createMonthRawData(100, 120, 110, 110, 110, 160, 170, 110, 500, 320, 160, 10, 160);

        when(userExpHistoryRepositoryPort.findUserMonthlyEarnedExpByUserIdAndPeriod(
                eq(1L), any(), any(), any()
        )).thenReturn(rawData);

        // when
        GetMyGrowthRateData.Result result = service.execute(command);

        // then
        assertThat(result.dataPoint()).hasSize(12);
        assertThat(result.dataPoint().get(0).rate()).isEqualTo(20L);  // 120 - 100
        assertThat(result.dataPoint().get(1).rate()).isEqualTo(-10L); // 110 - 120
    }

    @Test
    @DisplayName("DB 데이터가 없는 날짜는 0으로 채워 항상 12개 DataPoint를 반환한다")
    void execute_returns12DataPoints_withMissingDatesFilledAsZero() {
        // given
        Actor actor = new Actor(1L);
        GetMyGrowthRateData.Command command = new GetMyGrowthRateData.Command(actor, "DAY");

        when(userExpHistoryRepositoryPort.findUserDailyEarnedExpByUserIdAndPeriod(
                any(), any(), any(), any()
        )).thenReturn(createDayRawData(100));  // 오늘 날짜 1개만

        // when
        GetMyGrowthRateData.Result result = service.execute(command);

        // then - 빈 날짜는 0으로 채워서 항상 12개 반환
        assertThat(result.dataPoint()).hasSize(12);
        assertThat(result.dataPoint().get(0).rate()).isEqualTo(0L);    // 0 - 0
        assertThat(result.dataPoint().get(11).rate()).isEqualTo(100L); // 100 - 0
    }

    @Test
    @DisplayName("잘못된 카테고리면 InvalidDayCategoryException이 발생한다")
    void execute_throwsException_whenCategoryIsInvalid() {
        // given
        Actor actor = new Actor(1L);
        GetMyGrowthRateData.Command command = new GetMyGrowthRateData.Command(actor, "INVALID");

        // when & then
        assertThatThrownBy(() -> service.execute(command))
                .isInstanceOf(InvalidDayCategoryException.class);
    }

    @Test
    @DisplayName("WEEK 카테고리 호출 시 주별 쿼리를 사용한다")
    void execute_week_callsWeeklyRepository() {
        // given
        Actor actor = new Actor(1L);
        GetMyGrowthRateData.Command command = new GetMyGrowthRateData.Command(actor, "WEEK");

        when(userExpHistoryRepositoryPort.findUserWeeklyEarnedExpByUserIdAndPeriod(
                any(), any(), any(), any()
        )).thenReturn(List.of());

        // when
        service.execute(command);

        // then
        verify(userExpHistoryRepositoryPort).findUserWeeklyEarnedExpByUserIdAndPeriod(
                any(), any(), any(), any()
        );
        verify(userExpHistoryRepositoryPort, never())
                .findUserDailyEarnedExpByUserIdAndPeriod(any(), any(), any(), any());
    }

    // ─── helpers ─────────────────────────────────────────────────────────────

    private List<UserEarnedExp> createDayRawData(long... expValues) {
        LocalDate now = LocalDate.now();
        int n = expValues.length;
        List<UserEarnedExp> list = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            list.add(new UserEarnedExp(now.minusDays(n - 1 - i), expValues[i]));
        }
        return list;
    }

    private List<UserEarnedExp> createWeekRawData(long... expValues) {
        LocalDate now = LocalDate.now();
        int n = expValues.length;
        List<UserEarnedExp> list = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            list.add(new UserEarnedExp(now.minusWeeks(n - 1 - i).with(DayOfWeek.MONDAY), expValues[i]));
        }
        return list;
    }

    private List<UserEarnedExp> createMonthRawData(long... expValues) {
        LocalDate now = LocalDate.now();
        int n = expValues.length;
        List<UserEarnedExp> list = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            list.add(new UserEarnedExp(now.minusMonths(n - 1 - i).withDayOfMonth(1), expValues[i]));
        }
        return list;
    }
}
