package com.process.clash.adapter.web.record.v2.docs.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "V2 개발 앱 전환 요청")
public class SwitchDevelopAppV2RequestDocument {

    @Schema(description = "개발 기록 앱 ID", example = "INTELLIJ_IDEA")
    public String appId;
}
