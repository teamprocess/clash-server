package com.process.clash.application.record.v2.exception.exception.conflict;

import com.process.clash.application.common.exception.exception.ConflictException;
import com.process.clash.application.record.v2.exception.statuscode.RecordV2StatusCode;

public class SubjectV2HasActiveSessionException extends ConflictException {

    public SubjectV2HasActiveSessionException() {
        super(RecordV2StatusCode.SUBJECT_V2_HAS_ACTIVE_SESSION);
    }

    public SubjectV2HasActiveSessionException(Throwable cause) {
        super(RecordV2StatusCode.SUBJECT_V2_HAS_ACTIVE_SESSION, cause);
    }
}
