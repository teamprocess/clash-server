package com.process.clash.application.user.userattendance.exception.exception.notfound;

import com.process.clash.application.common.exception.exception.NotFoundException;
import com.process.clash.application.user.userattendance.exception.statuscode.UserAttendanceStatusCode;

public class UserAttendanceNotFoundException extends NotFoundException {
    public UserAttendanceNotFoundException() {
        super(UserAttendanceStatusCode.USER_ATTENDANCE_NOT_FOUND);
    }
}