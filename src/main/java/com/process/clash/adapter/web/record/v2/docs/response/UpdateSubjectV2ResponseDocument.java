package com.process.clash.adapter.web.record.v2.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessResponseDocument;
import com.process.clash.adapter.web.record.v2.dto.UpdateSubjectV2Dto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "V2 과목 그룹 수정 응답")
public class UpdateSubjectV2ResponseDocument extends SuccessResponseDocument {

    @Schema(description = "수정된 과목 그룹", implementation = UpdateSubjectV2Dto.Response.class)
    public UpdateSubjectV2Dto.Response data;
}
