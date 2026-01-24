package com.process.clash.application.group.exception.exception.forbidden;

import com.process.clash.application.common.exception.exception.ForbiddenException;
import com.process.clash.application.common.exception.statuscode.CommonStatusCode;

public class GroupAccessDeniedException extends ForbiddenException {
    public GroupAccessDeniedException() {
        super(CommonStatusCode.PERMISSION_DENIED);
    }

    public GroupAccessDeniedException(Throwable cause) {
        super(CommonStatusCode.PERMISSION_DENIED, cause);
    }
}
