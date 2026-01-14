package com.process.clash.application.record.service;

import com.process.clash.application.record.dto.StartRecordData;
import com.process.clash.application.record.exception.exception.conflict.StudySessionAlreadyStartedException;
import com.process.clash.application.record.exception.exception.notfound.TaskNotFoundException;
import com.process.clash.application.record.port.in.StartRecordUseCase;
import com.process.clash.application.record.port.out.StudySessionRepositoryPort;
import com.process.clash.application.record.port.out.TaskRepositoryPort;
import com.process.clash.application.user.user.exception.exception.notfound.UserNotFoundException;
import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import com.process.clash.domain.record.model.entity.StudySession;
import com.process.clash.domain.record.model.entity.Task;
import com.process.clash.domain.user.user.entity.User;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class StartRecordService implements StartRecordUseCase {

    private final StudySessionRepositoryPort studySessionRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;
    private final TaskRepositoryPort taskRepositoryPort;

    @Override
    public StartRecordData.Result execute(StartRecordData.Command command) {

        User user = userRepositoryPort.findById(command.actor().id())
                .orElseThrow(UserNotFoundException::new);

        Task task = taskRepositoryPort.findById(command.taskId())
                .orElseThrow(TaskNotFoundException::new);

        Boolean existsActiveSession = studySessionRepositoryPort.existsActiveSessionByUserId(
                command.actor().id()
        );

        if(existsActiveSession) throw new StudySessionAlreadyStartedException();

        StudySession newStudySession = StudySession.create(
                null,
                user,
                task,
                LocalDateTime.now(),
                null
        );

        studySessionRepositoryPort.save(newStudySession);

        return StartRecordData.Result.from(
                newStudySession.startedAt().atZone(ZoneOffset.UTC).toInstant()
        );
    }
}
