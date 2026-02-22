package com.process.clash.application.record.v2.exception.exception.badrequest;

import com.process.clash.application.common.exception.exception.BadRequestException;
import com.process.clash.application.record.v2.exception.statuscode.RecordV2StatusCode;

public class InvalidRecordV2DailyDateRequestException extends BadRequestException {

    public InvalidRecordV2DailyDateRequestException() {
        super(RecordV2StatusCode.INVALID_RECORD_V2_DAILY_DATE_REQUEST);
    }

    public InvalidRecordV2DailyDateRequestException(Throwable cause) {
        super(RecordV2StatusCode.INVALID_RECORD_V2_DAILY_DATE_REQUEST, cause);
    }
}
