package com.process.clash.application.user.usernotice.exception.statuscode;

import com.process.clash.application.common.exception.statuscode.ErrorCategory;
import com.process.clash.application.common.exception.statuscode.StatusCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserNoticeStatusCode implements StatusCode {

    // 400
    INVALID_USER_NOTICE("INVALID_USER_NOTICE", "잘못된 유저 알림입니다.", ErrorCategory.BAD_REQUEST),
    ;

    private final String code;
    private final String message;
    private final ErrorCategory errorCategory;
}