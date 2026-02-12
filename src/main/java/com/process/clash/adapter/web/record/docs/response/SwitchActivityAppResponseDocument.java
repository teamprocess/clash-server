package com.process.clash.adapter.web.record.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessResponseDocument;
import com.process.clash.adapter.web.record.dto.SwitchActivityAppDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "활동 앱 전환 응답")
public class SwitchActivityAppResponseDocument extends SuccessResponseDocument {

    @Schema(description = "활동 앱 전환 결과", implementation = SwitchActivityAppDto.Response.class)
    public SwitchActivityAppDto.Response data;
}
