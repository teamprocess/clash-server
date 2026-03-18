package com.process.clash.application.group.exception.exception.conflict;

import com.process.clash.application.common.exception.exception.ConflictException;
import com.process.clash.application.group.exception.statuscode.GroupStatusCode;

public class GroupDuplicateNameException extends ConflictException {
    public GroupDuplicateNameException() {
        super(GroupStatusCode.DUPLICATE_GROUP_NAME);
    }
}