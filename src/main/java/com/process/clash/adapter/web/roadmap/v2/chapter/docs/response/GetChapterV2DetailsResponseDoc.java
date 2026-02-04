package com.process.clash.adapter.web.roadmap.v2.chapter.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessResponseDoc;
import com.process.clash.adapter.web.roadmap.v2.chapter.dto.GetChapterV2DetailsDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "챕터 상세 조회 응답 (학습 자료 URL 포함)")
public class GetChapterV2DetailsResponseDoc extends SuccessResponseDoc {

    @Schema(description = "챕터 상세 정보 (학습 자료 URL 포함)", implementation = GetChapterV2DetailsDto.Response.class)
    public GetChapterV2DetailsDto.Response data;
}
