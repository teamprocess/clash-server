package com.process.clash.application.record.exception.exception.notfound;

import com.process.clash.application.common.exception.exception.NotFoundException;
import com.process.clash.application.record.exception.statuscode.RecordStatusCode;

public class ActiveSessionNotFound extends NotFoundException {

    public ActiveSessionNotFound() {
        super(RecordStatusCode.ACTIVE_STUDY_SESSION_NOT_FOUND);
    }

    public ActiveSessionNotFound(Throwable cause) {
        super(RecordStatusCode.ACTIVE_STUDY_SESSION_NOT_FOUND, cause);
    }
}
