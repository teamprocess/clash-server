package com.process.clash.application.group.exception.exception.badrequest;

import com.process.clash.application.common.exception.exception.BadRequestException;
import com.process.clash.application.group.exception.statuscode.GroupStatusCode;

public class GroupMemberLimitTooSmallException extends BadRequestException {
    public GroupMemberLimitTooSmallException() {
        super(GroupStatusCode.GROUP_MEMBER_LIMIT_TOO_SMALL);
    }

    public GroupMemberLimitTooSmallException(Throwable cause) {
        super(GroupStatusCode.GROUP_MEMBER_LIMIT_TOO_SMALL, cause);
    }
}
