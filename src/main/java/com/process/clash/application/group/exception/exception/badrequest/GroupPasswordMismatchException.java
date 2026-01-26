package com.process.clash.application.group.exception.exception.badrequest;

import com.process.clash.application.common.exception.exception.BadRequestException;
import com.process.clash.application.group.exception.statuscode.GroupStatusCode;

public class GroupPasswordMismatchException extends BadRequestException {
    public GroupPasswordMismatchException() {
        super(GroupStatusCode.GROUP_PASSWORD_MISMATCH);
    }

    public GroupPasswordMismatchException(Throwable cause) {
        super(GroupStatusCode.GROUP_PASSWORD_MISMATCH, cause);
    }
}
