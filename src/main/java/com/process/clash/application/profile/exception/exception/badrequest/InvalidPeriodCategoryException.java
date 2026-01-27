package com.process.clash.application.profile.exception.exception.badrequest;

import com.process.clash.application.common.exception.exception.BadRequestException;
import com.process.clash.application.profile.exception.statuscode.ProfileStatusCode;

public class InvalidPeriodCategoryException extends BadRequestException {
    public InvalidPeriodCategoryException() {
        super(ProfileStatusCode.INVALID_PERIOD_CATEGORY);
    }

    public InvalidPeriodCategoryException(Throwable cause) {
        super(ProfileStatusCode.INVALID_PERIOD_CATEGORY, cause);
    }
}
