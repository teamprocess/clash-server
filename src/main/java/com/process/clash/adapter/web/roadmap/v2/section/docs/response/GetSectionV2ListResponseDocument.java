package com.process.clash.adapter.web.roadmap.v2.section.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessResponseDocument;
import com.process.clash.adapter.web.roadmap.v2.section.dto.GetSectionV2ListDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "로드맵 목록 조회 응답 (V2)")
public class GetSectionV2ListResponseDocument extends SuccessResponseDocument {

    @Schema(description = "로드맵 목록 (챕터 포함)", implementation = GetSectionV2ListDto.Response.class)
    public GetSectionV2ListDto.Response data;
}
