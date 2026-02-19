package com.process.clash.application.record.exception.exception.notfound;

import com.process.clash.application.common.exception.exception.NotFoundException;
import com.process.clash.application.record.exception.statuscode.RecordStatusCode;

public class RecordSessionNotFound extends NotFoundException {

    public RecordSessionNotFound() {
        super(RecordStatusCode.ACTIVE_STUDY_SESSION_NOT_FOUND);
    }

    public RecordSessionNotFound(Throwable cause) {
        super(RecordStatusCode.ACTIVE_STUDY_SESSION_NOT_FOUND, cause);
    }
}
