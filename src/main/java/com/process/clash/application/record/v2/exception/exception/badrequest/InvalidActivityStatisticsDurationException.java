package com.process.clash.application.record.v2.exception.exception.badrequest;

import com.process.clash.application.common.exception.exception.BadRequestException;
import com.process.clash.application.record.v2.exception.statuscode.RecordV2StatusCode;

public class InvalidActivityStatisticsDurationException extends BadRequestException {

    public InvalidActivityStatisticsDurationException() {
        super(RecordV2StatusCode.INVALID_ACTIVITY_STATISTICS_DURATION);
    }

    public InvalidActivityStatisticsDurationException(Throwable cause) {
        super(RecordV2StatusCode.INVALID_ACTIVITY_STATISTICS_DURATION, cause);
    }
}
