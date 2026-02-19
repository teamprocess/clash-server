package com.process.clash.application.record.data;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.domain.record.enums.MonitoredApp;
import java.util.List;

public class GetMonitoredAppsData {

    public record Command(
        Actor actor
    ) {}

    public record Result(
        List<MonitoredApp> apps
    ) {
        public static Result from(List<MonitoredApp> apps) {
            return new Result(apps);
        }
    }
}
