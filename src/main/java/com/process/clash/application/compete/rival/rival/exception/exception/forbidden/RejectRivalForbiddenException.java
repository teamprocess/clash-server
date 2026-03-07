package com.process.clash.application.compete.rival.rival.exception.exception.forbidden;

import com.process.clash.application.common.exception.exception.ForbiddenException;
import com.process.clash.application.compete.rival.rival.exception.statuscode.RivalStatusCode;

public class RejectRivalForbiddenException extends ForbiddenException {
    public RejectRivalForbiddenException() {
        super(RivalStatusCode.REJECT_RIVAL_FORBIDDEN);
    }
}
