package com.process.clash.application.record.data;

import com.process.clash.application.common.actor.Actor;
import java.time.Instant;

public class StartRecordData {

    public record Command(
        Long taskId,
        Actor actor
    ) {}

    public record Result(
        Instant startedAt
    ) {

        public static Result from(Instant startedAt) {
            return new Result(startedAt);
        }
    }
}
