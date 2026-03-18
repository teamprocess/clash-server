package com.process.clash.application.record.v2.service;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.record.v2.data.UpdateTaskCompletionV2Data;
import com.process.clash.application.record.v2.exception.exception.notfound.TaskV2NotFoundException;
import com.process.clash.application.record.v2.port.in.UpdateTaskCompletionV2UseCase;
import com.process.clash.application.record.v2.port.out.RecordActivityNotifierPort;
import com.process.clash.application.record.v2.port.out.RecordDevelopSessionSegmentV2RepositoryPort;
import com.process.clash.application.record.v2.port.out.RecordSessionV2RepositoryPort;
import com.process.clash.application.record.v2.port.out.RecordTaskV2RepositoryPort;
import com.process.clash.application.user.exp.service.StudyTimeExpGrantService;
import com.process.clash.domain.record.v2.entity.RecordSessionV2;
import com.process.clash.domain.record.v2.entity.RecordTaskV2;
import com.process.clash.domain.record.v2.enums.RecordSessionTypeV2;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UpdateTaskCompletionV2Service implements UpdateTaskCompletionV2UseCase {

    private final RecordTaskV2RepositoryPort recordTaskV2RepositoryPort;
    private final RecordSessionV2RepositoryPort recordSessionV2RepositoryPort;
    private final RecordDevelopSessionSegmentV2RepositoryPort recordDevelopSessionSegmentV2RepositoryPort;
    private final RecordActivityNotifierPort recordActivityNotifierPort;
    private final StudyTimeExpGrantService studyTimeExpGrantService;

    @Override
    public UpdateTaskCompletionV2Data.Result execute(UpdateTaskCompletionV2Data.Command command) {
        RecordTaskV2 task = recordTaskV2RepositoryPort.findByIdAndUserId(command.taskId(), command.actor().id())
            .orElseThrow(TaskV2NotFoundException::new);
        RecordTaskV2 savedTask = recordTaskV2RepositoryPort.save(task.changeCompleted(command.completed()));
        if (command.completed()) {
            stopActiveTaskSessionIfNeeded(command.actor(), task.id());
        }

        return UpdateTaskCompletionV2Data.Result.from(savedTask);
    }

    private void stopActiveTaskSessionIfNeeded(Actor actor, Long taskId) {
        recordSessionV2RepositoryPort.findActiveSessionByUserIdForUpdate(actor.id())
            .filter(activeSession -> Objects.equals(activeSession.taskId(), taskId))
            .ifPresent(activeSession -> stopActiveSession(activeSession, actor));
    }

    private void stopActiveSession(RecordSessionV2 activeSession, Actor actor) {
        Instant endedAt = Instant.now();
        if (activeSession.sessionType() == RecordSessionTypeV2.DEVELOP) {
            recordDevelopSessionSegmentV2RepositoryPort.findOpenSegmentBySessionIdForUpdate(activeSession.id())
                .ifPresent(segment -> recordDevelopSessionSegmentV2RepositoryPort.save(segment.changeEndedAt(endedAt)));
        }

        recordSessionV2RepositoryPort.save(activeSession.changeEndedAt(endedAt));
        recordActivityNotifierPort.notifyActivityStopped(actor);
        try {
            studyTimeExpGrantService.grant(activeSession.userId(), activeSession.startedAt(), endedAt);
        } catch (Exception e) {
            log.error("학습시간 EXP 지급 실패 (task completion auto stop). userId={}", activeSession.userId(), e);
        }
    }
}
