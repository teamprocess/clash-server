package com.process.clash.application.record.data;

import com.process.clash.application.common.actor.Actor;

public class GetCurrentRecordData {

    public record Command(
        Actor actor
    ) {}

    public record Result(
        RecordSessionData.Session session
    ) {
        public static Result from(RecordSessionData.Session session) {
            return new Result(session);
        }
    }
}
