package com.process.clash.application.record.v2.exception.exception.notfound;

import com.process.clash.application.common.exception.exception.NotFoundException;
import com.process.clash.application.record.v2.exception.statuscode.RecordV2StatusCode;

public class ActiveSessionV2NotFoundException extends NotFoundException {

    public ActiveSessionV2NotFoundException() {
        super(RecordV2StatusCode.ACTIVE_RECORD_SESSION_V2_NOT_FOUND);
    }

    public ActiveSessionV2NotFoundException(Throwable cause) {
        super(RecordV2StatusCode.ACTIVE_RECORD_SESSION_V2_NOT_FOUND, cause);
    }
}
