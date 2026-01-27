package com.process.clash.application.group.exception.exception.badrequest;

import com.process.clash.application.common.exception.exception.BadRequestException;
import com.process.clash.application.group.exception.statuscode.GroupStatusCode;

public class GroupPasswordRequiredException extends BadRequestException {
    public GroupPasswordRequiredException() {
        super(GroupStatusCode.GROUP_PASSWORD_REQUIRED);
    }

    public GroupPasswordRequiredException(Throwable cause) {
        super(GroupStatusCode.GROUP_PASSWORD_REQUIRED, cause);
    }
}
