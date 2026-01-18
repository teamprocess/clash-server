package com.process.clash.application.user.user.exception.exception.badrequest;

import com.process.clash.application.common.exception.exception.BadRequestException;
import com.process.clash.application.user.user.exception.statuscode.UserStatusCode;

public class InvalidPeriodCategoryException extends BadRequestException {
    public InvalidPeriodCategoryException() {
        super(UserStatusCode.INVALID_PERIOD_CATEGORY);
    }

    public InvalidPeriodCategoryException(Throwable cause) {
        super(UserStatusCode.INVALID_PERIOD_CATEGORY, cause);
    }
}
