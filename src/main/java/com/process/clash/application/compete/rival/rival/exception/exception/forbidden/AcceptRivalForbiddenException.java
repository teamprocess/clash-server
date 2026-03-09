package com.process.clash.application.compete.rival.rival.exception.exception.forbidden;

import com.process.clash.application.common.exception.exception.ForbiddenException;
import com.process.clash.application.compete.rival.rival.exception.statuscode.RivalStatusCode;

public class AcceptRivalForbiddenException extends ForbiddenException {
    public AcceptRivalForbiddenException() {
        super(RivalStatusCode.ACCEPT_RIVAL_FORBIDDEN);
    }
}