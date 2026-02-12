package com.process.clash.application.record.exception.exception.notfound;

import com.process.clash.application.common.exception.exception.NotFoundException;
import com.process.clash.application.record.exception.statuscode.RecordStatusCode;

public class RecordActivitySegmentNotFound extends NotFoundException {

    public RecordActivitySegmentNotFound() {
        super(RecordStatusCode.RECORD_ACTIVITY_SEGMENT_NOT_FOUND);
    }

    public RecordActivitySegmentNotFound(Throwable cause) {
        super(RecordStatusCode.RECORD_ACTIVITY_SEGMENT_NOT_FOUND, cause);
    }
}
