package com.process.clash.application.githubinfo.exception.statuscode;

import com.process.clash.application.common.exception.statuscode.ErrorCategory;
import com.process.clash.application.common.exception.statuscode.StatusCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GitHubInfoStatusCode implements StatusCode {

    // 404
    GIT_HUB_INFO_NOT_FOUND("GIT_HUB_INFO_NOT_FOUND", "깃허브 정보가 존재하지 않습니다.", ErrorCategory.NOT_FOUND),
    ;

    private final String code;
    private final String message;
    private final ErrorCategory errorCategory;
}