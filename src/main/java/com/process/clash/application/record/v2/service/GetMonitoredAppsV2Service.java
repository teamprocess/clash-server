package com.process.clash.application.record.v2.service;

import com.process.clash.application.record.policy.MonitoredAppPolicy;
import com.process.clash.application.record.v2.data.GetMonitoredAppsV2Data;
import com.process.clash.application.record.v2.port.in.GetMonitoredAppsV2UseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetMonitoredAppsV2Service implements GetMonitoredAppsV2UseCase {

    private final MonitoredAppPolicy monitoredAppPolicy;

    @Override
    public GetMonitoredAppsV2Data.Result execute(GetMonitoredAppsV2Data.Command command) {
        return GetMonitoredAppsV2Data.Result.from(monitoredAppPolicy.getMonitoredApps());
    }
}
