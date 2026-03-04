package com.process.clash.application.record.v2.service;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.record.port.out.RecordActivityNotifierPort;
import com.process.clash.application.record.v2.port.out.RecordSessionV2RepositoryPort;
import com.process.clash.application.user.exp.service.StudyTimeExpGrantService;
import com.process.clash.domain.record.v2.entity.RecordSessionV2;
import jakarta.transaction.Transactional;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class RecordV2TaskSessionTimeoutService {

    private static final Duration TASK_SESSION_MAX_DURATION = Duration.ofHours(3);

    private final RecordSessionV2RepositoryPort recordSessionV2RepositoryPort;
    private final RecordActivityNotifierPort recordActivityNotifierPort;
    private final StudyTimeExpGrantService studyTimeExpGrantService;

    public int stopExpiredTaskSessions() {
        Instant endedAt = Instant.now();
        Instant startedBeforeInclusive = endedAt.minus(TASK_SESSION_MAX_DURATION);
        List<RecordSessionV2> expiredTaskSessions = recordSessionV2RepositoryPort
            .findActiveTaskSessionsStartedBeforeForUpdate(startedBeforeInclusive);

        for (RecordSessionV2 expiredTaskSession : expiredTaskSessions) {
            recordSessionV2RepositoryPort.save(expiredTaskSession.changeEndedAt(endedAt));
            recordActivityNotifierPort.notifyActivityStopped(new Actor(expiredTaskSession.userId()));
            try {
                studyTimeExpGrantService.grant(expiredTaskSession.userId(), expiredTaskSession.startedAt(), endedAt);
            } catch (Exception e) {
                log.error("학습시간 EXP 지급 실패 (task timeout). userId={}", expiredTaskSession.userId(), e);
            }
        }

        return expiredTaskSessions.size();
    }
}
