package com.process.clash.application.user.user.exception.exception.badrequest;

import com.process.clash.application.common.exception.exception.BadRequestException;
import com.process.clash.application.user.user.exception.statuscode.UserStatusCode;

public class InvalidTargetCategoryException extends BadRequestException {
    public InvalidTargetCategoryException() {
        super(UserStatusCode.INVALID_TARGET_CATEGORY);
    }

    public InvalidTargetCategoryException(Throwable cause) {
        super(UserStatusCode.INVALID_TARGET_CATEGORY, cause);
    }
}
