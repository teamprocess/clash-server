package com.process.clash.application.realtime.port.out;

import com.process.clash.application.realtime.data.UserActivityStatus;

public interface NotifyPresenceStatusChangedPort {

    void notifyStatusChanged(
        Long userId,
        UserActivityStatus previousStatus,
        UserActivityStatus currentStatus
    );
}
