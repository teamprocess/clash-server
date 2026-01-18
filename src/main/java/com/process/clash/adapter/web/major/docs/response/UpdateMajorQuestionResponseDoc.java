package com.process.clash.adapter.web.major.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessResponseDoc;
import com.process.clash.adapter.web.major.dto.UpdateMajorQuestionDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "전공 질문 수정 응답")
public class UpdateMajorQuestionResponseDoc extends SuccessResponseDoc {

    @Schema(description = "수정된 질문", implementation = UpdateMajorQuestionDto.Response.class)
    public UpdateMajorQuestionDto.Response data;
}
