package com.process.clash.adapter.web.record.dto;

import com.process.clash.application.record.data.GetMonitoredAppsData;
import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;

public class GetMonitoredAppsDto {

    @Schema(name = "GetMonitoredAppsDtoResponse")
    public record Response(
        List<String> apps
    ) {
        public static Response from(GetMonitoredAppsData.Result result) {
            return new Response(result.apps());
        }
    }
}
