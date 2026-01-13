package com.process.clash.application.mainpage.exception.exception.notfound;

import com.process.clash.application.common.exception.exception.NotFoundException;
import com.process.clash.application.mainpage.exception.status.UserStatusCode;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException() {
        super(UserStatusCode.USER_NOT_FOUND);
    }

    public UserNotFoundException(Throwable cause) {
        super(UserStatusCode.USER_NOT_FOUND, cause);
    }
}
