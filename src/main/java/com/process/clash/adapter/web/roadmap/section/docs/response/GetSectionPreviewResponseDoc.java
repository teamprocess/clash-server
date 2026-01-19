package com.process.clash.adapter.web.roadmap.section.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessResponseDoc;
import com.process.clash.adapter.web.roadmap.section.dto.GetSectionPreviewDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "로드맵 미리보기 응답")
public class GetSectionPreviewResponseDoc extends SuccessResponseDoc {

    @Schema(description = "로드맵 미리보기", implementation = GetSectionPreviewDto.Response.class)
    public GetSectionPreviewDto.Response data;
}
