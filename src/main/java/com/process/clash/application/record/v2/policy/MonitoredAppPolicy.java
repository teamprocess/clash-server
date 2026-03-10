package com.process.clash.application.record.v2.policy;

import com.process.clash.application.record.v2.exception.exception.badrequest.InvalidMonitoredAppV2Exception;
import com.process.clash.domain.record.enums.MonitoredApp;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class MonitoredAppPolicy {

    private static final List<MonitoredApp> MONITORED_APPS = List.of(MonitoredApp.values());

    public List<MonitoredApp> getMonitoredApps() {
        return MONITORED_APPS;
    }

    public void validate(MonitoredApp appId) {
        if (appId == null) {
            throw new InvalidMonitoredAppV2Exception();
        }
    }
}