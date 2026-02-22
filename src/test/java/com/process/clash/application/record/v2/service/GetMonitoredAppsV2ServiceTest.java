package com.process.clash.application.record.v2.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.record.policy.MonitoredAppPolicy;
import com.process.clash.application.record.v2.data.GetMonitoredAppsV2Data;
import com.process.clash.domain.record.enums.MonitoredApp;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class GetMonitoredAppsV2ServiceTest {

    private final GetMonitoredAppsV2Service getMonitoredAppsV2Service =
        new GetMonitoredAppsV2Service(new MonitoredAppPolicy());

    @Test
    @DisplayName("모니터링 앱 목록을 반환한다")
    void execute_returnsMonitoredApps() {
        GetMonitoredAppsV2Data.Result result = getMonitoredAppsV2Service.execute(
            new GetMonitoredAppsV2Data.Command(new Actor(1L))
        );

        assertThat(result.apps()).contains(MonitoredApp.VSCODE, MonitoredApp.INTELLIJ_IDEA, MonitoredApp.XCODE);
    }
}
