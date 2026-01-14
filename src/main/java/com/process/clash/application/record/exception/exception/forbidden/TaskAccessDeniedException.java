package com.process.clash.application.record.exception.exception.forbidden;

import com.process.clash.application.common.exception.exception.ForbiddenException;
import com.process.clash.application.common.exception.statuscode.CommonStatusCode;

public class TaskAccessDeniedException extends ForbiddenException {
    public TaskAccessDeniedException() {
        super(CommonStatusCode.PERMISSION_DENIED);
    }

    public TaskAccessDeniedException(Throwable cause) {
        super(CommonStatusCode.PERMISSION_DENIED, cause);
    }
}
