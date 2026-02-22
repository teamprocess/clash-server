package com.process.clash.application.record.v2.service;

import com.process.clash.application.record.policy.MonitoredAppPolicy;
import com.process.clash.application.record.port.out.RecordActivityNotifierPort;
import com.process.clash.application.record.v2.data.StartRecordV2Data;
import com.process.clash.application.record.v2.exception.exception.badrequest.InvalidRecordV2StartRequestException;
import com.process.clash.application.record.v2.exception.exception.conflict.RecordSessionV2AlreadyStartedException;
import com.process.clash.application.record.v2.exception.exception.notfound.SubjectV2NotFoundException;
import com.process.clash.application.record.v2.exception.exception.notfound.TaskV2NotFoundException;
import com.process.clash.application.record.v2.policy.SubjectV2Policy;
import com.process.clash.application.record.v2.port.in.StartRecordV2UseCase;
import com.process.clash.application.record.v2.port.out.RecordDevelopSessionSegmentV2RepositoryPort;
import com.process.clash.application.record.v2.port.out.RecordSessionV2RepositoryPort;
import com.process.clash.application.record.v2.port.out.RecordSubjectV2RepositoryPort;
import com.process.clash.application.record.v2.port.out.RecordTaskV2RepositoryPort;
import com.process.clash.application.user.user.exception.exception.notfound.UserNotFoundException;
import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import com.process.clash.domain.record.enums.MonitoredApp;
import com.process.clash.domain.record.v2.entity.RecordDevelopSessionSegmentV2;
import com.process.clash.domain.record.v2.entity.RecordSessionV2;
import com.process.clash.domain.record.v2.entity.RecordSubjectV2;
import com.process.clash.domain.record.v2.entity.RecordTaskV2;
import com.process.clash.domain.record.v2.enums.RecordSessionTypeV2;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.time.ZoneId;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class StartRecordV2Service implements StartRecordV2UseCase {

    private final RecordSessionV2RepositoryPort recordSessionV2RepositoryPort;
    private final RecordSubjectV2RepositoryPort recordSubjectV2RepositoryPort;
    private final RecordTaskV2RepositoryPort recordTaskV2RepositoryPort;
    private final RecordDevelopSessionSegmentV2RepositoryPort recordDevelopSessionSegmentV2RepositoryPort;
    private final UserRepositoryPort userRepositoryPort;
    private final SubjectV2Policy subjectV2Policy;
    private final MonitoredAppPolicy monitoredAppPolicy;
    private final RecordActivityNotifierPort recordActivityNotifierPort;
    private final ZoneId recordZoneId;

    @Override
    public StartRecordV2Data.Result execute(StartRecordV2Data.Command command) {
        userRepositoryPort.findById(command.actor().id())
            .orElseThrow(UserNotFoundException::new);

        if (recordSessionV2RepositoryPort.existsActiveSessionByUserId(command.actor().id())) {
            throw new RecordSessionV2AlreadyStartedException();
        }

        RecordSessionTypeV2 sessionType = resolveSessionType(command);
        Instant startedAt = Instant.now();
        RecordSessionV2 savedSession;
        try {
            RecordSessionV2 newSession = createSession(command, sessionType, startedAt);
            savedSession = recordSessionV2RepositoryPort.save(newSession);

            if (savedSession.sessionType() == RecordSessionTypeV2.DEVELOP && savedSession.appId() != null) {
                recordDevelopSessionSegmentV2RepositoryPort.save(
                    RecordDevelopSessionSegmentV2.start(
                        savedSession.id(),
                        savedSession.appId(),
                        startedAt
                    )
                );
            }
            recordActivityNotifierPort.notifyActivityStarted(command.actor());
        } catch (DataIntegrityViolationException exception) {
            throw new RecordSessionV2AlreadyStartedException(exception);
        }

        return StartRecordV2Data.Result.from(
            startedAt,
            RecordSessionV2Mapper.toSession(savedSession, recordZoneId)
        );
    }

    private RecordSessionV2 createSession(
        StartRecordV2Data.Command command,
        RecordSessionTypeV2 sessionType,
        Instant startedAt
    ) {
        if (sessionType == RecordSessionTypeV2.TASK) {
            validateTaskStartRequest(command);

            RecordSubjectV2 subject = recordSubjectV2RepositoryPort.findById(command.subjectId())
                .orElseThrow(SubjectV2NotFoundException::new);
            subjectV2Policy.validateOwnership(command.actor(), subject);

            RecordTaskV2 task = null;
            if (command.taskId() != null) {
                task = recordTaskV2RepositoryPort.findByIdAndSubjectId(command.taskId(), subject.id())
                    .orElseThrow(TaskV2NotFoundException::new);
            }

            return RecordSessionV2.createTask(
                command.actor().id(),
                subject.id(),
                subject.name(),
                task == null ? null : task.id(),
                task == null ? null : task.name(),
                startedAt
            );
        }

        validateDevelopStartRequest(command);
        MonitoredApp appId = command.appId();
        monitoredAppPolicy.validate(appId);
        return RecordSessionV2.createDevelop(command.actor().id(), appId, startedAt);
    }

    private RecordSessionTypeV2 resolveSessionType(StartRecordV2Data.Command command) {
        if (command.sessionType() != null) {
            return command.sessionType();
        }

        if (command.subjectId() != null && command.appId() == null) {
            return RecordSessionTypeV2.TASK;
        }
        if (command.subjectId() == null && command.taskId() == null && command.appId() != null) {
            return RecordSessionTypeV2.DEVELOP;
        }

        throw new InvalidRecordV2StartRequestException();
    }

    private void validateTaskStartRequest(StartRecordV2Data.Command command) {
        if (command.subjectId() == null || command.appId() != null) {
            throw new InvalidRecordV2StartRequestException();
        }
    }

    private void validateDevelopStartRequest(StartRecordV2Data.Command command) {
        if (command.subjectId() != null || command.taskId() != null || command.appId() == null) {
            throw new InvalidRecordV2StartRequestException();
        }
    }
}
