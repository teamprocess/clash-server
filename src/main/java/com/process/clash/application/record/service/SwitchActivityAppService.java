package com.process.clash.application.record.service;

import com.process.clash.application.record.data.SwitchActivityAppData;
import com.process.clash.application.record.exception.exception.badrequest.InvalidActivitySwitchRequestException;
import com.process.clash.application.record.exception.exception.notfound.ActiveSessionNotFound;
import com.process.clash.application.record.policy.MonitoredAppPolicy;
import com.process.clash.application.record.port.in.SwitchActivityAppUseCase;
import com.process.clash.application.record.port.out.RecordSessionSegmentRepositoryPort;
import com.process.clash.application.record.port.out.RecordSessionRepositoryPort;
import com.process.clash.domain.record.entity.RecordSessionSegment;
import com.process.clash.domain.record.entity.RecordSession;
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

    private final RecordSessionRepositoryPort recordSessionRepositoryPort;
    private final RecordSessionSegmentRepositoryPort recordSessionSegmentRepositoryPort;
    private final MonitoredAppPolicy monitoredAppPolicy;
    private final ZoneId recordZoneId;

    @Override
    public SwitchActivityAppData.Result execute(SwitchActivityAppData.Command command) {
        String nextAppName = normalizeAppName(command.appName());
        monitoredAppPolicy.validate(nextAppName);

        RecordSession activeSession = loadActiveActivitySession(command.actor().id());
        Instant switchedAt = Instant.now();

        if (nextAppName.equals(activeSession.appName())) {
            return toResult(activeSession, switchedAt);
        }

        Optional<RecordSessionSegment> openSegment = findOpenSegment(activeSession.id());
        if (isSameAppAsOpenSegment(openSegment, nextAppName)) {
            RecordSession syncedSession = syncSessionAppName(activeSession, nextAppName);
            return toResult(syncedSession, switchedAt);
        }

        closeOpenSegment(openSegment, switchedAt);
        createOpenSegment(activeSession.id(), nextAppName, switchedAt);
        RecordSession switchedSession = syncSessionAppName(activeSession, nextAppName);

        return toResult(switchedSession, switchedAt);
    }

    private String normalizeAppName(String appName) {
        if (appName == null || appName.isBlank()) {
            throw new InvalidActivitySwitchRequestException();
        }
        return appName.trim();
    }

    private RecordSession loadActiveActivitySession(Long userId) {
        RecordSession activeSession = recordSessionRepositoryPort.findActiveSessionByUserIdForUpdate(userId)
            .orElseThrow(ActiveSessionNotFound::new);
        if (activeSession.recordType() != RecordType.ACTIVITY) {
            throw new InvalidActivitySwitchRequestException();
        }
        return activeSession;
    }

    private Optional<RecordSessionSegment> findOpenSegment(Long sessionId) {
        return recordSessionSegmentRepositoryPort.findOpenSegmentBySessionIdForUpdate(sessionId);
    }

    private boolean isSameAppAsOpenSegment(Optional<RecordSessionSegment> openSegment, String appName) {
        return openSegment.isPresent() && appName.equals(openSegment.get().appName());
    }

    private void closeOpenSegment(Optional<RecordSessionSegment> openSegment, Instant closedAt) {
        openSegment.ifPresent(segment ->
            recordSessionSegmentRepositoryPort.save(segment.changeEndedAt(closedAt))
        );
    }

    private void createOpenSegment(Long sessionId, String appName, Instant startedAt) {
        recordSessionSegmentRepositoryPort.save(
            RecordSessionSegment.start(sessionId, appName, startedAt)
        );
    }

    private RecordSession syncSessionAppName(RecordSession activeSession, String appName) {
        return recordSessionRepositoryPort.save(activeSession.changeActivityAppName(appName));
    }

    private SwitchActivityAppData.Result toResult(RecordSession session, Instant switchedAt) {
        return SwitchActivityAppData.Result.from(
            switchedAt,
            RecordSessionMapper.toSession(session, recordZoneId)
        );
    }
}
