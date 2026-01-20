package com.process.clash.application.record.data;

import com.process.clash.application.common.actor.Actor;

public class UpdateRecordSettingData {

    public record Command(
        Actor actor,
        Boolean pomodoroEnabled,
        Integer studyMinute,
        Integer breakMinute
    ) {}

    public record Result(
        Boolean pomodoroEnabled,
        Integer studyMinute,
        Integer breakMinute
    ) {
        public static Result create(Boolean pomodoroEnabled, Integer studyMinute, Integer breakMinute) {
            return new Result(pomodoroEnabled, studyMinute, breakMinute);
        }
    }
}
