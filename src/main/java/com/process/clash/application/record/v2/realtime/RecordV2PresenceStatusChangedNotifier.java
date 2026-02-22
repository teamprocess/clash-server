package com.process.clash.application.record.v2.realtime;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.realtime.data.UserActivityStatus;
import com.process.clash.application.realtime.port.out.NotifyPresenceStatusChangedPort;
import com.process.clash.application.record.port.out.RecordActivityNotifierPort;
import com.process.clash.application.record.v2.port.out.RecordDevelopSessionSegmentV2RepositoryPort;
import com.process.clash.application.record.v2.port.out.RecordSessionV2RepositoryPort;
import com.process.clash.domain.record.v2.entity.RecordSessionV2;
import com.process.clash.domain.record.v2.enums.RecordSessionTypeV2;
import jakarta.transaction.Transactional;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Transactional
@RequiredArgsConstructor
public class RecordV2PresenceStatusChangedNotifier implements NotifyPresenceStatusChangedPort {

    private final RecordSessionV2RepositoryPort recordSessionV2RepositoryPort;
    private final RecordDevelopSessionSegmentV2RepositoryPort recordDevelopSessionSegmentV2RepositoryPort;
    private final RecordActivityNotifierPort recordActivityNotifierPort;

    @Override
    public void notifyStatusChanged(
        Long userId,
        UserActivityStatus previousStatus,
        UserActivityStatus currentStatus
    ) {
        if (userId == null || previousStatus == null || currentStatus == null) {
            return;
        }
        if (!shouldStopDevelopSession(previousStatus, currentStatus)) {
            return;
        }

        recordSessionV2RepositoryPort.findActiveSessionByUserIdForUpdate(userId)
            .ifPresent(this::stopDevelopSessionIfNeeded);
    }

    private boolean shouldStopDevelopSession(
        UserActivityStatus previousStatus,
        UserActivityStatus currentStatus
    ) {
        if (previousStatus == currentStatus) {
            return false;
        }

        // 이슈 #309: 자리비움/오프라인 전환 시 DEVELOP 세션은 유지되면 안 된다.
        return currentStatus == UserActivityStatus.AWAY || currentStatus == UserActivityStatus.OFFLINE;
    }

    private void stopDevelopSessionIfNeeded(RecordSessionV2 activeSession) {
        // TASK 세션은 제외하고 DEVELOP 세션만 자동 종료한다.
        if (activeSession.sessionType() != RecordSessionTypeV2.DEVELOP) {
            return;
        }

        Instant endedAt = Instant.now();
        recordDevelopSessionSegmentV2RepositoryPort.findOpenSegmentBySessionIdForUpdate(activeSession.id())
            .ifPresent(segment -> recordDevelopSessionSegmentV2RepositoryPort.save(segment.changeEndedAt(endedAt)));
        recordSessionV2RepositoryPort.save(activeSession.changeEndedAt(endedAt));
        recordActivityNotifierPort.notifyActivityStopped(new Actor(activeSession.userId()));
    }
}
