package com.process.clash.application.github.port.in;

public interface SyncGithubDailyStatsUseCase {

    void syncRecent365DaysForUser(Long userId);
}
