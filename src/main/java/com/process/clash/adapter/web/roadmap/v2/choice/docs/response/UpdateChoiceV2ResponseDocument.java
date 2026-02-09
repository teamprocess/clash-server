package com.process.clash.adapter.web.roadmap.v2.choice.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessResponseDocument;
import com.process.clash.adapter.web.roadmap.v2.choice.dto.UpdateChoiceV2Dto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "선택지 수정 응답")
public class UpdateChoiceV2ResponseDocument extends SuccessResponseDocument {

    @Schema(description = "수정된 선택지 정보", implementation = UpdateChoiceV2Dto.Response.class)
    public UpdateChoiceV2Dto.Response data;
}
