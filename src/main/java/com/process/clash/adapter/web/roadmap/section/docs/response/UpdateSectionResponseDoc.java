package com.process.clash.adapter.web.roadmap.section.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessResponseDoc;
import com.process.clash.adapter.web.roadmap.section.dto.UpdateSectionDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "로드맵 수정 응답")
public class UpdateSectionResponseDoc extends SuccessResponseDoc {

    @Schema(description = "수정된 로드맵", implementation = UpdateSectionDto.Response.class)
    public UpdateSectionDto.Response data;
}
