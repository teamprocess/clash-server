package com.process.clash.adapter.web.user.dto;

import com.process.clash.application.user.userattendance.data.MarkAttendanceData;

public class MarkAttendanceDto {

    public record Response(
            int attendanceStreak
    ) {
        public static Response from(MarkAttendanceData.Result result) {
            return new Response(result.attendanceStreak());
        }
    }
}