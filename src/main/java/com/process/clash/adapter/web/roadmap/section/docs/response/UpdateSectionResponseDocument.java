package com.process.clash.adapter.web.roadmap.section.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessResponseDocument;
import com.process.clash.adapter.web.roadmap.section.dto.UpdateSectionDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "로드맵 수정 응답")
public class UpdateSectionResponseDocument extends SuccessResponseDocument {

    @Schema(description = "수정된 로드맵", implementation = UpdateSectionDto.Response.class)
    public UpdateSectionDto.Response data;
}
