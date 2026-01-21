package com.process.clash.application.record.exception.exception.conflict;

import com.process.clash.application.common.exception.exception.ConflictException;
import com.process.clash.application.record.exception.statuscode.RecordStatusCode;

public class TaskHasActiveSessionException extends ConflictException {

    public TaskHasActiveSessionException() {
        super(RecordStatusCode.TASK_HAS_ACTIVE_SESSION);
    }

    public TaskHasActiveSessionException(Throwable cause) {
        super(RecordStatusCode.TASK_HAS_ACTIVE_SESSION, cause);
    }
}
