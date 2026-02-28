package com.process.clash.adapter.web.record.v2.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessResponseDocument;
import com.process.clash.adapter.web.record.v2.dto.UpdateTaskCompletionV2Dto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "V2 세부 작업 완료 상태 변경 응답")
public class UpdateTaskCompletionV2ResponseDocument extends SuccessResponseDocument {

    @Schema(description = "완료 상태가 변경된 세부 작업", implementation = UpdateTaskCompletionV2Dto.Response.class)
    public UpdateTaskCompletionV2Dto.Response data;
}
