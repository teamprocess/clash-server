package com.process.clash.application.github.port.out;

import com.process.clash.domain.github.entity.GithubDailyStats;

import java.util.List;

public interface GithubDailyStatsStorePort {
    void upsertAll(List<GithubDailyStats> stats);
}
