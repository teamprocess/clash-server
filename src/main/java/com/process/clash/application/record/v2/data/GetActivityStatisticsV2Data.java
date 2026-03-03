package com.process.clash.application.record.v2.data;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.domain.common.enums.PeriodCategory;
import java.util.List;

public class GetActivityStatisticsV2Data {

    public record Command(
        Actor actor,
        PeriodCategory duration
    ) {
    }

    public record Result(
        List<AppActivity> apps
    ) {
        public static Result from(List<AppActivity> apps) {
            return new Result(apps);
        }
    }

    public record AppActivity(
        String appId,
        Integer studyTime
    ) {
    }
}
