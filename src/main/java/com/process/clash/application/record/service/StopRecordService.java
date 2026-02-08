package com.process.clash.application.record.service;

import com.process.clash.application.record.data.StopRecordData;
import com.process.clash.application.record.exception.exception.notfound.ActiveSessionNotFound;
import com.process.clash.application.record.port.in.StopRecordUseCase;
import com.process.clash.application.record.port.out.StudySessionRepositoryPort;
import com.process.clash.application.record.realtime.PublishToIncludedGroups;
import com.process.clash.application.realtime.data.ChangeType;
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
    private final PublishToIncludedGroups publishToIncludedGroups;
    private final ZoneId recordZoneId;

    public StopRecordData.Result execute(StopRecordData.Command command) {
        StudySession studySession = studySessionRepositoryPort
                .findActiveSessionByUserId(command.actor().id())
                .orElseThrow(ActiveSessionNotFound::new);

        LocalDateTime endedAt = LocalDateTime.now(recordZoneId);
        StudySession updatedSession = studySession.changeEndedAt(endedAt);
        studySessionRepositoryPort.save(updatedSession);
        publishToIncludedGroups.publish(command.actor(), ChangeType.ACTIVITY_STOPPED);

        return StopRecordData.Result.create(
                updatedSession.task().id(),
                endedAt.atZone(recordZoneId).toInstant()
        );
    }
}
