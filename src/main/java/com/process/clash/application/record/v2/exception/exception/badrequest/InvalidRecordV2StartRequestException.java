package com.process.clash.application.record.v2.exception.exception.badrequest;

import com.process.clash.application.common.exception.exception.BadRequestException;
import com.process.clash.application.record.v2.exception.statuscode.RecordV2StatusCode;

public class InvalidRecordV2StartRequestException extends BadRequestException {

    public InvalidRecordV2StartRequestException() {
        super(RecordV2StatusCode.INVALID_RECORD_V2_START_REQUEST);
    }

    public InvalidRecordV2StartRequestException(Throwable cause) {
        super(RecordV2StatusCode.INVALID_RECORD_V2_START_REQUEST, cause);
    }
}
