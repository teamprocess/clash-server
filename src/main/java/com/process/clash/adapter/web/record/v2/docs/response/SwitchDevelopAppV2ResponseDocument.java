package com.process.clash.adapter.web.record.v2.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessResponseDocument;
import com.process.clash.adapter.web.record.v2.dto.SwitchDevelopAppV2Dto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "V2 개발 앱 전환 응답")
public class SwitchDevelopAppV2ResponseDocument extends SuccessResponseDocument {

    @Schema(description = "개발 앱 전환 결과", implementation = SwitchDevelopAppV2Dto.Response.class)
    public SwitchDevelopAppV2Dto.Response data;
}
