package com.process.clash.application.compete.my.service;

import com.process.clash.application.compete.my.data.CompareMyActivityData;
import com.process.clash.application.compete.my.exception.exception.badrequest.InvalidDayCategoryException;
import com.process.clash.application.compete.my.port.in.CompareMyActivityUseCase;
import com.process.clash.application.github.port.out.GitHubDailyStatsQueryPort;
import com.process.clash.application.record.port.out.StudySessionRepositoryPort;
import com.process.clash.application.user.userexphistory.port.out.UserExpHistoryRepositoryPort;
import com.process.clash.application.user.userstudytime.port.out.UserStudyTimeRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompareMyActivityService implements CompareMyActivityUseCase {

    private final GitHubDailyStatsQueryPort gitHubDailyStatsQueryPort;
    private final UserStudyTimeRepositoryPort userStudyTimeRepositoryPort;
    private final UserExpHistoryRepositoryPort userExpHistoryRepositoryPort;
    private final StudySessionRepositoryPort studySessionRepositoryPort;

    @Override
    public CompareMyActivityData.Result execute(CompareMyActivityData.Command command) {

        Long id = command.actor().id();

        List<Double> result = switch (command.category()) {
            case "TODAY" -> today(id);
            case "YESTERDAY" -> yesterday(id);
            case "LAST_WEEK" -> lastWeek(id);
            case "LAST_MONTH" -> lastMonth(id);
            default -> throw new InvalidDayCategoryException();
        };

        return CompareMyActivityData.Result.from(result);
    }

    private List<Double> today(Long id) {
        LocalDate now = LocalDate.now();
        LocalDateTime startOfDay = now.atTime(6, 0, 0);
        LocalDateTime endOfDay = now.plusDays(1).atTime(6, 0, 0);

        Double earnedExp = getValueOrDefault(
                userExpHistoryRepositoryPort.findAverageExpByUserIdAndCategoryAndPeriod(id, now, now.plusDays(1))
        );

        Long todayActiveTime = studySessionRepositoryPort.getTotalStudyTimeInSeconds(id, startOfDay, endOfDay);
        Double studyTime = todayActiveTime != null ? todayActiveTime.doubleValue() : 0.0;

        Double gitHubAttribution = getValueOrDefault(
                gitHubDailyStatsQueryPort.findAverageContributionByUserIdAndPeriod(id, now, now.plusDays(1))
        );

        return List.of(earnedExp, studyTime, gitHubAttribution);
    }

    private List<Double> yesterday(Long id) {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        return getActivityData(id, yesterday, yesterday.plusDays(1));
    }

    private List<Double> lastWeek(Long id) {
        LocalDate lastWeek = LocalDate.now().minusWeeks(1);
        return getActivityData(id, lastWeek, lastWeek.plusWeeks(1));
    }

    private List<Double> lastMonth(Long id) {
        LocalDate lastMonth = LocalDate.now().minusMonths(1);
        return getActivityData(id, lastMonth, lastMonth.plusMonths(1));
    }

    private List<Double> getActivityData(Long id, LocalDate startDate, LocalDate endDate) {
        Double earnedExp = getValueOrDefault(
                userExpHistoryRepositoryPort.findAverageExpByUserIdAndCategoryAndPeriod(id, startDate, endDate)
        );

        Double studyTime = getValueOrDefault(
                userStudyTimeRepositoryPort.findAverageStudyTimeByUserIdAndPeriod(id, startDate, endDate)
        );

        Double gitHubAttribution = getValueOrDefault(
                gitHubDailyStatsQueryPort.findAverageContributionByUserIdAndPeriod(id, startDate, endDate)
        );

        return List.of(earnedExp, studyTime, gitHubAttribution);
    }

    private Double getValueOrDefault(Double value) {
        return value != null ? value : 0.0;
    }
}