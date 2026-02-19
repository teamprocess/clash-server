package com.process.clash.adapter.web.record.docs.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "활동 앱 전환 요청")
public class SwitchActivityAppRequestDocument {

    @Schema(description = "활동 기록 앱 ID", example = "INTELLIJ_IDEA")
    public String appId;
}
