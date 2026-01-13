package com.process.clash.application.user.exception.exception.forbidden;

import com.process.clash.application.common.exception.exception.NotFoundException;
import com.process.clash.application.user.exception.status.UserStatusCode;

public class NotProperAccessException extends NotFoundException {
    public NotProperAccessException() {
        super(UserStatusCode.NOT_PROPER_ACCESS);
    }

    public NotProperAccessException(Throwable cause) {
        super(UserStatusCode.NOT_PROPER_ACCESS, cause);
    }
}
