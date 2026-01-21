package com.process.clash.application.github.port.out;

import com.process.clash.application.github.model.GithubSyncTarget;
import com.process.clash.domain.github.entity.GithubDailyStats;

import java.time.LocalDate;
import java.util.List;

public interface GithubStatsFetchPort {
    List<GithubDailyStats> fetchDailyStats(GithubSyncTarget target, List<LocalDate> studyDates);
}
