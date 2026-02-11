package com.process.clash.application.compete.realtime;

import com.process.clash.application.compete.rival.rival.port.out.RivalRepositoryPort;
import com.process.clash.application.realtime.data.UserActivityStatus;
import com.process.clash.application.realtime.port.out.NotifyPresenceStatusChangedPort;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RivalPresenceStatusChangedNotifier implements NotifyPresenceStatusChangedPort {

    private final RivalRepositoryPort rivalRepositoryPort;
    private final CompeteRefetchNotifier competeRefetchNotifier;

    @Override
    public void notifyStatusChanged(
        Long userId,
        UserActivityStatus previousStatus,
        UserActivityStatus currentStatus
    ) {
        if (userId == null || previousStatus == null || currentStatus == null) {
            return;
        }
        if (previousStatus == currentStatus) {
            return;
        }

        List<Long> rivalUserIds = rivalRepositoryPort.findOpponentIdByUserId(userId);
        if (rivalUserIds == null || rivalUserIds.isEmpty()) {
            return;
        }

        competeRefetchNotifier.notifyRivalStatusChanged(rivalUserIds);
    }
}
