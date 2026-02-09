package com.process.clash.adapter.web.roadmap.v2.question.docs.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "문제 답안 제출 요청")
public class SubmitQuestionV2RequestDocument {

    @Schema(description = "제출한 선택지 ID", example = "1")
    public Long submittedChoiceId;
}
