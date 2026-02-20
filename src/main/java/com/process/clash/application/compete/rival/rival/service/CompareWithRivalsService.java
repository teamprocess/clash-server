package com.process.clash.application.compete.rival.rival.service;

import com.process.clash.application.compete.rival.rival.data.CompareWithRivalsData;
import com.process.clash.application.compete.rival.rival.data.TotalData;
import com.process.clash.application.compete.rival.rival.exception.exception.notfound.RivalNotFoundException;
import com.process.clash.application.compete.rival.rival.port.in.CompareWithRivalsUseCase;
import com.process.clash.application.compete.rival.rival.port.out.RivalRepositoryPort;
import com.process.clash.application.github.port.out.GitHubDailyStatsQueryPort;
import com.process.clash.application.record.port.out.RecordSessionRepositoryPort;
import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import com.process.clash.application.user.userexphistory.port.out.UserExpHistoryRepositoryPort;
import com.process.clash.domain.common.enums.PeriodCategory;
import com.process.clash.domain.user.user.entity.User;
import com.process.clash.infrastructure.config.RecordProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompareWithRivalsService implements CompareWithRivalsUseCase {

    private final GitHubDailyStatsQueryPort githubDailyStatsQueryPort;
    private final RivalRepositoryPort rivalRepositoryPort;
    private final UserExpHistoryRepositoryPort userExpHistoryRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;
    private final RecordSessionRepositoryPort recordSessionRepositoryPort;
    private final ZoneId recordZoneId;
    private final RecordProperties recordProperties;

    @Override
    public CompareWithRivalsData.Result execute(CompareWithRivalsData.Command command) {

        List<Long> rivalIds = rivalRepositoryPort.findOpponentIdByUserId(command.actor().id());

        if (rivalIds.isEmpty()) {
            throw new RivalNotFoundException();
        }

        rivalIds.add(command.actor().id());

        LocalDate endDate = LocalDate.now(recordZoneId);
        LocalDate startDate = switch (command.period()) {
            case DAY -> endDate.minusDays(10);
            case WEEK -> endDate.minusWeeks(10);
            case MONTH -> endDate.minusMonths(10);
            case SEASON -> null; //TODO: 나중에 처리
            case YEAR -> null; //TODO: 나중에 처리
        };

        List<Object[]> results = switch (command.category()) {
            case GITHUB -> gitHub(command.period(), rivalIds, startDate, endDate);
            case EXP -> exp(command.period(), rivalIds, startDate, endDate);
            case SOLVED_AC -> null; //TODO: 나중에 처리
            case ACTIVE_TIME -> activeTime(command.period(), rivalIds, startDate, endDate);
        };

        Map<Long, User> userMap = userRepositoryPort.findAllByIds(rivalIds)
                .stream()
                .collect(Collectors.toMap(User::id, user -> user));

        Map<Long, List<TotalData.DataPoint>> dataByUser = results.stream()
                .collect(Collectors.groupingBy(
                        row -> ((Number) row[0]).longValue(),
                        Collectors.mapping(
                                row -> new TotalData.DataPoint(
                                        ((Date) row[1]).toLocalDate(),
                                        ((Number) row[2]).doubleValue()
                                ),
                                Collectors.toList()
                        )
                ));

        List<TotalData> totalData = rivalIds.stream()
                .map(userId -> {
                    User user = userMap.get(userId);
                    List<TotalData.DataPoint> dataPoints = dataByUser.getOrDefault(userId, List.of());

                    List<TotalData.DataPoint> limitedDataPoints = dataPoints.stream()
                            .sorted(Comparator.comparing(TotalData.DataPoint::date).reversed())
                            .limit(10)
                            .sorted(Comparator.comparing(TotalData.DataPoint::date)) // 다시 오름차순
                            .toList();

                    return new TotalData(
                            userId,
                            user.name(),
                            limitedDataPoints
                    );
                })
                .toList();

        return CompareWithRivalsData.Result.of(command.category(), command.period(), totalData);
    }

    private List<Object[]> exp(PeriodCategory period, List<Long> rivalIds, LocalDate startDate, LocalDate endDate) {

        return switch (period) {
            case DAY -> userExpHistoryRepositoryPort.findDailyDataByUserIds(rivalIds, startDate, endDate);
            case WEEK -> userExpHistoryRepositoryPort.findWeeklyDataByUserIds(rivalIds, startDate, endDate);
            case MONTH -> userExpHistoryRepositoryPort.findMonthlyDataByUserIds(rivalIds, startDate, endDate);
            case SEASON -> null; //TODO: 나중에 처리
            case YEAR -> null; //TODO: 나중에 처리
        };
    }

    private List<Object[]> gitHub(PeriodCategory period, List<Long> rivalIds, LocalDate startDate, LocalDate endDate) {

        return switch (period) {
            case DAY -> githubDailyStatsQueryPort.findDailyContributionsByUserIds(rivalIds, startDate, endDate);
            case WEEK -> githubDailyStatsQueryPort.findWeeklyContributionsByUserIds(rivalIds, startDate, endDate);
            case MONTH -> githubDailyStatsQueryPort.findMonthlyContributionsByUserIds(rivalIds, startDate, endDate);
            case SEASON -> null; //TODO: 나중에 처리
            case YEAR -> null; //TODO: 나중에 처리
        };
    }

    private List<Object[]> activeTime(PeriodCategory period, List<Long> rivalIds, LocalDate startDate, LocalDate endDate) {

        // study_sessions에서 직접 실시간 계산 (명시적 시간대 사용)
        Instant startDateTime = startDate.atTime(recordProperties.dayBoundaryHour(), 0).atZone(recordZoneId).toInstant();
        Instant endDateTime = ZonedDateTime.now(recordZoneId).toInstant();

        return switch (period) {
            case DAY -> recordSessionRepositoryPort.findDailyStudyTimeByUserIds(rivalIds, startDateTime, endDateTime);
            case WEEK -> recordSessionRepositoryPort.findWeeklyStudyTimeByUserIds(rivalIds, startDateTime, endDateTime);
            case MONTH -> recordSessionRepositoryPort.findMonthlyStudyTimeByUserIds(rivalIds, startDateTime, endDateTime);
            case SEASON -> null; //TODO: 나중에 처리
            case YEAR -> null; //TODO: 나중에 처리
        };
    }
}
