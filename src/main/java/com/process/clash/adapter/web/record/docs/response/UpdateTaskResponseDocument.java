package com.process.clash.adapter.web.record.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessResponseDocument;
import com.process.clash.adapter.web.record.dto.UpdateTaskDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "과목 수정 응답")
public class UpdateTaskResponseDocument extends SuccessResponseDocument {

    @Schema(description = "수정된 과목", implementation = UpdateTaskDto.Response.class)
    public UpdateTaskDto.Response data;
}
