package com.process.clash.application.record.v2.port.in;

import com.process.clash.application.record.v2.data.GetMonitoredAppsV2Data;

public interface GetMonitoredAppsV2UseCase {

    GetMonitoredAppsV2Data.Result execute(GetMonitoredAppsV2Data.Command command);
}
