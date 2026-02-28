package com.process.clash.application.compete.rival.rival.exception.exception.forbidden;

import com.process.clash.application.common.exception.exception.ForbiddenException;
import com.process.clash.application.compete.rival.rival.exception.statuscode.RivalStatusCode;

public class CancelRivalForbiddenException extends ForbiddenException {
    public CancelRivalForbiddenException() {
        super(RivalStatusCode.CANCEL_RIVAL_FORBIDDEN);
    }
}
