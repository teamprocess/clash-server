package com.process.clash.application.github.exception.exception.notfound;

import com.process.clash.application.common.exception.exception.NotFoundException;
import com.process.clash.application.github.exception.statuscode.GithubDailyStatsStatusCode;

public class GithubDailyStatsNotFoundException extends NotFoundException {
    public GithubDailyStatsNotFoundException() {
        super(GithubDailyStatsStatusCode.GITHUB_DAILY_STATS_NOT_FOUND);
    }

    public GithubDailyStatsNotFoundException(Throwable cause) {
        super(GithubDailyStatsStatusCode.GITHUB_DAILY_STATS_NOT_FOUND, cause);
    }
}
