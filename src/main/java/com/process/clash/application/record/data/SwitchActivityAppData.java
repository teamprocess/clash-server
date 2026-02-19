package com.process.clash.application.record.data;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.domain.record.enums.MonitoredApp;
import java.time.Instant;

public class SwitchActivityAppData {

    public record Command(
        Actor actor,
        MonitoredApp appId
    ) {}

    public record Result(
        Instant switchedAt,
        RecordSessionData.Session session
    ) {
        public static Result from(Instant switchedAt, RecordSessionData.Session session) {
            return new Result(switchedAt, session);
        }
    }
}
