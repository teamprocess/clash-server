package com.process.clash.application.github.exception.statuscode;

import com.process.clash.application.common.exception.statuscode.ErrorCategory;
import com.process.clash.application.common.exception.statuscode.StatusCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GithubDailyStatsStatusCode implements StatusCode {

    // 404
    GITHUB_DAILY_STATS_NOT_FOUND("GITHUB_DAILY_STATS_NOT_FOUND", "GitHub 일일 통계가 존재하지 않습니다.", ErrorCategory.NOT_FOUND),
    ;

    private final String code;
    private final String message;
    private final ErrorCategory errorCategory;
}
