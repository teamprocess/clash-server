package com.process.clash.adapter.web.record.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessResponseDocument;
import com.process.clash.adapter.web.record.dto.GetMonitoredAppsDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "활동 기록 앱 목록 조회 응답")
public class GetMonitoredAppsResponseDocument extends SuccessResponseDocument {

    @Schema(description = "활동 기록 앱 목록", implementation = GetMonitoredAppsDto.Response.class)
    public GetMonitoredAppsDto.Response data;
}
