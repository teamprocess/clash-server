package com.process.clash.application.group.exception.exception.conflict;

import com.process.clash.application.common.exception.exception.ConflictException;
import com.process.clash.application.group.exception.statuscode.GroupStatusCode;

public class CannotQuitGlobalGroupException extends ConflictException {
    public CannotQuitGlobalGroupException() {
        super(GroupStatusCode.CANNOT_QUIT_GLOBAL_GROUP);
    }
}
