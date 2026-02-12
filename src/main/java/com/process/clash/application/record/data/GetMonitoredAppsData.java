package com.process.clash.application.record.data;

import com.process.clash.application.common.actor.Actor;
import java.util.List;

public class GetMonitoredAppsData {

    public record Command(
        Actor actor
    ) {}

    public record Result(
        List<String> apps
    ) {
        public static Result from(List<String> apps) {
            return new Result(apps);
        }
    }
}
