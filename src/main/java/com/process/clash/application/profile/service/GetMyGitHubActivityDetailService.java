package com.process.clash.application.profile.service;

import com.process.clash.application.github.exception.exception.notfound.GithubDailyStatsNotFoundException;
import com.process.clash.application.github.port.out.GitHubDailyStatsQueryPort;
import com.process.clash.application.profile.data.GetMyGitHubActivityDetailData;
import com.process.clash.application.profile.port.in.GetMyGitHubActivityDetailUsecase;
import com.process.clash.domain.github.entity.GitHubDailyStats;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetMyGitHubActivityDetailService implements GetMyGitHubActivityDetailUsecase {

    private final GitHubDailyStatsQueryPort gitHubDailyStatsQueryPort;

    @Override
    public GetMyGitHubActivityDetailData.Result execute(GetMyGitHubActivityDetailData.Command command) {
        GitHubDailyStats stats = gitHubDailyStatsQueryPort
                .findByUserIdAndStudyDate(command.actor().id(), command.date())
                .orElseThrow(GithubDailyStatsNotFoundException::new);

        int totalContributions = stats.getTotalContributionCount();
        int level = GitHubContributionLevelCalculator.fromCount(totalContributions);

        return GetMyGitHubActivityDetailData.Result.of(
                stats.studyDate().toString(),
                totalContributions,
                level,
                stats.commitCount(),
                stats.issueCount(),
                stats.prCount(),
                stats.reviewedPrCount(),
                stats.additions(),
                stats.deletions(),
                stats.topCommitRepo()
        );
    }
}
