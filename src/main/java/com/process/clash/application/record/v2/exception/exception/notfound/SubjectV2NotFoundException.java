package com.process.clash.application.record.v2.exception.exception.notfound;

import com.process.clash.application.common.exception.exception.NotFoundException;
import com.process.clash.application.record.v2.exception.statuscode.RecordV2StatusCode;

public class SubjectV2NotFoundException extends NotFoundException {

    public SubjectV2NotFoundException() {
        super(RecordV2StatusCode.SUBJECT_V2_NOT_FOUND);
    }

    public SubjectV2NotFoundException(Throwable cause) {
        super(RecordV2StatusCode.SUBJECT_V2_NOT_FOUND, cause);
    }
}
