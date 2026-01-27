package com.process.clash.application.group.exception.exception.notfound;

import com.process.clash.application.common.exception.exception.NotFoundException;
import com.process.clash.application.group.exception.statuscode.GroupStatusCode;

public class GroupNotFoundException extends NotFoundException {
    public GroupNotFoundException() {
        super(GroupStatusCode.GROUP_NOT_FOUND);
    }

    public GroupNotFoundException(Throwable cause) {
        super(GroupStatusCode.GROUP_NOT_FOUND, cause);
    }
}
