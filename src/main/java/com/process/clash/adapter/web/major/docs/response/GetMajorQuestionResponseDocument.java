package com.process.clash.adapter.web.major.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessResponseDocument;
import com.process.clash.adapter.web.major.dto.GetMajorQuestionDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "전공 성향 검사 질문 조회 응답")
public class GetMajorQuestionResponseDocument extends SuccessResponseDocument {

    @Schema(description = "질문 목록", implementation = GetMajorQuestionDto.Response.class)
    public GetMajorQuestionDto.Response data;
}
