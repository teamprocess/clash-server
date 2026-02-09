package com.process.clash.adapter.web.roadmap.section.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessResponseDocument;
import com.process.clash.adapter.web.roadmap.section.dto.CreateSectionDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "로드맵 생성 응답")
public class CreateSectionResponseDocument extends SuccessResponseDocument {

    @Schema(description = "생성된 로드맵", implementation = CreateSectionDto.Response.class)
    public CreateSectionDto.Response data;
}
