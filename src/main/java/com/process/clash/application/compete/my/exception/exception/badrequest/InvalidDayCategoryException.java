package com.process.clash.application.compete.my.exception.exception.badrequest;

import com.process.clash.application.common.exception.exception.BadRequestException;
import com.process.clash.application.compete.my.exception.statuscode.MyCompeteStatusCode;

public class InvalidDayCategoryException extends BadRequestException {
    public InvalidDayCategoryException() {
        super(MyCompeteStatusCode.INVALID_DAY_CATEGORY);
    }

    public InvalidDayCategoryException(Throwable cause) {
        super(MyCompeteStatusCode.INVALID_DAY_CATEGORY, cause);
    }
}
