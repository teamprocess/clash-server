package com.process.clash.application.record.v2.exception.exception.notfound;

import com.process.clash.application.common.exception.exception.NotFoundException;
import com.process.clash.application.record.v2.exception.statuscode.RecordV2StatusCode;

public class RecordDevelopSessionV2NotFoundException extends NotFoundException {

    public RecordDevelopSessionV2NotFoundException() {
        super(RecordV2StatusCode.RECORD_DEVELOP_SESSION_V2_NOT_FOUND);
    }

    public RecordDevelopSessionV2NotFoundException(Throwable cause) {
        super(RecordV2StatusCode.RECORD_DEVELOP_SESSION_V2_NOT_FOUND, cause);
    }
}
