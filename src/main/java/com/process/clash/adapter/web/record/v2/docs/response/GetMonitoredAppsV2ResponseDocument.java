package com.process.clash.adapter.web.record.v2.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessResponseDocument;
import com.process.clash.adapter.web.record.v2.dto.GetMonitoredAppsV2Dto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "V2 개발 앱 목록 조회 응답")
public class GetMonitoredAppsV2ResponseDocument extends SuccessResponseDocument {

    @Schema(description = "개발 앱 목록", implementation = GetMonitoredAppsV2Dto.Response.class)
    public GetMonitoredAppsV2Dto.Response data;
}
