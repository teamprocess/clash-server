package com.process.clash.application.record.port.in;

import com.process.clash.application.record.data.GetMonitoredAppsData;

public interface GetMonitoredAppsUseCase {
    GetMonitoredAppsData.Result execute(GetMonitoredAppsData.Command command);
}
