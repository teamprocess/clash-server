package com.process.clash.application.user.userattendance.port.in;

import com.process.clash.application.user.userattendance.data.MarkAttendanceData;

public interface MarkAttendanceUseCase {
    MarkAttendanceData.Result execute(MarkAttendanceData.Command command);
}