package com.process.clash.adapter.web.record.v2.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessResponseDocument;
import com.process.clash.adapter.web.record.v2.dto.GetActivityStatisticsV2Dto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "V2 앱별 활동 시간 조회 응답")
public class GetActivityStatisticsV2ResponseDocument extends SuccessResponseDocument {

    @Schema(description = "앱별 활동 시간", implementation = GetActivityStatisticsV2Dto.Response.class)
    public GetActivityStatisticsV2Dto.Response data;
}
