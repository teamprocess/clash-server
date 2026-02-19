package com.process.clash.application.record.exception.exception.notfound;

import com.process.clash.application.common.exception.exception.NotFoundException;
import com.process.clash.application.record.exception.statuscode.RecordStatusCode;

public class RecordSessionSegmentNotFound extends NotFoundException {

    public RecordSessionSegmentNotFound() {
        super(RecordStatusCode.RECORD_ACTIVITY_SEGMENT_NOT_FOUND);
    }

    public RecordSessionSegmentNotFound(Throwable cause) {
        super(RecordStatusCode.RECORD_ACTIVITY_SEGMENT_NOT_FOUND, cause);
    }
}
