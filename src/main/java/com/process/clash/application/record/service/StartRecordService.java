package com.process.clash.application.record.service;

import com.process.clash.application.record.data.StartRecordData;
import com.process.clash.application.record.exception.exception.badrequest.InvalidRecordStartRequestException;
import com.process.clash.application.record.exception.exception.conflict.RecordSessionAlreadyStartedException;
import com.process.clash.application.record.exception.exception.notfound.TaskNotFoundException;
import com.process.clash.application.record.policy.MonitoredAppPolicy;
import com.process.clash.application.record.policy.TaskPolicy;
import com.process.clash.application.record.port.in.StartRecordUseCase;
import com.process.clash.application.record.port.out.RecordSessionSegmentRepositoryPort;
import com.process.clash.application.record.port.out.RecordActivityNotifierPort;
import com.process.clash.application.record.port.out.RecordSessionRepositoryPort;
import com.process.clash.application.record.port.out.RecordTaskRepositoryPort;
import com.process.clash.application.user.user.exception.exception.notfound.UserNotFoundException;
import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import com.process.clash.domain.record.entity.RecordSession;
import com.process.clash.domain.record.entity.RecordTask;
import com.process.clash.domain.record.entity.RecordSessionSegment;
import com.process.clash.domain.record.enums.MonitoredApp;
import com.process.clash.domain.record.enums.RecordType;
import com.process.clash.domain.user.user.entity.User;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.time.ZoneId;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class StartRecordService implements StartRecordUseCase {

    private final RecordSessionRepositoryPort recordSessionRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;
    private final RecordTaskRepositoryPort taskRepositoryPort;
    private final TaskPolicy taskPolicy;
    private final MonitoredAppPolicy monitoredAppPolicy;
    private final RecordSessionSegmentRepositoryPort recordSessionSegmentRepositoryPort;
    private final RecordActivityNotifierPort recordActivityNotifierPort;
    private final ZoneId recordZoneId;

    @Override
    public StartRecordData.Result execute(StartRecordData.Command command) {

        User user = userRepositoryPort.findById(command.actor().id())
            .orElseThrow(UserNotFoundException::new);

        Boolean existsActiveSession = recordSessionRepositoryPort.existsActiveSessionByUserId(
                command.actor().id()
        );

        if(existsActiveSession) throw new RecordSessionAlreadyStartedException();

        RecordType recordType = resolveRecordType(command);
        Instant startedAt = Instant.now();
        RecordSession savedSession;
        try {
            RecordSession newRecordSession = createRecordSession(command, user, recordType, startedAt);
            savedSession = recordSessionRepositoryPort.save(newRecordSession);
            if (savedSession.recordType() == RecordType.ACTIVITY && savedSession.appId() != null) {
                recordSessionSegmentRepositoryPort.save(
                    RecordSessionSegment.start(
                        savedSession.id(),
                        savedSession.appId(),
                        startedAt
                    )
                );
            }
            recordActivityNotifierPort.notifyActivityStarted(command.actor());
        } catch (DataIntegrityViolationException exception) {
            throw new RecordSessionAlreadyStartedException(exception);
        }

        return StartRecordData.Result.from(
                startedAt,
                RecordSessionMapper.toSession(savedSession, recordZoneId)
        );
    }

    private RecordSession createRecordSession(
        StartRecordData.Command command,
        User user,
        RecordType recordType,
        Instant startedAt
    ) {
        if (recordType == RecordType.TASK) {
            validateTaskStartRequest(command);
            RecordTask task = taskRepositoryPort.findById(command.taskId())
                .orElseThrow(TaskNotFoundException::new);
            taskPolicy.validateOwnership(command.actor(), task);
            return RecordSession.create(
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
        MonitoredApp appId = command.appId();
        monitoredAppPolicy.validate(appId);
        return RecordSession.createActivity(
            null,
            user,
            appId,
            startedAt,
            null
        );
    }

    private RecordType resolveRecordType(StartRecordData.Command command) {
        if (command.recordType() != null) {
            return command.recordType();
        }

        if (command.taskId() != null && command.appId() == null) {
            return RecordType.TASK;
        }

        if (command.taskId() == null && command.appId() != null) {
            return RecordType.ACTIVITY;
        }

        throw new InvalidRecordStartRequestException();
    }

    private void validateTaskStartRequest(StartRecordData.Command command) {
        if (command.taskId() == null || command.appId() != null) {
            throw new InvalidRecordStartRequestException();
        }
    }

    private void validateActivityStartRequest(StartRecordData.Command command) {
        if (command.taskId() != null || command.appId() == null) {
            throw new InvalidRecordStartRequestException();
        }
    }
}
