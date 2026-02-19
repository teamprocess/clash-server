package com.process.clash.application.record.service;

import com.process.clash.application.record.data.StopRecordData;
import com.process.clash.application.record.exception.exception.notfound.ActiveSessionNotFound;
import com.process.clash.application.record.port.in.StopRecordUseCase;
import com.process.clash.application.record.port.out.RecordSessionSegmentRepositoryPort;
import com.process.clash.application.record.port.out.RecordActivityNotifierPort;
import com.process.clash.application.record.port.out.RecordSessionRepositoryPort;
import com.process.clash.domain.record.enums.RecordType;
import com.process.clash.domain.record.entity.RecordSession;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.time.ZoneId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class StopRecordService implements StopRecordUseCase {

    private final RecordSessionRepositoryPort recordSessionRepositoryPort;
    private final RecordSessionSegmentRepositoryPort recordSessionSegmentRepositoryPort;
    private final RecordActivityNotifierPort recordActivityNotifierPort;
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

        return StopRecordData.Result.create(
                endedAt,
                RecordSessionMapper.toSession(savedSession, recordZoneId)
        );
    }
}
