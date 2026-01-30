package com.process.clash.application.profile.data;

import com.process.clash.application.common.actor.Actor;
import java.time.YearMonth;
import java.util.List;

public class GetMyActivityCalendarData {

    public record Command(
        Actor actor,
        YearMonth month
    ) {}

    public record Result(
        List<CalendarDay> calendar
    ) {
        public static Result from(List<CalendarDay> calendar) {
            return new Result(calendar);
        }
    }

    public record CalendarDay(
        String date,
        int studyTime
    ) {
        public static CalendarDay of(String date, int studyTime) {
            return new CalendarDay(date, studyTime);
        }
    }
}
