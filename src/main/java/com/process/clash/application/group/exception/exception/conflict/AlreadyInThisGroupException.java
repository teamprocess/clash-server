package com.process.clash.application.group.exception.exception.conflict;

import com.process.clash.application.common.exception.exception.ConflictException;
import com.process.clash.application.group.exception.statuscode.GroupStatusCode;

public class AlreadyInThisGroupException extends ConflictException {
    public AlreadyInThisGroupException() {
        super(GroupStatusCode.ALREADY_IN_THIS_GROUP);
    }

    public AlreadyInThisGroupException(Throwable cause) {
        super(GroupStatusCode.ALREADY_IN_THIS_GROUP, cause);
    }
}
