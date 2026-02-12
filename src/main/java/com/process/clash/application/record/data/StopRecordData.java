package com.process.clash.application.record.data;

import com.process.clash.application.common.actor.Actor;
import java.time.Instant;

public class StopRecordData {

    public record Command(
            Actor actor
    ) {}

    public record Result(
            Instant stoppedAt,
            RecordSessionData.Session session
    ) {

        public static Result create(Instant stoppedAt, RecordSessionData.Session session) {
            return new Result(stoppedAt, session);
        }
    }
}
