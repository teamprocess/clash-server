package com.process.clash.adapter.web.major.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessResponseDocument;
import com.process.clash.adapter.web.major.dto.PostMajorQuestionDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "전공 질문 생성 응답")
public class PostMajorQuestionResponseDocument extends SuccessResponseDocument {

    @Schema(description = "생성된 질문", implementation = PostMajorQuestionDto.Response.class)
    public PostMajorQuestionDto.Response data;
}
