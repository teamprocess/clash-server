package com.process.clash.application.user.userattendance.port.in;

import com.process.clash.application.user.userattendance.data.GetWeeklyAttendanceData;

public interface GetWeeklyAttendanceUseCase {
    GetWeeklyAttendanceData.Result execute(GetWeeklyAttendanceData.Command command);
}