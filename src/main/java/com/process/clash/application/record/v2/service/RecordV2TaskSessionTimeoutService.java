package com.process.clash.application.record.v2.service;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.record.port.out.RecordActivityNotifierPort;
import com.process.clash.application.record.v2.port.out.RecordSessionV2RepositoryPort;
import com.process.clash.domain.record.v2.entity.RecordSessionV2;
import jakarta.transaction.Transactional;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class RecordV2TaskSessionTimeoutService {

    private static final Duration TASK_SESSION_MAX_DURATION = Duration.ofHours(3);

    private final RecordSessionV2RepositoryPort recordSessionV2RepositoryPort;
    private final RecordActivityNotifierPort recordActivityNotifierPort;

    public int stopExpiredTaskSessions() {
        Instant endedAt = Instant.now();
        Instant startedBeforeInclusive = endedAt.minus(TASK_SESSION_MAX_DURATION);
        List<RecordSessionV2> expiredTaskSessions = recordSessionV2RepositoryPort
            .findActiveTaskSessionsStartedBeforeForUpdate(startedBeforeInclusive);

        for (RecordSessionV2 expiredTaskSession : expiredTaskSessions) {
            recordSessionV2RepositoryPort.save(expiredTaskSession.changeEndedAt(endedAt));
            recordActivityNotifierPort.notifyActivityStopped(new Actor(expiredTaskSession.userId()));
        }

        return expiredTaskSessions.size();
    }
}
