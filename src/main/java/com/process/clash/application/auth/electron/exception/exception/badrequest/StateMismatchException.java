package com.process.clash.application.auth.electron.exception.exception.badrequest;

import com.process.clash.application.auth.electron.exception.statuscode.ElectronAuthStatusCode;
import com.process.clash.application.common.exception.exception.BadRequestException;

public class StateMismatchException extends BadRequestException {
    public StateMismatchException() {
        super(ElectronAuthStatusCode.STATE_MISMATCH);
    }

    public StateMismatchException(Throwable cause) {
        super(ElectronAuthStatusCode.STATE_MISMATCH, cause);
    }
}
