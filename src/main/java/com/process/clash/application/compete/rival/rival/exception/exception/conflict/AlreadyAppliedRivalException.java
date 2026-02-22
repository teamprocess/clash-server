package com.process.clash.application.compete.rival.rival.exception.exception.conflict;

import com.process.clash.application.common.exception.exception.ConflictException;
import com.process.clash.application.compete.rival.rival.exception.statuscode.RivalStatusCode;

public class AlreadyAppliedRivalException extends ConflictException {
    public AlreadyAppliedRivalException() {
        super(RivalStatusCode.ALREADY_APPLIED_RIVAL);
    }

    public AlreadyAppliedRivalException(Throwable cause) {
        super(RivalStatusCode.ALREADY_APPLIED_RIVAL, cause);
    }
}
