package com.process.clash.application.user.usernotice.exception.exception.badrequest;

import com.process.clash.application.common.exception.exception.BadRequestException;
import com.process.clash.application.user.usernotice.exception.statuscode.UserNoticeStatusCode;

public class InvalidUserNoticeException extends BadRequestException {
    public InvalidUserNoticeException() {
        super(UserNoticeStatusCode.INVALID_USER_NOTICE);
    }

    protected InvalidUserNoticeException(Throwable cause) {
        super(UserNoticeStatusCode.INVALID_USER_NOTICE, cause);
    }
}
