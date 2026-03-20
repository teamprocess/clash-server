package com.process.clash.application.health.data;

public class GetHealthStatusData {

    public record Result(
            String status,
            String db,
            String redis,
            String app,
            String message
    ) {}
}
