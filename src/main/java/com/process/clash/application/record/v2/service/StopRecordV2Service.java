package com.process.clash.application.record.v2.service;

import com.process.clash.application.record.port.out.RecordActivityNotifierPort;
import com.process.clash.application.record.v2.data.StopRecordV2Data;
import com.process.clash.application.record.v2.exception.exception.notfound.ActiveSessionV2NotFoundException;
import com.process.clash.application.record.v2.port.in.StopRecordV2UseCase;
import com.process.clash.application.record.v2.port.out.RecordDevelopSessionSegmentV2RepositoryPort;
import com.process.clash.application.record.v2.port.out.RecordSessionV2RepositoryPort;
import com.process.clash.application.user.exp.service.StudyTimeExpGrantService;
import com.process.clash.domain.record.v2.entity.RecordSessionV2;
import com.process.clash.domain.record.v2.enums.RecordSessionTypeV2;
import jakarta.transaction.Transactional;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class StopRecordV2Service implements StopRecordV2UseCase {

    private final RecordSessionV2RepositoryPort recordSessionV2RepositoryPort;
    private final RecordDevelopSessionSegmentV2RepositoryPort recordDevelopSessionSegmentV2RepositoryPort;
    private final RecordActivityNotifierPort recordActivityNotifierPort;
    private final StudyTimeExpGrantService studyTimeExpGrantService;

    @Override
    public StopRecordV2Data.Result execute(StopRecordV2Data.Command command) {
        RecordSessionV2 activeSession = recordSessionV2RepositoryPort
            .findActiveSessionByUserIdForUpdate(command.actor().id())
            .orElseThrow(ActiveSessionV2NotFoundException::new);

        Instant endedAt = Instant.now();
        if (activeSession.sessionType() == RecordSessionTypeV2.DEVELOP) {
            recordDevelopSessionSegmentV2RepositoryPort.findOpenSegmentBySessionIdForUpdate(activeSession.id())
                .ifPresent(segment -> recordDevelopSessionSegmentV2RepositoryPort.save(segment.changeEndedAt(endedAt)));
        }

        RecordSessionV2 savedSession = recordSessionV2RepositoryPort.save(activeSession.changeEndedAt(endedAt));
        recordActivityNotifierPort.notifyActivityStopped(command.actor());

        try {
            studyTimeExpGrantService.grant(activeSession.userId(), activeSession.startedAt(), endedAt);
        } catch (Exception e) {
            log.error("학습시간 EXP 지급 실패. userId={}", activeSession.userId(), e);
        }

        return StopRecordV2Data.Result.from(
            endedAt,
            RecordSessionV2Mapper.toSession(savedSession)
        );
    }
}
