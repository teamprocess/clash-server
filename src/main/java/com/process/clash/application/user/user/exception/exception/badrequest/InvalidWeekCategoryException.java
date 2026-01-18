package com.process.clash.application.user.user.exception.exception.badrequest;

import com.process.clash.application.common.exception.exception.BadRequestException;
import com.process.clash.application.user.user.exception.statuscode.UserStatusCode;

public class InvalidWeekCategoryException extends BadRequestException {
    public InvalidWeekCategoryException() {
        super(UserStatusCode.INVALID_WEEK_CATEGORY);
    }

    public InvalidWeekCategoryException(Throwable cause) {
        super(UserStatusCode.INVALID_WEEK_CATEGORY, cause);
    }
}
