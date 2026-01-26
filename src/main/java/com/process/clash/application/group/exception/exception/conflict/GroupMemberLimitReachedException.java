package com.process.clash.application.group.exception.exception.conflict;

import com.process.clash.application.common.exception.exception.ConflictException;
import com.process.clash.application.group.exception.statuscode.GroupStatusCode;

public class GroupMemberLimitReachedException extends ConflictException {
    public GroupMemberLimitReachedException() {
        super(GroupStatusCode.GROUP_MEMBER_LIMIT_REACHED);
    }

    public GroupMemberLimitReachedException(Throwable cause) {
        super(GroupStatusCode.GROUP_MEMBER_LIMIT_REACHED, cause);
    }
}
