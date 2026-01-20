package com.process.clash.application.record.data;

import com.process.clash.application.common.actor.Actor;
import java.time.Instant;

public class StopRecordData {

    public record Command(
            Actor actor
    ) {}

    public record Result(
            Long taskId,
            Instant stoppedAt
    ) {

        public static Result create(Long taskId, Instant stoppedAt) {
            return new Result(taskId, stoppedAt);
        }
    }
}
