package com.process.clash.adapter.web.roadmap.v2.choice.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessResponseDocument;
import com.process.clash.adapter.web.roadmap.v2.choice.dto.CreateChoiceV2Dto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "선택지 생성 응답")
public class CreateChoiceV2ResponseDocument extends SuccessResponseDocument {

    @Schema(description = "생성된 선택지 정보", implementation = CreateChoiceV2Dto.Response.class)
    public CreateChoiceV2Dto.Response data;
}
