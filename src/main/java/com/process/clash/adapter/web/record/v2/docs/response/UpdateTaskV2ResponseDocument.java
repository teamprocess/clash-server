package com.process.clash.adapter.web.record.v2.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessResponseDocument;
import com.process.clash.adapter.web.record.v2.dto.UpdateTaskV2Dto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "V2 세부 작업 수정 응답")
public class UpdateTaskV2ResponseDocument extends SuccessResponseDocument {

    @Schema(description = "수정된 세부 작업", implementation = UpdateTaskV2Dto.Response.class)
    public UpdateTaskV2Dto.Response data;
}
