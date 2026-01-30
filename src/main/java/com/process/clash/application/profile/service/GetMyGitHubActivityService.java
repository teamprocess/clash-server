package com.process.clash.application.profile.service;

import com.process.clash.application.github.port.out.GitHubDailyStatsQueryPort;
import com.process.clash.application.profile.data.GetMyGitHubActivityData;
import com.process.clash.application.profile.exception.exception.badrequest.InvalidPeriodCategoryException;
import com.process.clash.application.profile.policy.ProfilePolicy;
import com.process.clash.application.profile.port.in.GetMyGitHubActivityUsecase;
import com.process.clash.domain.common.enums.PeriodCategory;
import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetMyGitHubActivityService implements GetMyGitHubActivityUsecase {

    private final GitHubDailyStatsQueryPort gitHubDailyStatsQueryPort;
    private final ProfilePolicy profilePolicy;

    @Override
    public GetMyGitHubActivityData.Result execute(GetMyGitHubActivityData.Command command) {
        profilePolicy.validateGithubPeriod(command.period());

        LocalDate endDate = LocalDate.now(ZoneId.of("Asia/Seoul")).plusDays(1);
        LocalDate startDate = resolveStartDate(command.period(), endDate);
        int limit = (int) ChronoUnit.DAYS.between(startDate, endDate);

        List<Object[]> rows = gitHubDailyStatsQueryPort.findDailyContributionsByUserIds(
                List.of(command.actor().id()),
                startDate,
                endDate,
                PageRequest.of(0, limit)
        );

        List<GetMyGitHubActivityData.Contribution> contributions = rows.stream()
                .map(row -> {
                    LocalDate date = ((Date) row[1]).toLocalDate();
                    int count = ((Number) row[2]).intValue();
                    int level = GitHubContributionLevelCalculator.fromCount(count);
                    return GetMyGitHubActivityData.Contribution.of(date.toString(), count, level);
                })
                .toList();

        int totalContributions = contributions.stream()
                .mapToInt(GetMyGitHubActivityData.Contribution::contributionCount)
                .sum();

        return GetMyGitHubActivityData.Result.from(totalContributions, contributions);
    }

    private LocalDate resolveStartDate(PeriodCategory period, LocalDate endDate) {
        return switch (period) {
            case WEEK -> endDate.minusWeeks(1);
            case MONTH -> endDate.minusMonths(1);
            case YEAR -> endDate.minusYears(1);
            default -> throw new InvalidPeriodCategoryException();
        };
    }
}
