package com.process.clash.adapter.web.record.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessResponseDoc;
import com.process.clash.adapter.web.record.dto.RecordSettingDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "일반 기록 설정 응답")
public class RecordSettingResponseDoc extends SuccessResponseDoc {

    @Schema(description = "설정 정보", implementation = RecordSettingDto.Response.class)
    public RecordSettingDto.Response data;
}
