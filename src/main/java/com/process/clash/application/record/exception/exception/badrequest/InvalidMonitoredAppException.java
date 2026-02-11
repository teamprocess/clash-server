package com.process.clash.application.record.exception.exception.badrequest;

import com.process.clash.application.common.exception.exception.BadRequestException;
import com.process.clash.application.record.exception.statuscode.RecordStatusCode;

public class InvalidMonitoredAppException extends BadRequestException {

    public InvalidMonitoredAppException() {
        super(RecordStatusCode.INVALID_MONITORED_APP);
    }

    public InvalidMonitoredAppException(Throwable cause) {
        super(RecordStatusCode.INVALID_MONITORED_APP, cause);
    }
}
