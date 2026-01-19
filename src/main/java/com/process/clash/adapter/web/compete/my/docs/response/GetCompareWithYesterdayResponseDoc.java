package com.process.clash.adapter.web.compete.my.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessResponseDoc;
import com.process.clash.adapter.web.compete.my.dto.GetCompareWithYesterdayDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "어제와 비교 응답")
public class GetCompareWithYesterdayResponseDoc extends SuccessResponseDoc {

    @Schema(description = "비교 결과", implementation = GetCompareWithYesterdayDto.Response.class)
    public GetCompareWithYesterdayDto.Response data;
}
