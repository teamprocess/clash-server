package com.process.clash.application.user.usergithub.exception.statuscode;

import com.process.clash.application.common.exception.statuscode.ErrorCategory;
import com.process.clash.application.common.exception.statuscode.StatusCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserGitHubStatusCode implements StatusCode {

    GITHUB_OAUTH_INVALID_CODE("GITHUB_OAUTH_INVALID_CODE", "GitHub 인증 코드가 유효하지 않습니다.", ErrorCategory.BAD_REQUEST),
    GITHUB_OAUTH_TOKEN_REQUEST_FAILED("GITHUB_OAUTH_TOKEN_REQUEST_FAILED", "GitHub 액세스 토큰 요청에 실패했습니다.", ErrorCategory.INTERNAL_ERROR),
    GITHUB_OAUTH_USER_FETCH_FAILED("GITHUB_OAUTH_USER_FETCH_FAILED", "GitHub 사용자 정보를 가져오지 못했습니다.", ErrorCategory.INTERNAL_ERROR),
    GITHUB_ALREADY_LINKED("GITHUB_ALREADY_LINKED", "이미 다른 계정에 연결된 GitHub 계정입니다.", ErrorCategory.CONFLICT),
    ;

    private final String code;
    private final String message;
    private final ErrorCategory errorCategory;
}
