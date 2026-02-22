package com.process.clash.application.record.v2.exception.exception.badrequest;

import com.process.clash.application.common.exception.exception.BadRequestException;
import com.process.clash.application.record.v2.exception.statuscode.RecordV2StatusCode;

public class DevelopStartRequiresOnlineException extends BadRequestException {

    public DevelopStartRequiresOnlineException() {
        super(RecordV2StatusCode.DEVELOP_START_REQUIRES_ONLINE);
    }

    public DevelopStartRequiresOnlineException(Throwable cause) {
        super(RecordV2StatusCode.DEVELOP_START_REQUIRES_ONLINE, cause);
    }
}
