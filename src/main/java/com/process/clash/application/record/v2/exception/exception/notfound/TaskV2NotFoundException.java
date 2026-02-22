package com.process.clash.application.record.v2.exception.exception.notfound;

import com.process.clash.application.common.exception.exception.NotFoundException;
import com.process.clash.application.record.v2.exception.statuscode.RecordV2StatusCode;

public class TaskV2NotFoundException extends NotFoundException {

    public TaskV2NotFoundException() {
        super(RecordV2StatusCode.TASK_V2_NOT_FOUND);
    }

    public TaskV2NotFoundException(Throwable cause) {
        super(RecordV2StatusCode.TASK_V2_NOT_FOUND, cause);
    }
}
