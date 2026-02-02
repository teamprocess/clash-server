package com.process.clash.application.auth.electron.exception.exception.notfound;

import com.process.clash.application.auth.electron.exception.statuscode.ElectronAuthStatusCode;
import com.process.clash.application.common.exception.exception.NotFoundException;

public class UserNotFoundInAuthException extends NotFoundException {
    public UserNotFoundInAuthException() {
        super(ElectronAuthStatusCode.USER_NOT_FOUND_IN_AUTH);
    }

    public UserNotFoundInAuthException(Throwable cause) {
        super(ElectronAuthStatusCode.USER_NOT_FOUND_IN_AUTH, cause);
    }
}
