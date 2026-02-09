package com.process.clash.adapter.web.roadmap.v2.question.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessResponseDocument;
import com.process.clash.adapter.web.roadmap.v2.question.dto.GetChapterV2ResultDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "챕터 결과 조회 응답")
public class GetChapterV2ResultResponseDocument extends SuccessResponseDocument {

    @Schema(description = "챕터 결과 정보", implementation = GetChapterV2ResultDto.Response.class)
    public GetChapterV2ResultDto.Response data;
}
