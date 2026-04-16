package com.process.clash.application.user.userattendance.exception.exception.conflict;

import com.process.clash.application.common.exception.exception.ConflictException;
import com.process.clash.application.user.userattendance.exception.statuscode.UserAttendanceStatusCode;

public class AlreadyAttendedException extends ConflictException {
    public AlreadyAttendedException() {
        super(UserAttendanceStatusCode.ALREADY_ATTENDED);
    }
}