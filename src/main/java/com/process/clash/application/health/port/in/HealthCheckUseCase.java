package com.process.clash.application.health.port.in;

import com.process.clash.application.health.data.GetHealthStatusData;
import com.process.clash.application.health.data.PingData;

public interface HealthCheckUseCase {
    GetHealthStatusData.Result getHealthStatus();

    PingData.Result ping();
}
