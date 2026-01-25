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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetMyGrowthRateService implements GetMyGrowthRateUseCase {

    private final UserExpHistoryRepositoryPort userExpHistoryRepositoryPort;
    private static final int MAX_DATA_POINTS = 10;
    private static final int FETCH_COUNT = 11;

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

        return userExpHistoryRepositoryPort.findUserDailyEarnedExpByUserIdAndPeriod(
                id,
                now.minusDays(10),
                now,
                PageRequest.of(0, FETCH_COUNT)
        );
    }

    private List<UserEarnedExp> week(Long id) {

        LocalDate now = LocalDate.now();

        return userExpHistoryRepositoryPort.findUserWeeklyEarnedExpByUserIdAndPeriod(
                id,
                now.minusWeeks(10),
                now,
                PageRequest.of(0, FETCH_COUNT)
        );
    }

    private List<UserEarnedExp> month(Long id) {

        LocalDate now = LocalDate.now();

        return userExpHistoryRepositoryPort.findUserMonthlyEarnedExpByUserIdAndPeriod(
                id,
                now.minusMonths(10),
                now,
                PageRequest.of(0, FETCH_COUNT)
        );
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

            double growthRate = calculateGrowthRate(
                    current.avgEarnedExp(),
                    previous.avgEarnedExp()
            );

            result.add(new DataPoint(current.date(), growthRate));
        }

        return result.stream()
                .limit(MAX_DATA_POINTS)
                .toList();
    }

    private double calculateGrowthRate(Double current, Double previous) {
        if (previous == null || previous == 0.0) {
            return current != null && current > 0 ? 100.0 : 0.0;
        }
        if (current == null) {
            return -100.0;
        }
        return Math.round(((current - previous) / previous) * 100.0 * 100.0) / 100.0; // 소수점 깨지는 것 방지
    }
}