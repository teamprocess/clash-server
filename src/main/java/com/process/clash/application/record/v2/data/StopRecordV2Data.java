package com.process.clash.application.record.v2.data;

import com.process.clash.application.common.actor.Actor;
import java.time.Instant;

public class StopRecordV2Data {

    public record Command(
        Actor actor
    ) {
    }

    public record Result(
        Instant stoppedAt,
        RecordSessionV2Data.Session session
    ) {
        public static Result from(Instant stoppedAt, RecordSessionV2Data.Session session) {
            return new Result(stoppedAt, session);
        }
    }
}
