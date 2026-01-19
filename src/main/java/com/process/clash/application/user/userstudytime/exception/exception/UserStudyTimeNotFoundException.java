package com.process.clash.application.user.userstudytime.exception.exception;

import com.process.clash.application.common.exception.exception.NotFoundException;
import com.process.clash.application.user.userstudytime.exception.statuscode.UserStudyTimeStatusCode;

public class UserStudyTimeNotFoundException extends NotFoundException {
    public UserStudyTimeNotFoundException() {
        super(UserStudyTimeStatusCode.USER_STUDY_TIME_NOT_FOUND);
    }

    public UserStudyTimeNotFoundException(Throwable cause) {
        super(UserStudyTimeStatusCode.USER_STUDY_TIME_NOT_FOUND, cause);
    }
}
