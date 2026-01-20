package com.process.clash.application.record.data;

import com.process.clash.application.common.actor.Actor;
import lombok.Builder;

public class GetRecordSettingData {

    @Builder
    public record Command(
        Actor actor
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
