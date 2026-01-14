package com.process.clash.application.record.exception.exception.notfound;

import com.process.clash.application.common.exception.exception.NotFoundException;
import com.process.clash.application.record.exception.status.RecordStatusCode;

public class TaskNotFoundException extends NotFoundException {
    public TaskNotFoundException() {
        super(RecordStatusCode.TASK_NOT_FOUND);
    }

    public TaskNotFoundException(Throwable cause) {
        super(RecordStatusCode.TASK_NOT_FOUND, cause);
    }
}
