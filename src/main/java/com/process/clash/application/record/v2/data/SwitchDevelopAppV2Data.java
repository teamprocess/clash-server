package com.process.clash.application.record.v2.data;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.domain.record.enums.MonitoredApp;
import java.time.Instant;

public class SwitchDevelopAppV2Data {

    public record Command(
        Actor actor,
        MonitoredApp appId
    ) {
    }

    public record Result(
        Instant switchedAt,
        RecordSessionV2Data.Session session
    ) {
        public static Result from(Instant switchedAt, RecordSessionV2Data.Session session) {
            return new Result(switchedAt, session);
        }
    }
}
