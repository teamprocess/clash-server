package com.process.clash.adapter.web.roadmap.v2.question.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessResponseDocument;
import com.process.clash.adapter.web.roadmap.v2.question.dto.CreateQuestionV2Dto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "문제 생성 응답")
public class CreateQuestionV2ResponseDocument extends SuccessResponseDocument {

    @Schema(description = "생성된 문제 정보", implementation = CreateQuestionV2Dto.Response.class)
    public CreateQuestionV2Dto.Response data;
}
