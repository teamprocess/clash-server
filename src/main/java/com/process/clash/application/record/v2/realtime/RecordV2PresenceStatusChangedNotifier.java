package com.process.clash.application.record.v2.realtime;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.realtime.data.UserActivityStatus;
import com.process.clash.application.realtime.port.out.NotifyPresenceStatusChangedPort;
import com.process.clash.application.record.v2.port.out.RecordActivityNotifierPort;
import com.process.clash.application.record.v2.port.out.RecordDevelopSessionSegmentV2RepositoryPort;
import com.process.clash.application.record.v2.port.out.RecordSessionV2RepositoryPort;
import com.process.clash.application.user.exp.service.StudyTimeExpGrantService;
import com.process.clash.domain.record.v2.entity.RecordSessionV2;
import com.process.clash.domain.record.v2.enums.RecordSessionTypeV2;
import jakarta.transaction.Transactional;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Transactional
@RequiredArgsConstructor
@Slf4j
public class RecordV2PresenceStatusChangedNotifier implements NotifyPresenceStatusChangedPort {

    private final RecordSessionV2RepositoryPort recordSessionV2RepositoryPort;
    private final RecordDevelopSessionSegmentV2RepositoryPort recordDevelopSessionSegmentV2RepositoryPort;
    private final RecordActivityNotifierPort recordActivityNotifierPort;
    private final StudyTimeExpGrantService studyTimeExpGrantService;

    @Override
    public void notifyStatusChanged(
        Long userId,
        UserActivityStatus previousStatus,
        UserActivityStatus currentStatus
    ) {
        if (userId == null || previousStatus == null || currentStatus == null) {
            return;
        }
        if (!isSessionStoppingTransition(previousStatus, currentStatus)) {
            return;
        }

        recordSessionV2RepositoryPort.findActiveSessionByUserIdForUpdate(userId)
            .ifPresent(activeSession -> stopActiveSessionIfNeeded(activeSession, currentStatus));
    }

    private boolean isSessionStoppingTransition(
        UserActivityStatus previousStatus,
        UserActivityStatus currentStatus
    ) {
        if (previousStatus == currentStatus) {
            return false;
        }

        return currentStatus == UserActivityStatus.AWAY || currentStatus == UserActivityStatus.OFFLINE;
    }

    private void stopActiveSessionIfNeeded(
        RecordSessionV2 activeSession,
        UserActivityStatus currentStatus
    ) {
        if (currentStatus == UserActivityStatus.AWAY && activeSession.sessionType() == RecordSessionTypeV2.TASK) {
            return;
        }

        Instant endedAt = Instant.now();
        if (activeSession.sessionType() == RecordSessionTypeV2.DEVELOP) {
            recordDevelopSessionSegmentV2RepositoryPort.findOpenSegmentBySessionIdForUpdate(activeSession.id())
                .ifPresent(segment -> recordDevelopSessionSegmentV2RepositoryPort.save(segment.changeEndedAt(endedAt)));
        }
        recordSessionV2RepositoryPort.save(activeSession.changeEndedAt(endedAt));
        recordActivityNotifierPort.notifyActivityStopped(new Actor(activeSession.userId()));
        try {
            studyTimeExpGrantService.grant(activeSession.userId(), activeSession.startedAt(), endedAt);
        } catch (Exception e) {
            log.error("학습시간 EXP 지급 실패 (presence auto stop). userId={}", activeSession.userId(), e);
        }
    }
}
