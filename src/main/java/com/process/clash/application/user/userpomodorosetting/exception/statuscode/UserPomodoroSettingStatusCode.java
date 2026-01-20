package com.process.clash.application.user.userpomodorosetting.exception.statuscode;

import com.process.clash.application.common.exception.statuscode.ErrorCategory;
import com.process.clash.application.common.exception.statuscode.StatusCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserPomodoroSettingStatusCode implements StatusCode {

    // 404
    USER_POMODORO_SETTING_NOT_FOUND("USER_POMODORO_SETTING_NOT_FOUND", "존재하지 않는 유저 뽀모도로 타이머 설정입니다.", ErrorCategory.NOT_FOUND),
    ;

    private final String code;
    private final String message;
    private final ErrorCategory errorCategory;
}