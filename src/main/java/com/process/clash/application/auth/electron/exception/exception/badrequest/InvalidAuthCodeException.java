package com.process.clash.application.auth.electron.exception.exception.badrequest;

import com.process.clash.application.auth.electron.exception.statuscode.ElectronAuthStatusCode;
import com.process.clash.application.common.exception.exception.BadRequestException;

public class InvalidAuthCodeException extends BadRequestException {
    public InvalidAuthCodeException() {
        super(ElectronAuthStatusCode.INVALID_AUTH_CODE);
    }

    public InvalidAuthCodeException(Throwable cause) {
        super(ElectronAuthStatusCode.INVALID_AUTH_CODE, cause);
    }
}
