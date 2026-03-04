package com.process.clash.application.record.v2.exception.exception.badrequest;

import com.process.clash.application.common.exception.exception.BadRequestException;
import com.process.clash.application.record.v2.exception.statuscode.RecordV2StatusCode;

public class TaskStartRequiresOnlineException extends BadRequestException {

    public TaskStartRequiresOnlineException() {
        super(RecordV2StatusCode.TASK_START_REQUIRES_ONLINE);
    }

    public TaskStartRequiresOnlineException(Throwable cause) {
        super(RecordV2StatusCode.TASK_START_REQUIRES_ONLINE, cause);
    }
}
