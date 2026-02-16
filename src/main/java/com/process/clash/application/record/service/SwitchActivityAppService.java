package com.process.clash.application.record.service;

import com.process.clash.application.record.data.SwitchActivityAppData;
import com.process.clash.application.record.exception.exception.badrequest.InvalidActivitySwitchRequestException;
import com.process.clash.application.record.exception.exception.notfound.ActiveSessionNotFound;
import com.process.clash.application.record.policy.MonitoredAppPolicy;
import com.process.clash.application.record.port.in.SwitchActivityAppUseCase;
import com.process.clash.application.record.port.out.RecordActivitySegmentRepositoryPort;
import com.process.clash.application.record.port.out.StudySessionRepositoryPort;
import com.process.clash.domain.record.entity.RecordActivitySegment;
import com.process.clash.domain.record.entity.StudySession;
import com.process.clash.domain.record.enums.RecordType;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class SwitchActivityAppService implements SwitchActivityAppUseCase {

    private final StudySessionRepositoryPort studySessionRepositoryPort;
    private final RecordActivitySegmentRepositoryPort recordActivitySegmentRepositoryPort;
    private final MonitoredAppPolicy monitoredAppPolicy;
    private final ZoneId recordZoneId;

    @Override
    public SwitchActivityAppData.Result execute(SwitchActivityAppData.Command command) {
        String nextAppName = normalizeAppName(command.appName());
        monitoredAppPolicy.validate(nextAppName);

        StudySession activeSession = loadActiveActivitySession(command.actor().id());
        Instant switchedAt = Instant.now();

        if (nextAppName.equals(activeSession.appName())) {
            return toResult(activeSession, switchedAt);
        }

        Optional<RecordActivitySegment> openSegment = findOpenSegment(activeSession.id());
        if (isSameAppAsOpenSegment(openSegment, nextAppName)) {
            StudySession syncedSession = syncSessionAppName(activeSession, nextAppName);
            return toResult(syncedSession, switchedAt);
        }

        closeOpenSegment(openSegment, switchedAt);
        createOpenSegment(activeSession.id(), nextAppName, switchedAt);
        StudySession switchedSession = syncSessionAppName(activeSession, nextAppName);

        return toResult(switchedSession, switchedAt);
    }

    private String normalizeAppName(String appName) {
        if (appName == null || appName.isBlank()) {
            throw new InvalidActivitySwitchRequestException();
        }
        return appName.trim();
    }

    private StudySession loadActiveActivitySession(Long userId) {
        StudySession activeSession = studySessionRepositoryPort.findActiveSessionByUserIdForUpdate(userId)
            .orElseThrow(ActiveSessionNotFound::new);
        if (activeSession.recordType() != RecordType.ACTIVITY) {
            throw new InvalidActivitySwitchRequestException();
        }
        return activeSession;
    }

    private Optional<RecordActivitySegment> findOpenSegment(Long sessionId) {
        return recordActivitySegmentRepositoryPort.findOpenSegmentBySessionIdForUpdate(sessionId);
    }

    private boolean isSameAppAsOpenSegment(Optional<RecordActivitySegment> openSegment, String appName) {
        return openSegment.isPresent() && appName.equals(openSegment.get().appName());
    }

    private void closeOpenSegment(Optional<RecordActivitySegment> openSegment, Instant closedAt) {
        openSegment.ifPresent(segment ->
            recordActivitySegmentRepositoryPort.save(segment.changeEndedAt(closedAt))
        );
    }

    private void createOpenSegment(Long sessionId, String appName, Instant startedAt) {
        recordActivitySegmentRepositoryPort.save(
            RecordActivitySegment.start(sessionId, appName, startedAt)
        );
    }

    private StudySession syncSessionAppName(StudySession activeSession, String appName) {
        return studySessionRepositoryPort.save(activeSession.changeActivityAppName(appName));
    }

    private SwitchActivityAppData.Result toResult(StudySession session, Instant switchedAt) {
        return SwitchActivityAppData.Result.from(
            switchedAt,
            RecordSessionMapper.toSession(session, recordZoneId)
        );
    }
}
