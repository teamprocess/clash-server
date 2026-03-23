package com.process.clash.adapter.web.controller.docs.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Ping 응답")
public class PingResponseDocument {

    @Schema(description = "Ping 결과 메시지", example = "PONG")
    public String message;
}
