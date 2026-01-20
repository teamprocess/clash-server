package com.process.clash.application.user.userpomodorosetting.exception.exception.notfound;

import com.process.clash.application.common.exception.exception.NotFoundException;
import com.process.clash.application.user.userpomodorosetting.exception.statuscode.UserPomodoroSettingStatusCode;

public class UserPomodoroSettingNotFoundException extends NotFoundException {
    public UserPomodoroSettingNotFoundException() {
        super(UserPomodoroSettingStatusCode.USER_POMODORO_SETTING_NOT_FOUND);
    }

    public UserPomodoroSettingNotFoundException(Throwable cause) {
        super(UserPomodoroSettingStatusCode.USER_POMODORO_SETTING_NOT_FOUND, cause);
    }
}
