package com.process.clash.adapter.web.user.dto;

import com.process.clash.application.profile.data.GetMyCalendarData;
import java.util.List;

public class GetMyCalendarDto {

    public record Response(
        List<CalendarDay> calendar
    ) {
        public static Response from(GetMyCalendarData.Result result) {
            List<CalendarDay> calendar = result.calendar().stream()
                    .map(CalendarDay::from)
                    .toList();
            return new Response(calendar);
        }
    }

    public record CalendarDay(
        String date,
        int studyTime
    ) {
        public static CalendarDay from(GetMyCalendarData.CalendarDay day) {
            return new CalendarDay(day.date(), day.studyTime());
        }
    }
}
