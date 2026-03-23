package com.process.clash.adapter.web.controller.docs.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "상세 헬스 체크 응답")
public class HealthResponseDocument {

    @Schema(description = "전체 상태", example = "UP", allowableValues = {"UP", "DOWN", "ERROR", "UNKNOWN"})
    public String status;

    @Schema(description = "DB 상태", example = "UP", allowableValues = {"UP", "DOWN", "ERROR", "UNKNOWN"}, nullable = true)
    public String db;

    @Schema(description = "Redis 상태", example = "UP", allowableValues = {"UP", "DOWN", "ERROR", "UNKNOWN"}, nullable = true)
    public String redis;

    @Schema(description = "애플리케이션 상태", example = "UP", allowableValues = {"UP", "DOWN", "ERROR", "UNKNOWN"}, nullable = true)
    public String app;

    @Schema(description = "오류 메시지", example = "Health endpoint access failed", nullable = true)
    public String message;
}
