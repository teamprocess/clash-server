package com.process.clash.application.record.exception.exception.conflict;

import com.process.clash.application.common.exception.exception.ConflictException;
import com.process.clash.application.record.exception.statuscode.RecordStatusCode;

public class StudySessionAlreadyStartedException extends ConflictException {

    public StudySessionAlreadyStartedException() {
        super(RecordStatusCode.STUDY_SESSION_ALREADY_STARTED);
    }

    public StudySessionAlreadyStartedException(Throwable cause) {
        super(RecordStatusCode.STUDY_SESSION_ALREADY_STARTED, cause);
    }
}
