package com.process.clash.application.record.v2.exception.exception.forbidden;

import com.process.clash.application.common.exception.exception.ForbiddenException;
import com.process.clash.application.record.v2.exception.statuscode.RecordV2StatusCode;

public class SubjectV2AccessDeniedException extends ForbiddenException {

    public SubjectV2AccessDeniedException() {
        super(RecordV2StatusCode.SUBJECT_V2_ACCESS_DENIED);
    }

    public SubjectV2AccessDeniedException(Throwable cause) {
        super(RecordV2StatusCode.SUBJECT_V2_ACCESS_DENIED, cause);
    }
}
