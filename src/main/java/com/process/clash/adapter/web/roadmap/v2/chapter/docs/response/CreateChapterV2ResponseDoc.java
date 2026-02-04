package com.process.clash.adapter.web.roadmap.v2.chapter.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessResponseDoc;
import com.process.clash.adapter.web.roadmap.v2.chapter.dto.CreateChapterV2Dto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "챕터 생성 응답")
public class CreateChapterV2ResponseDoc extends SuccessResponseDoc {

    @Schema(description = "생성된 챕터 정보", implementation = CreateChapterV2Dto.Response.class)
    public CreateChapterV2Dto.Response data;
}
