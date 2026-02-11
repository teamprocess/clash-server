package com.process.clash.adapter.web.roadmap.chapter.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessResponseDocument;
import com.process.clash.adapter.web.roadmap.chapter.dto.GetChapterDetailsDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "챕터 상세 응답")
public class GetChapterDetailsResponseDocument extends SuccessResponseDocument {

    @Schema(description = "챕터 상세 정보", implementation = GetChapterDetailsDto.Response.class)
    public GetChapterDetailsDto.Response data;
}
