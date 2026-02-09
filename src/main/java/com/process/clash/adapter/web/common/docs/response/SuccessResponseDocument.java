package com.process.clash.adapter.web.common.docs.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "성공 응답 공통 형식")
public class SuccessResponseDocument {

    @Schema(description = "성공 여부")
    public Boolean success = true;

    @Schema(description = "응답 메시지")
    public String message;
}
