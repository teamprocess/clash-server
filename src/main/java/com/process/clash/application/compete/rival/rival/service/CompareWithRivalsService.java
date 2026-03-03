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
import com.process.clash.infrastructure.config.record.RecordProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
        List<LocalDate> expectedDates = generateExpectedDates(command.period(), endDate);
        LocalDate startDate = expectedDates.isEmpty() ? null : expectedDates.get(0);

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

                    Map<LocalDate, Double> dateValueMap = dataPoints.stream()
                            .collect(Collectors.toMap(TotalData.DataPoint::date, TotalData.DataPoint::point));

                    List<TotalData.DataPoint> filledDataPoints = expectedDates.stream()
                            .map(date -> new TotalData.DataPoint(date, dateValueMap.getOrDefault(date, 0.0)))
                            .toList();

                    return new TotalData(
                            userId,
                            user.name(),
                            filledDataPoints
                    );
                })
                .toList();

        return CompareWithRivalsData.Result.of(command.category(), command.period(), totalData);
    }

    private List<LocalDate> generateExpectedDates(PeriodCategory period, LocalDate endDate) {
        return switch (period) {
            case DAY -> IntStream.rangeClosed(0, 6)
                    .mapToObj(i -> endDate.minusDays(6 - i))
                    .toList();
            case WEEK -> {
                LocalDate[] dates = new LocalDate[7];
                LocalDate ws = toWeekStart(endDate);
                for (int i = 6; i >= 0; i--) {
                    dates[i] = ws;
                    ws = previousWeekStart(ws);
                }
                yield List.of(dates);
            }
            case MONTH -> {
                LocalDate monthStart = endDate.withDayOfMonth(1);
                yield IntStream.rangeClosed(0, 6)
                        .mapToObj(i -> monthStart.minusMonths(6 - i))
                        .toList();
            }
            case SEASON, YEAR -> List.of(); //TODO: 나중에 처리
        };
    }

    // 해당 날짜가 속한 주의 시작일 반환 (1~7일=1일, 8~14일=8일, 15~21일=15일, 22~끝=22일)
    private LocalDate toWeekStart(LocalDate date) {
        int weekStartDay = ((date.getDayOfMonth() - 1) / 7) * 7 + 1;
        return date.withDayOfMonth(weekStartDay);
    }

    // 이전 주의 시작일 반환
    private LocalDate previousWeekStart(LocalDate weekStart) {
        if (weekStart.getDayOfMonth() == 1) {
            LocalDate prevMonth = weekStart.minusMonths(1);
            int lastWeekStartDay = ((prevMonth.lengthOfMonth() - 1) / 7) * 7 + 1;
            return prevMonth.withDayOfMonth(lastWeekStartDay);
        }
        return weekStart.minusDays(7);
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
        Instant now = Instant.now();
        Instant endDateTime = ZonedDateTime.now(recordZoneId).toInstant();

        return switch (period) {
            case DAY -> recordSessionRepositoryPort.findDailyStudyTimeByUserIds(rivalIds, startDateTime, endDateTime, now);
            case WEEK -> recordSessionRepositoryPort.findWeeklyStudyTimeByUserIds(rivalIds, startDateTime, endDateTime, now);
            case MONTH -> recordSessionRepositoryPort.findMonthlyStudyTimeByUserIds(rivalIds, startDateTime, endDateTime, now);
            case SEASON -> null; //TODO: 나중에 처리
            case YEAR -> null; //TODO: 나중에 처리
        };
    }
}
