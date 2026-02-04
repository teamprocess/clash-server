package com.process.clash.adapter.web.roadmap.v2.chapter.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessResponseDoc;
import com.process.clash.adapter.web.roadmap.v2.chapter.dto.UpdateChapterV2Dto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "챕터 수정 응답")
public class UpdateChapterV2ResponseDoc extends SuccessResponseDoc {

    @Schema(description = "수정된 챕터 정보", implementation = UpdateChapterV2Dto.Response.class)
    public UpdateChapterV2Dto.Response data;
}
