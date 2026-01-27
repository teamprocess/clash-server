package com.process.clash.application.group.exception.exception.badrequest;

import com.process.clash.application.common.exception.exception.BadRequestException;
import com.process.clash.application.group.exception.statuscode.GroupStatusCode;

public class GroupNotMemberException extends BadRequestException {
    public GroupNotMemberException() {
        super(GroupStatusCode.GROUP_NOT_MEMBER);
    }

    public GroupNotMemberException(Throwable cause) {
        super(GroupStatusCode.GROUP_NOT_MEMBER, cause);
    }
}
