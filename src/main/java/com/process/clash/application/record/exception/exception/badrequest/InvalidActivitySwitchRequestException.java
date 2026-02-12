package com.process.clash.application.record.exception.exception.badrequest;

import com.process.clash.application.common.exception.exception.BadRequestException;
import com.process.clash.application.record.exception.statuscode.RecordStatusCode;

public class InvalidActivitySwitchRequestException extends BadRequestException {

    public InvalidActivitySwitchRequestException() {
        super(RecordStatusCode.INVALID_ACTIVITY_SWITCH_REQUEST);
    }

    public InvalidActivitySwitchRequestException(Throwable cause) {
        super(RecordStatusCode.INVALID_ACTIVITY_SWITCH_REQUEST, cause);
    }
}
