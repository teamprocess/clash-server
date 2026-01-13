package com.process.clash.application.user.exception.status;

import com.process.clash.application.common.exception.statuscode.ErrorCategory;
import com.process.clash.application.common.exception.statuscode.StatusCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserStatusCode implements StatusCode {
    // 404
    USER_NOT_FOUND("USER_NOT_FOUND", "존재하지 않는 유저입니다.", ErrorCategory.NOT_FOUND),
    INVALID_CREDENTIALS("INVALID_CREDENTIALS", "유효하지 않은 자격 증명입니다.", ErrorCategory.UNAUTHORIZED),
    ;

    private final String code;
    private final String message;
    private final ErrorCategory errorCategory;
}