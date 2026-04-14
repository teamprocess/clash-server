package com.process.clash.application.user.userattendance.data;

import com.process.clash.application.common.actor.Actor;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

public class GetWeeklyAttendanceData {

    public record Command(Actor actor) {}

    public record Result(
            int weekNumber,
            LocalDate weekStart,
            LocalDate weekEnd,
            List<AttendanceDay> days,
            int currentStreak
    ) {}

    public record AttendanceDay(
            LocalDate date,
            DayOfWeek dayOfWeek,
            boolean isAttended
    ) {}
}