package com.process.clash.application.record.v2.exception.exception.notfound;

import com.process.clash.application.common.exception.exception.NotFoundException;
import com.process.clash.application.record.v2.exception.statuscode.RecordV2StatusCode;

public class RecordDevelopSegmentV2NotFoundException extends NotFoundException {

    public RecordDevelopSegmentV2NotFoundException() {
        super(RecordV2StatusCode.RECORD_DEVELOP_SEGMENT_V2_NOT_FOUND);
    }

    public RecordDevelopSegmentV2NotFoundException(Throwable cause) {
        super(RecordV2StatusCode.RECORD_DEVELOP_SEGMENT_V2_NOT_FOUND, cause);
    }
}
