package com.process.clash.application.user.exception.status;

import com.process.clash.application.common.exception.statuscode.ErrorCategory;
import com.process.clash.application.common.exception.statuscode.StatusCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserStatusCode implements StatusCode {
    // 403
    REQUIRED_ADMIN_ROLE("REQUIRED_ADMIN_ROLE", "어드민 권한이 요구됩니다.", ErrorCategory.FORBIDDEN),
    REQUIRED_USER_ROLE("REQUIRED_USER_ROLE", "유저 권한이 요구됩니다.", ErrorCategory.FORBIDDEN),

    // 404
    USER_NOT_FOUND("USER_NOT_FOUND", "존재하지 않는 유저입니다.", ErrorCategory.NOT_FOUND),
    ;

    private final String code;
    private final String message;
    private final ErrorCategory errorCategory;
}