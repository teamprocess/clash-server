package com.process.clash.application.compete.my.service;

import com.process.clash.application.compete.my.data.DataPoint;
import com.process.clash.application.compete.my.data.GetMyGrowthRateData;
import com.process.clash.application.compete.my.exception.exception.badrequest.InvalidDayCategoryException;
import com.process.clash.application.compete.my.port.in.GetMyGrowthRateUseCase;
import com.process.clash.application.user.userexphistory.port.out.UserExpHistoryRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        List<DataPoint> rawDataPoints = switch (command.category()) {
            case "DAY" -> day(id);
            case "WEEK" -> week(id);
            case "MONTH" -> month(id);
            default -> throw new InvalidDayCategoryException();
        };

        List<DataPoint> proceedDataPoints = calculateGrowthRates(rawDataPoints);

        return GetMyGrowthRateData.Result.from(proceedDataPoints);
    }

    private List<DataPoint> day(Long id) {

//        List<DataPoint> rawDataPoints = userExpHistoryRepositoryPort.findMonthlyDataByUserIds()

        return null;
    }

    private List<DataPoint> week(Long id) {

        return null;
    }

    private List<DataPoint> month(Long id) {

        return null;
    }

    private List<DataPoint> calculateGrowthRates(List<DataPoint> rawDataPoints) {
        if (rawDataPoints == null || rawDataPoints.isEmpty()) {
            return Collections.emptyList();
        }

        List<DataPoint> result = new ArrayList<>();

        // 첫 번째 데이터는 비교 대상이 없으므로 성장률 0
        result.add(new DataPoint(
                rawDataPoints.get(0).date(),
                0.0
        ));

        // 나머지 데이터는 이전 데이터와 비교하여 성장률 계산
        for (int i = 1; i < Math.min(FETCH_COUNT, rawDataPoints.size()); i++) {
            DataPoint current = rawDataPoints.get(i);
            DataPoint previous = rawDataPoints.get(i - 1);

            double growthRate = calculateGrowthRate(current.rate(), previous.rate());

            result.add(new DataPoint(
                    current.date(),
                    growthRate
            ));
        }

        // 최대 10개만 반환
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
