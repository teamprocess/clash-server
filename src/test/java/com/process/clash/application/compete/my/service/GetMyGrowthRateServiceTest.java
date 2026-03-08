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
        GetMyGrowthRateData.Command command = new GetMyGrowthRateData.Command(actor,"DAY");

        // 13개의 UserEarnedExp 데이터 준비
        List<UserEarnedExp> rawData = createRawData(100, 120, 110, 110, 110, 160, 170, 110, 500, 320, 160, 10, 160);

        when(userExpHistoryRepositoryPort.findUserDailyEarnedExpByUserIdAndPeriod(
                eq(1L), any(), any(), any()
        )).thenReturn(rawData);

        //when
        GetMyGrowthRateData.Result result = service.execute(command);

        // then
        assertThat(result.dataPoint()).hasSize(12);
        assertThat(result.dataPoint().get(0).rate()).isEqualTo(20L);
        assertThat(result.dataPoint().get(1).rate()).isEqualTo(-10L);

    }

    @Test
    @DisplayName("데이터가 1개이면 빈 리스트를 반환한다")
    void execute_returnsEmptyList_whenDataIsOne() {
        // given
        Actor actor = new Actor(1L);
        GetMyGrowthRateData.Command command = new GetMyGrowthRateData.Command(actor, "DAY");

        when(userExpHistoryRepositoryPort.findUserDailyEarnedExpByUserIdAndPeriod(
                any(), any(), any(), any()
        )).thenReturn(createRawData(100));  // 1개

        // when
        GetMyGrowthRateData.Result result = service.execute(command);

        // then
        assertThat(result.dataPoint()).isEmpty();
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




    private List<UserEarnedExp> createRawData(long... expValues) {
        List<UserEarnedExp> list = new ArrayList<>();
        for (int i = 0; i < expValues.length; i++) {
            list.add(new UserEarnedExp(LocalDate.of(2026, 3, i + 1), expValues[i]));
        }
        return list;
    }

}
