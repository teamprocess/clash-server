package com.process.clash.adapter.web.compete.my.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessResponseDoc;
import com.process.clash.adapter.web.compete.my.dto.AnalyzeMyActivityDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "내 활동 분석 응답")
public class AnalyzeMyActivityResponseDoc extends SuccessResponseDoc {

    @Schema(description = "분석 결과", implementation = AnalyzeMyActivityDto.Response.class)
    public AnalyzeMyActivityDto.Response data;
}
