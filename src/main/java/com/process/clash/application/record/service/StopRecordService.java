package com.process.clash.application.record.service;

import com.process.clash.application.record.data.StopRecordData;
import com.process.clash.application.record.exception.exception.notfound.ActiveSessionNotFound;
import com.process.clash.application.record.port.in.StopRecordUseCase;
import com.process.clash.application.record.port.out.RecordSessionSegmentRepositoryPort;
import com.process.clash.application.record.port.out.RecordActivityNotifierPort;
import com.process.clash.application.record.port.out.RecordSessionRepositoryPort;
import com.process.clash.application.user.exp.service.StudyTimeExpGrantService;
import com.process.clash.domain.record.enums.RecordType;
import com.process.clash.domain.record.entity.RecordSession;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.time.ZoneId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class StopRecordService implements StopRecordUseCase {

    private final RecordSessionRepositoryPort recordSessionRepositoryPort;
    private final RecordSessionSegmentRepositoryPort recordSessionSegmentRepositoryPort;
    private final RecordActivityNotifierPort recordActivityNotifierPort;
    private final StudyTimeExpGrantService studyTimeExpGrantService;
    private final ZoneId recordZoneId;

    public StopRecordData.Result execute(StopRecordData.Command command) {
        RecordSession recordSession = recordSessionRepositoryPort
                .findActiveSessionByUserIdForUpdate(command.actor().id())
                .orElseThrow(ActiveSessionNotFound::new);

        Instant endedAt = Instant.now();
        if (recordSession.recordType() == RecordType.ACTIVITY) {
            recordSessionSegmentRepositoryPort.findOpenSegmentBySessionIdForUpdate(recordSession.id())
                .ifPresent(segment -> recordSessionSegmentRepositoryPort.save(segment.changeEndedAt(endedAt)));
        }
        RecordSession updatedSession = recordSession.changeEndedAt(endedAt);
        RecordSession savedSession = recordSessionRepositoryPort.save(updatedSession);
        recordActivityNotifierPort.notifyActivityStopped(command.actor());

        try {
            studyTimeExpGrantService.grant(recordSession.user().id(), recordSession.startedAt(), endedAt);
        } catch (Exception e) {
            log.error("학습시간 EXP 지급 실패. userId={}", recordSession.user().id(), e);
        }

        return StopRecordData.Result.create(
                endedAt,
                RecordSessionMapper.toSession(savedSession, recordZoneId)
        );
    }
}
