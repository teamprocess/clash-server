package com.process.clash.application.compete.my.service;

import com.process.clash.application.compete.my.data.DataPoint;
import com.process.clash.application.compete.my.data.GetMyGrowthRateData;
import com.process.clash.application.compete.my.data.UserEarnedExp;
import com.process.clash.application.compete.my.exception.exception.badrequest.InvalidDayCategoryException;
import com.process.clash.application.compete.my.port.in.GetMyGrowthRateUseCase;
import com.process.clash.application.user.userexphistory.port.out.UserExpHistoryRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetMyGrowthRateService implements GetMyGrowthRateUseCase {

    private final UserExpHistoryRepositoryPort userExpHistoryRepositoryPort;
    private static final int GROWTH_RATE_PERIOD = 12;  // 보여줄 구간 수
    private static final int FETCH_COUNT = GROWTH_RATE_PERIOD + 1;  // DB 페이지 사이즈

    @Override
    public GetMyGrowthRateData.Result execute(GetMyGrowthRateData.Command command) {

        Long id = command.actor().id();

        List<UserEarnedExp> rawDataPoints = switch (command.category()) {
            case "DAY" -> day(id);
            case "WEEK" -> week(id);
            case "MONTH" -> month(id);
            default -> throw new InvalidDayCategoryException();
        };

        List<DataPoint> proceedDataPoints = calculateGrowthRates(rawDataPoints);

        return GetMyGrowthRateData.Result.from(proceedDataPoints);
    }

    private List<UserEarnedExp> day(Long id) {

        LocalDate now = LocalDate.now();

        List<UserEarnedExp> rawData = userExpHistoryRepositoryPort.findUserDailyEarnedExpByUserIdAndPeriod(
                id,
                now.minusDays(GROWTH_RATE_PERIOD),
                now,
                PageRequest.of(0, FETCH_COUNT)
        );

        List<LocalDate> allDates = Stream.iterate(now.minusDays(GROWTH_RATE_PERIOD), date -> date.plusDays(1))
                .limit(GROWTH_RATE_PERIOD + 1)
                .toList();

        return fillMissingDates(rawData, allDates);
    }

    private List<UserEarnedExp> week(Long id) {

        LocalDate now = LocalDate.now();

        List<UserEarnedExp> rawData = userExpHistoryRepositoryPort.findUserWeeklyEarnedExpByUserIdAndPeriod(
                id,
                now.minusWeeks(GROWTH_RATE_PERIOD),
                now,
                PageRequest.of(0, FETCH_COUNT)
        );

        List<LocalDate> allDates = Stream.iterate(now.minusWeeks(GROWTH_RATE_PERIOD).with(DayOfWeek.MONDAY), date -> date.plusWeeks(1))
                .limit(GROWTH_RATE_PERIOD + 1)
                .toList();

        return fillMissingDates(rawData, allDates);
    }

    private List<UserEarnedExp> month(Long id) {

        LocalDate now = LocalDate.now();

        List<UserEarnedExp> rawData = userExpHistoryRepositoryPort.findUserMonthlyEarnedExpByUserIdAndPeriod(
                id,
                now.minusMonths(GROWTH_RATE_PERIOD),
                now,
                PageRequest.of(0, FETCH_COUNT)
        );

        List<LocalDate> allDates = Stream.iterate(now.minusMonths(GROWTH_RATE_PERIOD).withDayOfMonth(1), date -> date.plusMonths(1))
                .limit(GROWTH_RATE_PERIOD + 1)
                .toList();

        return fillMissingDates(rawData, allDates);
    }

    private List<UserEarnedExp> fillMissingDates(List<UserEarnedExp> rawData, List<LocalDate> allDates) {

        Map<LocalDate, Long> dateToExp = rawData.stream()
                .collect(Collectors.toMap(UserEarnedExp::date, UserEarnedExp::sumEarnedExp));

        return allDates.stream()
                .map(date -> new UserEarnedExp(date, dateToExp.getOrDefault(date, 0L)))
                .toList();
    }

    private List<DataPoint> calculateGrowthRates(List<UserEarnedExp> rawDataPoints) {
        if (rawDataPoints == null || rawDataPoints.size() < 2) {
            return Collections.emptyList();
        }

        List<DataPoint> result = new ArrayList<>();

        // 두 번째 데이터부터 시작 (첫 번째는 비교 기준으로만 사용)
        for (int i = 1; i < rawDataPoints.size(); i++) {
            UserEarnedExp current = rawDataPoints.get(i);
            UserEarnedExp previous = rawDataPoints.get(i - 1);

            Long growthRate = calculateGrowthRate(
                    current.sumEarnedExp(),
                    previous.sumEarnedExp()
            );

            result.add(new DataPoint(current.date(), growthRate));
        }

        return result.stream()
                .limit(GROWTH_RATE_PERIOD)
                .toList();
    }

    private Long calculateGrowthRate(Long current, Long previous) {
        if (current == null) current = 0L;
        if (previous == null) previous = 0L;

        return current - previous;
    }
}
