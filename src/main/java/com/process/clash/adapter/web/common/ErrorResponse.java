package com.process.clash.adapter.web.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.process.clash.application.common.exception.statuscode.StatusCode;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Map;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
        String code,
        String message,
        LocalDateTime timestamp,
        Map<String, String> details
) {

    public static ErrorResponse of(StatusCode statusCode) {
        return ErrorResponse.builder()
                .code(statusCode.getCode())
                .message(statusCode.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static ErrorResponse of(
            StatusCode statusCode,
            Map<String, String> details
    ) {
        return ErrorResponse.builder()
                .code(statusCode.getCode())
                .message(statusCode.getMessage())
                .timestamp(LocalDateTime.now())
                .details(details)
                .build();
    }
}
