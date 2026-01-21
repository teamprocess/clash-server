package com.process.clash.application.github.port.out;

import com.process.clash.application.github.model.GithubSyncTarget;
import com.process.clash.domain.github.entity.GitHubDailyStats;

import java.time.LocalDate;
import java.util.List;

public interface GithubStatsFetchPort {
    List<GitHubDailyStats> fetchDailyStats(GithubSyncTarget target, List<LocalDate> studyDates);
}
