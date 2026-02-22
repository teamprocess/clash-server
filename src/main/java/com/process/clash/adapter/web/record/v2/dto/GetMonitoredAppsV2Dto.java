package com.process.clash.adapter.web.record.v2.dto;

import com.process.clash.application.record.v2.data.GetMonitoredAppsV2Data;
import com.process.clash.domain.record.enums.MonitoredApp;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public class GetMonitoredAppsV2Dto {

    @Schema(name = "GetMonitoredAppsV2DtoResponse")
    public record Response(
        List<MonitoredApp> apps
    ) {
        public static Response from(GetMonitoredAppsV2Data.Result result) {
            return new Response(result.apps());
        }
    }
}
