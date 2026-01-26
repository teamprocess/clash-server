package com.process.clash.application.group.exception.exception.conflict;

import com.process.clash.application.common.exception.exception.ConflictException;
import com.process.clash.application.group.exception.statuscode.GroupStatusCode;

public class GroupOwnerCannotQuitException extends ConflictException {
    public GroupOwnerCannotQuitException() {
        super(GroupStatusCode.GROUP_OWNER_CANNOT_QUIT);
    }

    public GroupOwnerCannotQuitException(Throwable cause) {
        super(GroupStatusCode.GROUP_OWNER_CANNOT_QUIT, cause);
    }
}
