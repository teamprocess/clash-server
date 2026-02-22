package com.process.clash.adapter.web.record.v2.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessResponseDocument;
import com.process.clash.adapter.web.record.v2.dto.UpdateSubjectTaskV2Dto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "V2 세부 작업 수정 응답")
public class UpdateSubjectTaskV2ResponseDocument extends SuccessResponseDocument {

    @Schema(description = "수정된 세부 작업", implementation = UpdateSubjectTaskV2Dto.Response.class)
    public UpdateSubjectTaskV2Dto.Response data;
}
