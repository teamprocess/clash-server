package com.process.clash.application.group.exception.exception.conflict;

import com.process.clash.application.common.exception.exception.ConflictException;
import com.process.clash.application.group.exception.statuscode.GroupStatusCode;

public class GroupAlreadyMemberException extends ConflictException {
    public GroupAlreadyMemberException() {
        super(GroupStatusCode.GROUP_ALREADY_MEMBER);
    }

    public GroupAlreadyMemberException(Throwable cause) {
        super(GroupStatusCode.GROUP_ALREADY_MEMBER, cause);
    }
}
