package com.process.clash.adapter.web.user.dto;

import com.process.clash.application.user.userattendance.data.GetWeeklyAttendanceData;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

public class GetWeeklyAttendanceDto {

    public record Response(
            LocalDate weekStart,
            LocalDate weekEnd,
            List<AttendanceDayItem> days
    ) {
        public static Response from(GetWeeklyAttendanceData.Result result) {
            return new Response(
                    result.weekStart(),
                    result.weekEnd(),
                    result.days().stream()
                            .map(AttendanceDayItem::from)
                            .toList()
            );
        }
    }

    public record AttendanceDayItem(
            LocalDate date,
            DayOfWeek dayOfWeek,
            boolean isAttended
    ) {
        public static AttendanceDayItem from(GetWeeklyAttendanceData.AttendanceDay day) {
            return new AttendanceDayItem(day.date(), day.dayOfWeek(), day.isAttended());
        }
    }
}