package com.process.clash.application.record.admin.service;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.record.admin.data.CutUserRecordAdminData;
import com.process.clash.application.record.admin.port.in.CutUserRecordAdminUseCase;
import com.process.clash.application.record.v2.port.out.RecordActivityNotifierPort;
import com.process.clash.application.record.v2.exception.exception.notfound.ActiveSessionV2NotFoundException;
import com.process.clash.application.record.v2.port.out.RecordDevelopSessionSegmentV2RepositoryPort;
import com.process.clash.application.record.v2.port.out.RecordSessionV2RepositoryPort;
import com.process.clash.application.record.v2.service.RecordSessionV2Mapper;
import com.process.clash.domain.record.v2.entity.RecordSessionV2;
import com.process.clash.domain.record.v2.enums.RecordSessionTypeV2;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CutUserRecordAdminService implements CutUserRecordAdminUseCase {

    private final RecordSessionV2RepositoryPort recordSessionV2RepositoryPort;
    private final RecordDevelopSessionSegmentV2RepositoryPort recordDevelopSessionSegmentV2RepositoryPort;
    private final RecordActivityNotifierPort recordActivityNotifierPort;

    @Override
    public CutUserRecordAdminData.Result execute(CutUserRecordAdminData.Command command) {
        RecordSessionV2 activeSession = recordSessionV2RepositoryPort
            .findActiveSessionByUserIdForUpdate(command.userId())
            .orElseThrow(ActiveSessionV2NotFoundException::new);

        Instant endedAt = Instant.now();
        if (activeSession.sessionType() == RecordSessionTypeV2.DEVELOP) {
            recordDevelopSessionSegmentV2RepositoryPort.findOpenSegmentBySessionIdForUpdate(activeSession.id())
                .ifPresent(segment -> recordDevelopSessionSegmentV2RepositoryPort.save(segment.changeEndedAt(endedAt)));
        }

        RecordSessionV2 savedSession = recordSessionV2RepositoryPort.save(activeSession.changeEndedAt(endedAt));
        recordActivityNotifierPort.notifyActivityStopped(new Actor(activeSession.userId()));

        log.info("어드민에 의해 기록 강제 중단. userId={}, sessionId={}", activeSession.userId(), activeSession.id());

        return CutUserRecordAdminData.Result.from(
            endedAt,
            RecordSessionV2Mapper.toSession(savedSession)
        );
    }
}