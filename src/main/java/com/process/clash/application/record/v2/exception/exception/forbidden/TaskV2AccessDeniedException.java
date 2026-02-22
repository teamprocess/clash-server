package com.process.clash.application.record.v2.exception.exception.forbidden;

import com.process.clash.application.common.exception.exception.ForbiddenException;
import com.process.clash.application.record.v2.exception.statuscode.RecordV2StatusCode;

public class TaskV2AccessDeniedException extends ForbiddenException {

    public TaskV2AccessDeniedException() {
        super(RecordV2StatusCode.TASK_V2_ACCESS_DENIED);
    }

    public TaskV2AccessDeniedException(Throwable cause) {
        super(RecordV2StatusCode.TASK_V2_ACCESS_DENIED, cause);
    }
}
