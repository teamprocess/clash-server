package com.process.clash.application.record.data;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.domain.record.enums.RecordType;
import java.time.Instant;

public class StartRecordData {

    public record Command(
        RecordType recordType,
        Long taskId,
        String appName,
        Actor actor
    ) {}

    public record Result(
        Instant startedAt,
        RecordSessionData.Session session
    ) {

        public static Result from(Instant startedAt, RecordSessionData.Session session) {
            return new Result(startedAt, session);
        }
    }
}
