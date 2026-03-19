package com.process.clash.adapter.web.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record HealthResponse(
        String status,
        String db,
        String redis,
        String app,
        String message
) {}
