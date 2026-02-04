package com.process.clash.adapter.web.roadmap.v2.question.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessResponseDoc;
import com.process.clash.adapter.web.roadmap.v2.question.dto.UpdateQuestionV2Dto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "문제 수정 응답")
public class UpdateQuestionV2ResponseDoc extends SuccessResponseDoc {

    @Schema(description = "수정된 문제 정보", implementation = UpdateQuestionV2Dto.Response.class)
    public UpdateQuestionV2Dto.Response data;
}
