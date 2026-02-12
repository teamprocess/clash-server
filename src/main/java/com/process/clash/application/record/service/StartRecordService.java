package com.process.clash.application.record.service;

import com.process.clash.application.record.data.StartRecordData;
import com.process.clash.application.record.exception.exception.badrequest.InvalidRecordStartRequestException;
import com.process.clash.application.record.exception.exception.conflict.StudySessionAlreadyStartedException;
import com.process.clash.application.record.exception.exception.notfound.TaskNotFoundException;
import com.process.clash.application.record.policy.MonitoredAppPolicy;
import com.process.clash.application.record.policy.TaskPolicy;
import com.process.clash.application.record.port.in.StartRecordUseCase;
import com.process.clash.application.record.port.out.RecordActivityNotifierPort;
import com.process.clash.application.record.port.out.StudySessionRepositoryPort;
import com.process.clash.application.record.port.out.TaskRepositoryPort;
import com.process.clash.application.user.user.exception.exception.notfound.UserNotFoundException;
import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import com.process.clash.domain.record.entity.StudySession;
import com.process.clash.domain.record.entity.Task;
import com.process.clash.domain.record.enums.RecordType;
import com.process.clash.domain.user.user.entity.User;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class StartRecordService implements StartRecordUseCase {

    private final StudySessionRepositoryPort studySessionRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;
    private final TaskRepositoryPort taskRepositoryPort;
    private final TaskPolicy taskPolicy;
    private final MonitoredAppPolicy monitoredAppPolicy;
    private final RecordActivityNotifierPort recordActivityNotifierPort;
    private final ZoneId recordZoneId;

    @Override
    public StartRecordData.Result execute(StartRecordData.Command command) {

        User user = userRepositoryPort.findById(command.actor().id())
                .orElseThrow(UserNotFoundException::new);

        Boolean existsActiveSession = studySessionRepositoryPort.existsActiveSessionByUserId(
                command.actor().id()
        );

        if(existsActiveSession) throw new StudySessionAlreadyStartedException();

        RecordType recordType = resolveRecordType(command);
        LocalDateTime startedAt = LocalDateTime.now(recordZoneId);
        StudySession newStudySession = createStudySession(command, user, recordType, startedAt);
        StudySession savedSession = studySessionRepositoryPort.save(newStudySession);
        recordActivityNotifierPort.notifyActivityStarted(command.actor());

        return StartRecordData.Result.from(
                startedAt.atZone(recordZoneId).toInstant(),
                RecordSessionMapper.toSession(savedSession, recordZoneId)
        );
    }

    private StudySession createStudySession(
        StartRecordData.Command command,
        User user,
        RecordType recordType,
        LocalDateTime startedAt
    ) {
        if (recordType == RecordType.TASK) {
            validateTaskStartRequest(command);
            Task task = taskRepositoryPort.findById(command.taskId())
                .orElseThrow(TaskNotFoundException::new);
            taskPolicy.validateOwnership(command.actor(), task);
            return StudySession.create(
                null,
                user,
                task,
                RecordType.TASK,
                null,
                startedAt,
                null
            );
        }

        validateActivityStartRequest(command);
        String appName = command.appName().trim();
        monitoredAppPolicy.validate(appName);
        return StudySession.createActivity(
            null,
            user,
            appName,
            startedAt,
            null
        );
    }

    private RecordType resolveRecordType(StartRecordData.Command command) {
        if (command.recordType() != null) {
            return command.recordType();
        }

        if (command.taskId() != null && isBlank(command.appName())) {
            return RecordType.TASK;
        }

        if (command.taskId() == null && !isBlank(command.appName())) {
            return RecordType.ACTIVITY;
        }

        throw new InvalidRecordStartRequestException();
    }

    private void validateTaskStartRequest(StartRecordData.Command command) {
        if (command.taskId() == null || !isBlank(command.appName())) {
            throw new InvalidRecordStartRequestException();
        }
    }

    private void validateActivityStartRequest(StartRecordData.Command command) {
        if (command.taskId() != null || isBlank(command.appName())) {
            throw new InvalidRecordStartRequestException();
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
