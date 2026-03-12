package com.process.clash.application.auth.electron.exception.exception.badrequest;

import com.process.clash.application.auth.electron.exception.statuscode.ElectronAuthStatusCode;
import com.process.clash.application.common.exception.exception.BadRequestException;

public class InvalidStateException extends BadRequestException {
    public InvalidStateException() {
        super(ElectronAuthStatusCode.INVALID_STATE);
    }
}
