package com.process.clash.application.record.policy;

import com.process.clash.application.record.exception.exception.badrequest.InvalidMonitoredAppException;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class MonitoredAppPolicy {

    private static final List<String> MONITORED_APPS = List.of(
        "Code",
        "Visual Studio Code",
        "WebStorm",
        "IntelliJ IDEA",
        "IntelliJ IDEA Ultimate",
        "IntelliJ IDEA Community",
        "PyCharm",
        "PyCharm Professional",
        "GoLand",
        "PhpStorm",
        "RubyMine",
        "CLion",
        "DataGrip",
        "Rider",
        "Android Studio",
        "Xcode"
    );

    public List<String> getMonitoredApps() {
        return MONITORED_APPS;
    }

    public void validate(String appName) {
        if (!MONITORED_APPS.contains(appName)) {
            throw new InvalidMonitoredAppException();
        }
    }
}
