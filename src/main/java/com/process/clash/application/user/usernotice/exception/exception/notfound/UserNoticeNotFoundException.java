package com.process.clash.application.user.usernotice.exception.exception.notfound;

import com.process.clash.application.common.exception.exception.NotFoundException;
import com.process.clash.application.user.usernotice.exception.statuscode.UserNoticeStatusCode;

public class UserNoticeNotFoundException extends NotFoundException {

    public UserNoticeNotFoundException() {
        super(UserNoticeStatusCode.USER_NOTICE_NOT_FOUND);
    }
}
