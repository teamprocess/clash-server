package com.process.clash.application.record.exception.exception.badrequest;

import com.process.clash.application.common.exception.exception.BadRequestException;
import com.process.clash.application.record.exception.statuscode.RecordStatusCode;

public class InvalidRecordStartRequestException extends BadRequestException {

    public InvalidRecordStartRequestException() {
        super(RecordStatusCode.INVALID_RECORD_START_REQUEST);
    }

    public InvalidRecordStartRequestException(Throwable cause) {
        super(RecordStatusCode.INVALID_RECORD_START_REQUEST, cause);
    }
}
