package com.process.clash.application.record.service;

import com.process.clash.application.record.data.GetMonitoredAppsData;
import com.process.clash.application.record.policy.MonitoredAppPolicy;
import com.process.clash.application.record.port.in.GetMonitoredAppsUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetMonitoredAppsService implements GetMonitoredAppsUseCase {

    private final MonitoredAppPolicy monitoredAppPolicy;

    @Override
    public GetMonitoredAppsData.Result execute(GetMonitoredAppsData.Command command) {
        return GetMonitoredAppsData.Result.from(monitoredAppPolicy.getMonitoredApps());
    }
}
