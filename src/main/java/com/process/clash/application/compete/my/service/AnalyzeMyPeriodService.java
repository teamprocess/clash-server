package com.process.clash.application.compete.my.service;

import com.process.clash.application.compete.my.data.AnalyzeMyPeriodData;
import com.process.clash.application.compete.my.port.in.AnalyzeMyPeriodUseCase;
import com.process.clash.application.github.port.out.GitHubDailyStatsQueryPort;
import com.process.clash.application.record.v2.port.out.RecordSessionV2RepositoryPort;
import com.process.clash.application.shop.season.exception.exception.notfound.SeasonNotFoundException;
import com.process.clash.application.shop.season.port.out.SeasonRepositoryPort;
import com.process.clash.application.user.userexphistory.port.out.UserExpHistoryRepositoryPort;
import com.process.clash.domain.common.enums.PeriodCategory;
import com.process.clash.domain.shop.season.entity.Season;
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
public class AnalyzeMyPeriodService implements AnalyzeMyPeriodUseCase {

    private final GitHubDailyStatsQueryPort githubDailyStatsQueryPort;
    private final UserExpHistoryRepositoryPort userExpHistoryRepositoryPort;
    private final RecordSessionV2RepositoryPort recordSessionRepositoryPort;
    private final SeasonRepositoryPort seasonRepositoryPort;
    private final ZoneId recordZoneId;
    private final RecordProperties recordProperties;

    @Override
    public AnalyzeMyPeriodData.Result execute(AnalyzeMyPeriodData.Command command) {

        List<Long> userIds = List.of(command.actor().id());

        LocalDate today = LocalDate.now(recordZoneId);
        LocalDate endDate;
        LocalDate startDate;
        List<LocalDate> expectedDates;

        if (command.period() == PeriodCategory.SEASON) {
            Season season = seasonRepositoryPort.findCurrentSeason()
                    .orElseThrow(SeasonNotFoundException::new);
            startDate = season.startDate();
            endDate = season.endDate().isBefore(today) ? season.endDate() : today;
            expectedDates = generateSeasonDates(startDate, endDate);
        } else {
            endDate = today;
            expectedDates = generateExpectedDates(command.period(), endDate);
            startDate = expectedDates.isEmpty() ? null : expectedDates.get(0);
        }

        List<Object[]> results = switch (command.category()) {
            case GITHUB -> gitHub(command.period(), userIds, startDate, endDate);
            case EXP -> exp(command.period(), userIds, startDate, endDate);
            case SOLVED_AC -> null; //TODO: 나중에 처리
            case ACTIVE_TIME -> activeTime(command.period(), userIds, startDate, endDate);
        };

        Map<LocalDate, Double> dateValueMap = results == null ? Map.of() : results.stream()
                .collect(Collectors.toMap(
                        row -> ((Date) row[1]).toLocalDate(),
                        row -> ((Number) row[2]).doubleValue()
                ));

        List<AnalyzeMyPeriodData.DataPoint> dataPoints = expectedDates.stream()
                .map(date -> new AnalyzeMyPeriodData.DataPoint(date, dateValueMap.getOrDefault(date, 0.0)))
                .toList();

        return AnalyzeMyPeriodData.Result.of(command.category(), command.period(), dataPoints);
    }

    private List<LocalDate> generateSeasonDates(LocalDate startDate, LocalDate endDate) {
        return startDate.datesUntil(endDate.plusDays(1)).toList();
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
            case SEASON, YEAR -> List.of();
        };
    }

    private LocalDate toWeekStart(LocalDate date) {
        int weekStartDay = ((date.getDayOfMonth() - 1) / 7) * 7 + 1;
        return date.withDayOfMonth(weekStartDay);
    }

    private LocalDate previousWeekStart(LocalDate weekStart) {
        if (weekStart.getDayOfMonth() == 1) {
            LocalDate prevMonth = weekStart.minusMonths(1);
            int lastWeekStartDay = ((prevMonth.lengthOfMonth() - 1) / 7) * 7 + 1;
            return prevMonth.withDayOfMonth(lastWeekStartDay);
        }
        return weekStart.minusDays(7);
    }

    private List<Object[]> exp(PeriodCategory period, List<Long> userIds, LocalDate startDate, LocalDate endDate) {
        return switch (period) {
            case DAY -> userExpHistoryRepositoryPort.findDailyDataByUserIds(userIds, startDate, endDate);
            case WEEK -> userExpHistoryRepositoryPort.findWeeklyDataByUserIds(userIds, startDate, endDate);
            case MONTH -> userExpHistoryRepositoryPort.findMonthlyDataByUserIds(userIds, startDate, endDate);
            case SEASON -> userExpHistoryRepositoryPort.findDailyDataByUserIds(userIds, startDate, endDate);
            case YEAR -> null;
        };
    }

    private List<Object[]> gitHub(PeriodCategory period, List<Long> userIds, LocalDate startDate, LocalDate endDate) {
        return switch (period) {
            case DAY -> githubDailyStatsQueryPort.findDailyContributionsByUserIds(userIds, startDate, endDate);
            case WEEK -> githubDailyStatsQueryPort.findWeeklyContributionsByUserIds(userIds, startDate, endDate);
            case MONTH -> githubDailyStatsQueryPort.findMonthlyContributionsByUserIds(userIds, startDate, endDate);
            case SEASON -> githubDailyStatsQueryPort.findDailyContributionsByUserIds(userIds, startDate, endDate);
            case YEAR -> null;
        };
    }

    private List<Object[]> activeTime(PeriodCategory period, List<Long> userIds, LocalDate startDate, LocalDate endDate) {
        Instant startDateTime = startDate.atTime(recordProperties.dayBoundaryHour(), 0).atZone(recordZoneId).toInstant();
        Instant endDateTime = ZonedDateTime.now(recordZoneId).toInstant();
        Instant now = Instant.now();

        return switch (period) {
            case DAY -> recordSessionRepositoryPort.findDailyStudyTimeByUserIds(userIds, startDateTime, endDateTime, now);
            case WEEK -> recordSessionRepositoryPort.findWeeklyStudyTimeByUserIds(userIds, startDateTime, endDateTime, now);
            case MONTH -> recordSessionRepositoryPort.findMonthlyStudyTimeByUserIds(userIds, startDateTime, endDateTime, now);
            case SEASON -> recordSessionRepositoryPort.findDailyStudyTimeByUserIds(userIds, startDateTime, endDateTime, now);
            case YEAR -> null;
        };
    }
}