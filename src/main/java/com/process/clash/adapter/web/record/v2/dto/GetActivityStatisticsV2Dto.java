package com.process.clash.adapter.web.record.v2.dto;

import com.process.clash.application.record.v2.data.GetActivityStatisticsV2Data;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public class GetActivityStatisticsV2Dto {

    @Schema(name = "GetActivityStatisticsV2DtoResponse")
    public record Response(
        List<AppActivity> apps
    ) {
        public static Response from(GetActivityStatisticsV2Data.Result result) {
            return new Response(
                result.apps().stream().map(AppActivity::from).toList()
            );
        }
    }

    public record AppActivity(
        String appId,
        Integer studyTime
    ) {
        public static AppActivity from(GetActivityStatisticsV2Data.AppActivity app) {
            return new AppActivity(
                app.appId(),
                app.studyTime()
            );
        }
    }
}
