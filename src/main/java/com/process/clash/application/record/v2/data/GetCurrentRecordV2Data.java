package com.process.clash.application.record.v2.data;

import com.process.clash.application.common.actor.Actor;

public class GetCurrentRecordV2Data {

    public record Command(
        Actor actor
    ) {
    }

    public record Result(
        RecordSessionV2Data.Session session
    ) {
        public static Result from(RecordSessionV2Data.Session session) {
            return new Result(session);
        }
    }
}
