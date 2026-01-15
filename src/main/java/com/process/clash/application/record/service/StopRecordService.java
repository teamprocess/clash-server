package com.process.clash.application.record.service;

import com.process.clash.application.record.dto.StopRecordData;
import com.process.clash.application.record.exception.exception.notfound.ActiveSessionNotFound;
import com.process.clash.application.record.port.in.StopRecordUseCase;
import com.process.clash.application.record.port.out.StudySessionRepositoryPort;
import com.process.clash.domain.record.model.entity.StudySession;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class StopRecordService implements StopRecordUseCase {

    private final StudySessionRepositoryPort studySessionRepositoryPort;

    public StopRecordData.Result execute(StopRecordData.Command command) {
        StudySession studySession = studySessionRepositoryPort
                .findActiveSessionByUserId(command.actor().id())
                .orElseThrow(ActiveSessionNotFound::new);

        StudySession updatedSession = studySession.changeEndedAt(LocalDateTime.now());
        studySessionRepositoryPort.save(updatedSession);

        return StopRecordData.Result.create(
                updatedSession.task().id(),
                updatedSession.endedAt()
        );
    }
}
