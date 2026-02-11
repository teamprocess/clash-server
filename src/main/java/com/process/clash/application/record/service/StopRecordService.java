package com.process.clash.application.record.service;

import com.process.clash.application.record.data.StopRecordData;
import com.process.clash.application.record.exception.exception.notfound.ActiveSessionNotFound;
import com.process.clash.application.record.port.in.StopRecordUseCase;
import com.process.clash.application.record.port.out.RecordActivityNotifierPort;
import com.process.clash.application.record.port.out.StudySessionRepositoryPort;
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
    private final RecordActivityNotifierPort recordActivityNotifierPort;
    private final ZoneId recordZoneId;

    public StopRecordData.Result execute(StopRecordData.Command command) {
        StudySession studySession = studySessionRepositoryPort
                .findActiveSessionByUserId(command.actor().id())
                .orElseThrow(ActiveSessionNotFound::new);

        LocalDateTime endedAt = LocalDateTime.now(recordZoneId);
        StudySession updatedSession = studySession.changeEndedAt(endedAt);
        studySessionRepositoryPort.save(updatedSession);
        recordActivityNotifierPort.notifyActivityStopped(command.actor());

        return StopRecordData.Result.create(
                updatedSession.task().id(),
                endedAt.atZone(recordZoneId).toInstant()
        );
    }
}
