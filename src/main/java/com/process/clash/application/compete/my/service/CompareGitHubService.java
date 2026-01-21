package com.process.clash.application.compete.my.service;

import com.process.clash.application.compete.my.data.CompareGitHubData;
import com.process.clash.application.compete.my.port.in.CompareGitHubUseCase;
import com.process.clash.application.github.exception.exception.notfound.GithubDailyStatsNotFoundException;
import com.process.clash.application.github.port.out.GitHubDailyStatsQueryPort;
import com.process.clash.domain.github.entity.GitHubDailyStats;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CompareGitHubService implements CompareGitHubUseCase {

    private final GitHubDailyStatsQueryPort gitHubDailyStatsQueryPort;

    @Override
    public CompareGitHubData.Result execute(CompareGitHubData.Command command) {

        LocalDate today = LocalDate.now();

        GitHubDailyStats yesterdayGitHubStat =
                gitHubDailyStatsQueryPort.findByUserIdAndStudyDate(command.actor().id(), today.minusDays(1))
                        .orElseThrow(GithubDailyStatsNotFoundException::new);

        GitHubDailyStats todayGitHubStat =
                gitHubDailyStatsQueryPort.findByUserIdAndStudyDate(command.actor().id(), today)
                        .orElseThrow(GithubDailyStatsNotFoundException::new);

        return CompareGitHubData.Result.from(yesterdayGitHubStat, todayGitHubStat);
    }
}
