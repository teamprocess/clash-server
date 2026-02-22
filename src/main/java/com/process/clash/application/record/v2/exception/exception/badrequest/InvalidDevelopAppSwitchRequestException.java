package com.process.clash.application.record.v2.exception.exception.badrequest;

import com.process.clash.application.common.exception.exception.BadRequestException;
import com.process.clash.application.record.v2.exception.statuscode.RecordV2StatusCode;

public class InvalidDevelopAppSwitchRequestException extends BadRequestException {

    public InvalidDevelopAppSwitchRequestException() {
        super(RecordV2StatusCode.INVALID_DEVELOP_APP_SWITCH_REQUEST);
    }

    public InvalidDevelopAppSwitchRequestException(Throwable cause) {
        super(RecordV2StatusCode.INVALID_DEVELOP_APP_SWITCH_REQUEST, cause);
    }
}
