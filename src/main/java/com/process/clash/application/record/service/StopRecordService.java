package com.process.clash.application.record.service;

import com.process.clash.application.record.data.StopRecordData;
import com.process.clash.application.record.exception.exception.notfound.ActiveSessionNotFound;
import com.process.clash.application.record.port.in.StopRecordUseCase;
import com.process.clash.application.record.port.out.RecordActivitySegmentRepositoryPort;
import com.process.clash.application.record.port.out.RecordActivityNotifierPort;
import com.process.clash.application.record.port.out.StudySessionRepositoryPort;
import com.process.clash.domain.record.enums.RecordType;
import com.process.clash.domain.record.entity.StudySession;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class StopRecordService implements StopRecordUseCase {

    private final StudySessionRepositoryPort studySessionRepositoryPort;
    private final RecordActivitySegmentRepositoryPort recordActivitySegmentRepositoryPort;
    private final RecordActivityNotifierPort recordActivityNotifierPort;
    private final ZoneId recordZoneId;

    public StopRecordData.Result execute(StopRecordData.Command command) {
        StudySession studySession = studySessionRepositoryPort
                .findActiveSessionByUserIdForUpdate(command.actor().id())
                .orElseThrow(ActiveSessionNotFound::new);

        LocalDateTime endedAt = LocalDateTime.now(recordZoneId);
        if (studySession.recordType() == RecordType.ACTIVITY) {
            recordActivitySegmentRepositoryPort.findOpenSegmentBySessionIdForUpdate(studySession.id())
                .ifPresent(segment -> recordActivitySegmentRepositoryPort.save(segment.changeEndedAt(endedAt)));
        }
        StudySession updatedSession = studySession.changeEndedAt(endedAt);
        StudySession savedSession = studySessionRepositoryPort.save(updatedSession);
        recordActivityNotifierPort.notifyActivityStopped(command.actor());

        return StopRecordData.Result.create(
                endedAt.atZone(recordZoneId).toInstant(),
                RecordSessionMapper.toSession(savedSession, recordZoneId)
        );
    }
}
