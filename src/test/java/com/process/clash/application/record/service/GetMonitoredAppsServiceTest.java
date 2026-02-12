package com.process.clash.application.record.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.record.data.GetMonitoredAppsData;
import com.process.clash.application.record.policy.MonitoredAppPolicy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class GetMonitoredAppsServiceTest {

    private final GetMonitoredAppsService getMonitoredAppsService =
        new GetMonitoredAppsService(new MonitoredAppPolicy());

    @Test
    @DisplayName("모니터링 앱 목록을 반환한다")
    void execute_returnsMonitoredApps() {
        GetMonitoredAppsData.Result result = getMonitoredAppsService.execute(
            new GetMonitoredAppsData.Command(new Actor(1L))
        );

        assertThat(result.apps()).contains("Code", "Visual Studio Code", "Xcode");
    }
}
