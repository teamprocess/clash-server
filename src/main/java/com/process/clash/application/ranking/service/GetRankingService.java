package com.process.clash.application.ranking.service;

import com.process.clash.application.github.port.out.GitHubDailyStatsQueryPort;
import com.process.clash.application.profile.data.EquippedItemsData;
import com.process.clash.application.profile.service.EquippedItemsAssembler;
import com.process.clash.application.ranking.data.GetRankingData;
import com.process.clash.application.ranking.data.UserRanking;
import com.process.clash.application.ranking.port.in.GetRankingUseCase;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetRankingService implements GetRankingUseCase {

    private final UserExpHistoryRepositoryPort userExpHistoryRepositoryPort;
    private final GitHubDailyStatsQueryPort gitHubDailyStatsQueryPort;
    private final RecordSessionV2RepositoryPort recordSessionRepositoryPort;
    private final SeasonRepositoryPort seasonRepositoryPort;
    private final ZoneId recordZoneId;
    private final RecordProperties recordProperties;
    private final EquippedItemsAssembler equippedItemsAssembler;

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

        return GetRankingData.Result.of(command.category(), command.period(), attachEquippedItems(userRankings));
    }

    private List<UserRanking> gitHub(Long userId, PeriodCategory periodCategory) {

        // GitHub는 00시 기준 (캘린더 날짜 그대로)
        LocalDate today = LocalDate.now(recordZoneId);
        LocalDate startDate;
        LocalDate endDate = today;
        if (periodCategory == PeriodCategory.SEASON) {
            Season season = seasonRepositoryPort.findCurrentSeason()
                    .orElseThrow(SeasonNotFoundException::new);
            startDate = season.startDate();
            endDate = season.endDate().isBefore(today) ? season.endDate() : today;
        } else {
            startDate = switch (periodCategory) {
                case DAY -> today;
                case WEEK -> today.minusWeeks(1);
                case MONTH -> today.minusMonths(1);
                case YEAR -> today.minusYears(1);
                default -> today;
            };
        }

        return gitHubDailyStatsQueryPort.findGitHubRankingByUserIdAndPeriod(userId, startDate, endDate);
    }

    private List<UserRanking> exp(Long userId, PeriodCategory periodCategory) {

        // EXP는 학습시간과 동일하게 경계시간(06:00) 기준
        ZonedDateTime now = ZonedDateTime.now(recordZoneId);
        LocalDate today = now.toLocalDate();
        if (now.getHour() < recordProperties.dayBoundaryHour()) {
            today = today.minusDays(1);
        }

        LocalDate startDate;
        LocalDate endDate = today;
        if (periodCategory == PeriodCategory.SEASON) {
            Season season = seasonRepositoryPort.findCurrentSeason()
                    .orElseThrow(SeasonNotFoundException::new);
            startDate = season.startDate();
            endDate = season.endDate().isBefore(today) ? season.endDate() : today;
        } else {
            startDate = switch (periodCategory) {
                case DAY -> today;
                case WEEK -> today.minusWeeks(1);
                case MONTH -> today.minusMonths(1);
                case YEAR -> today.minusYears(1);
                default -> today;
            };
        }

        return userExpHistoryRepositoryPort.findExpRankingByUserIdAndPeriod(userId, startDate, endDate);
    }

    private List<UserRanking> activeTime(Long userId, PeriodCategory periodCategory) {

        ZonedDateTime now = ZonedDateTime.now(recordZoneId);
        LocalDate today = now.toLocalDate();
        if (now.getHour() < recordProperties.dayBoundaryHour()) {
            today = today.minusDays(1);
        }

        LocalDate startLocalDate;
        if (periodCategory == PeriodCategory.SEASON) {
            Season season = seasonRepositoryPort.findCurrentSeason()
                    .orElseThrow(SeasonNotFoundException::new);
            startLocalDate = season.startDate();
        } else {
            startLocalDate = switch (periodCategory) {
                case DAY -> today;
                case WEEK -> today.minusWeeks(1);
                case MONTH -> today.minusMonths(1);
                case YEAR -> today.minusYears(1);
                default -> today;
            };
        }

        LocalDateTime startDate = startLocalDate.atTime(recordProperties.dayBoundaryHour(), 0);
        LocalDateTime endDate = now.toLocalDateTime();

        return recordSessionRepositoryPort.findStudyTimeRankingByUserIdAndPeriod(userId, startDate, endDate);
    }

    private List<UserRanking> attachEquippedItems(List<UserRanking> rankings) {
        if (rankings == null || rankings.isEmpty()) {
            return rankings;
        }

        Map<Long, EquippedItemsData> equippedItemsByUserId = equippedItemsAssembler.loadByUserIds(
                rankings.stream()
                        .map(UserRanking::userId)
                        .toList()
        );

        return rankings.stream()
                .map(ranking -> new UserRanking(
                        ranking.userId(),
                        ranking.name(),
                        ranking.profileImage(),
                        ranking.isRival(),
                        ranking.linkedId(),
                        ranking.point(),
                        ranking.rankTier(),
                        ranking.expTier(),
                        equippedItemsByUserId.getOrDefault(ranking.userId(), EquippedItemsData.empty())
                ))
                .toList();
    }
}
