package com.process.clash.adapter.web.roadmap.section.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessResponseDoc;
import com.process.clash.adapter.web.roadmap.section.dto.GetSectionDetailsDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "로드맵 상세 조회 응답")
public class GetSectionDetailsResponseDoc extends SuccessResponseDoc {

    @Schema(description = "로드맵 상세", implementation = GetSectionDetailsDto.Response.class)
    public GetSectionDetailsDto.Response data;
}
