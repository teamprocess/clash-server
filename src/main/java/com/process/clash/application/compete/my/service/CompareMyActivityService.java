package com.process.clash.application.compete.my.service;

import com.process.clash.application.compete.my.data.CompareMyActivityData;
import com.process.clash.application.compete.my.exception.exception.badrequest.InvalidDayCategoryException;
import com.process.clash.application.compete.my.port.in.CompareMyActivityUseCase;
import com.process.clash.application.github.port.out.GitHubDailyStatsQueryPort;
import com.process.clash.application.record.port.out.RecordSessionRepositoryPort;
import com.process.clash.application.user.userexphistory.port.out.UserExpHistoryRepositoryPort;
import com.process.clash.application.user.userstudytime.port.out.UserStudyTimeRepositoryPort;
import com.process.clash.infrastructure.config.record.RecordProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompareMyActivityService implements CompareMyActivityUseCase {

    private final GitHubDailyStatsQueryPort gitHubDailyStatsQueryPort;
    private final UserStudyTimeRepositoryPort userStudyTimeRepositoryPort;
    private final UserExpHistoryRepositoryPort userExpHistoryRepositoryPort;
    private final RecordSessionRepositoryPort recordSessionRepositoryPort;
    private final RecordProperties recordProperties;
    private final ZoneId recordZoneId;

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
        LocalDate calendarToday = LocalDate.now(recordZoneId);        // GitHub: 00시 기준
        LocalDate boundaryToday = boundaryToday();                     // EXP/학습: 경계시간 기준

        LocalDateTime startOfDay = boundaryToday.atTime(recordProperties.dayBoundaryHour(), 0, 0);
        LocalDateTime endOfDay = boundaryToday.plusDays(1).atTime(recordProperties.dayBoundaryHour(), 0, 0);

        Double earnedExp = getValueOrDefault(
                userExpHistoryRepositoryPort.findAverageExpByUserIdAndCategoryAndPeriod(id, boundaryToday, boundaryToday.plusDays(1))
        );

        Long todayActiveTime = recordSessionRepositoryPort.getTotalStudyTimeInSeconds(id, startOfDay, endOfDay);
        Double studyTime = todayActiveTime != null ? todayActiveTime.doubleValue() : 0.0;

        Double gitHubAttribution = getValueOrDefault(
                gitHubDailyStatsQueryPort.findAverageContributionByUserIdAndPeriod(id, calendarToday, calendarToday.plusDays(1))
        );

        Double commitCount = gitHubDailyStatsQueryPort.findTotalCommitCountByUserIdAndPeriod(id, calendarToday, calendarToday.plusDays(1));

        return List.of(earnedExp, studyTime, gitHubAttribution, commitCount);
    }

    private List<Double> yesterday(Long id) {
        LocalDate expStart = boundaryToday().minusDays(1);
        LocalDate gitHubStart = LocalDate.now(recordZoneId).minusDays(1);
        return getActivityData(id, expStart, expStart.plusDays(1), gitHubStart, gitHubStart.plusDays(1));
    }

    private List<Double> lastWeek(Long id) {
        LocalDate bToday = boundaryToday();
        LocalDate cToday = LocalDate.now(recordZoneId);
        return getActivityData(id, bToday.minusWeeks(1), bToday, cToday.minusWeeks(1), cToday);
    }

    private List<Double> lastMonth(Long id) {
        LocalDate bToday = boundaryToday();
        LocalDate cToday = LocalDate.now(recordZoneId);
        return getActivityData(id, bToday.minusMonths(1), bToday, cToday.minusMonths(1), cToday);
    }

    private List<Double> getActivityData(Long id, LocalDate expStart, LocalDate expEnd, LocalDate gitHubStart, LocalDate gitHubEnd) {
        Double earnedExp = getValueOrDefault(
                userExpHistoryRepositoryPort.findAverageExpByUserIdAndCategoryAndPeriod(id, expStart, expEnd)
        );

        Double studyTime = getValueOrDefault(
                userStudyTimeRepositoryPort.findAverageStudyTimeByUserIdAndPeriod(id, expStart, expEnd)
        );

        Double gitHubAttribution = getValueOrDefault(
                gitHubDailyStatsQueryPort.findAverageContributionByUserIdAndPeriod(id, gitHubStart, gitHubEnd)
        );

        Double commitCount = gitHubDailyStatsQueryPort.findTotalCommitCountByUserIdAndPeriod(id, gitHubStart, gitHubEnd);

        return List.of(earnedExp, studyTime, gitHubAttribution, commitCount);
    }

    private LocalDate boundaryToday() {
        ZonedDateTime nowZoned = ZonedDateTime.now(recordZoneId);
        LocalDate today = nowZoned.toLocalDate();
        if (nowZoned.getHour() < recordProperties.dayBoundaryHour()) {
            today = today.minusDays(1);
        }
        return today;
    }

    private Double getValueOrDefault(Double value) {
        return value != null ? value : 0.0;
    }
}