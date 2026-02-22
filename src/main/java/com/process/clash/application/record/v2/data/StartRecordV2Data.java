package com.process.clash.application.record.v2.data;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.domain.record.enums.MonitoredApp;
import com.process.clash.domain.record.v2.enums.RecordSessionTypeV2;
import java.time.Instant;

public class StartRecordV2Data {

    public record Command(
        RecordSessionTypeV2 sessionType,
        Long subjectId,
        Long taskId,
        MonitoredApp appId,
        Actor actor
    ) {
    }

    public record Result(
        Instant startedAt,
        RecordSessionV2Data.Session session
    ) {
        public static Result from(Instant startedAt, RecordSessionV2Data.Session session) {
            return new Result(startedAt, session);
        }
    }
}
