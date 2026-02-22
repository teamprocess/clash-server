package com.process.clash.application.record.v2.exception.exception.conflict;

import com.process.clash.application.common.exception.exception.ConflictException;
import com.process.clash.application.record.v2.exception.statuscode.RecordV2StatusCode;

public class RecordSessionV2AlreadyStartedException extends ConflictException {

    public RecordSessionV2AlreadyStartedException() {
        super(RecordV2StatusCode.RECORD_SESSION_V2_ALREADY_STARTED);
    }

    public RecordSessionV2AlreadyStartedException(Throwable cause) {
        super(RecordV2StatusCode.RECORD_SESSION_V2_ALREADY_STARTED, cause);
    }
}
