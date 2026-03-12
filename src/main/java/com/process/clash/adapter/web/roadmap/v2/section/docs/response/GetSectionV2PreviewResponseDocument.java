package com.process.clash.adapter.web.roadmap.v2.section.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessResponseDocument;
import com.process.clash.adapter.web.roadmap.v2.section.dto.GetSectionV2PreviewDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "로드맵 미리보기 조회 응답 (V2)")
public class GetSectionV2PreviewResponseDocument extends SuccessResponseDocument {

    @Schema(description = "로드맵 미리보기 정보", implementation = GetSectionV2PreviewDto.Response.class)
    public GetSectionV2PreviewDto.Response data;
}