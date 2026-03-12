package com.process.clash.adapter.web.roadmap.v2.section.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessResponseDocument;
import com.process.clash.adapter.web.roadmap.v2.section.dto.GetSectionV2DetailsDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "로드맵 상세 조회 응답 (V2)")
public class GetSectionV2DetailsResponseDocument extends SuccessResponseDocument {

    @Schema(description = "로드맵 상세 정보 (챕터 목록 포함)", implementation = GetSectionV2DetailsDto.Response.class)
    public GetSectionV2DetailsDto.Response data;
}
