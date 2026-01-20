package com.process.clash.application.record.data;

import com.process.clash.application.common.actor.Actor;
import java.time.LocalDateTime;

public class StopRecordData {

    public record Command(
            Actor actor
    ) {}

    public record Result(
            Long taskId,
            LocalDateTime stoppedAt
    ) {

        public static Result create(Long taskId, LocalDateTime stoppedAt) {
            return new Result(taskId, stoppedAt);
        }
    }
}
