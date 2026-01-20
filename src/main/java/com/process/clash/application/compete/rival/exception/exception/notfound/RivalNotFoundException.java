package com.process.clash.application.compete.rival.exception.exception.notfound;

import com.process.clash.application.common.exception.exception.NotFoundException;
import com.process.clash.application.compete.rival.exception.statuscode.RivalStatusCode;

public class RivalNotFoundException extends NotFoundException {
    public RivalNotFoundException() {
        super(RivalStatusCode.RIVAL_NOT_FOUND);
    }

    public RivalNotFoundException(Throwable cause) {
        super(RivalStatusCode.RIVAL_NOT_FOUND, cause);
    }
}
