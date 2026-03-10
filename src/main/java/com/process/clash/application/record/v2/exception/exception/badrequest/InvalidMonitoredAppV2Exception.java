package com.process.clash.application.record.v2.exception.exception.badrequest;

import com.process.clash.application.common.exception.exception.BadRequestException;
import com.process.clash.application.record.v2.exception.statuscode.RecordV2StatusCode;

public class InvalidMonitoredAppV2Exception extends BadRequestException {

    public InvalidMonitoredAppV2Exception() {
        super(RecordV2StatusCode.INVALID_MONITORED_APP);
    }

    public InvalidMonitoredAppV2Exception(Throwable cause) {
        super(RecordV2StatusCode.INVALID_MONITORED_APP, cause);
    }
}