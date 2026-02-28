package com.process.clash.application.compete.rival.rival.exception.exception.badrequet;

import com.process.clash.application.common.exception.exception.BadRequestException;
import com.process.clash.application.compete.rival.rival.exception.statuscode.RivalStatusCode;

public class CancelRivalInvalidStatusException extends BadRequestException {
    public CancelRivalInvalidStatusException() {
        super(RivalStatusCode.CANCEL_RIVAL_INVALID_STATUS);
    }
}
