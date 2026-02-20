package com.process.clash.application.ranking.service;

import com.process.clash.application.github.port.out.GitHubDailyStatsQueryPort;
import com.process.clash.application.ranking.data.GetRankingData;
import com.process.clash.application.ranking.data.UserRanking;
import com.process.clash.application.ranking.port.in.GetRankingUseCase;
import com.process.clash.application.record.port.out.StudySessionRepositoryPort;
import com.process.clash.application.user.userexphistory.port.out.UserExpHistoryRepositoryPort;
import com.process.clash.domain.common.enums.PeriodCategory;
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
public class GetRankingService implements GetRankingUseCase {

    private final UserExpHistoryRepositoryPort userExpHistoryRepositoryPort;
    private final GitHubDailyStatsQueryPort gitHubDailyStatsQueryPort;
    private final StudySessionRepositoryPort studySessionRepositoryPort;
    private final ZoneId recordZoneId;

    @Override
    public GetRankingData.Result execute(GetRankingData.Command command) {
        Long userId = command.actor().id();
        PeriodCategory periodCategory = command.period();

        List<UserRanking> userRankings = switch (command.category()) {
            case GITHUB -> gitHub(userId, periodCategory);
            case EXP -> exp(userId, periodCategory);
            case SOLVED_AC -> null; //TODO: 나중에 구현
            case ACTIVE_TIME -> activeTime(userId, periodCategory);
        };

        return GetRankingData.Result.of(command.category(), command.period(), userRankings);
    }

    private List<UserRanking> gitHub(Long userId, PeriodCategory periodCategory) {

        LocalDate endDate = LocalDate.now();
        LocalDate startDate = switch (periodCategory) {
            case DAY -> endDate.minusDays(1);
            case WEEK -> endDate.minusWeeks(1);
            case MONTH -> endDate.minusMonths(1);
            case SEASON -> null; //TODO: 나중에 구현
            case YEAR -> endDate.minusYears(1);
        };

        return gitHubDailyStatsQueryPort.findGitHubRankingByUserIdAndPeriod(userId, startDate, endDate);
    }

    private List<UserRanking> exp(Long userId, PeriodCategory periodCategory) {

        LocalDate endDate = LocalDate.now();
        LocalDate startDate = switch (periodCategory) {
            case DAY -> endDate.minusDays(1);
            case WEEK -> endDate.minusWeeks(1);
            case MONTH -> endDate.minusMonths(1);
            case SEASON -> null; //TODO: 나중에 구현
            case YEAR -> endDate.minusYears(1);
        };

        return userExpHistoryRepositoryPort.findExpRankingByUserIdAndPeriod(userId, startDate, endDate);
    }

    private List<UserRanking> activeTime(Long userId, PeriodCategory periodCategory) {

        LocalDateTime endDate = ZonedDateTime.now(recordZoneId).toLocalDateTime();
        LocalDateTime startDate = switch (periodCategory) {
            case DAY -> endDate.minusDays(1);
            case WEEK -> endDate.minusWeeks(1);
            case MONTH -> endDate.minusMonths(1);
            case SEASON -> null; //TODO: 나중에 구현
            case YEAR -> endDate.minusYears(1);
        };

        return studySessionRepositoryPort.findStudyTimeRankingByUserIdAndPeriod(userId, startDate, endDate);
    }
}