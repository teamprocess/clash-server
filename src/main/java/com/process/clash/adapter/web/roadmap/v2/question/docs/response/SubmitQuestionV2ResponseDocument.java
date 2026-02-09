package com.process.clash.adapter.web.roadmap.v2.question.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessResponseDocument;
import com.process.clash.adapter.web.roadmap.v2.question.dto.SubmitQuestionV2AnswerDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "문제 답안 제출 응답")
public class SubmitQuestionV2ResponseDocument extends SuccessResponseDocument {

    @Schema(description = "답안 제출 결과", implementation = SubmitQuestionV2AnswerDto.Response.class)
    public SubmitQuestionV2AnswerDto.Response data;
}
