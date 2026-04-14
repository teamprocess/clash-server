package com.process.clash.application.user.userattendance.data;

import com.process.clash.application.common.actor.Actor;

public class MarkAttendanceData {

    public record Command(
            Actor actor
    ) {}

    public record Result(
            int attendanceStreak,
            int earnedCookies
    ) {}
}