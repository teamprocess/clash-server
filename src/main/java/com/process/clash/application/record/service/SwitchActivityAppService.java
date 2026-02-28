package com.process.clash.application.record.service;

import com.process.clash.application.record.data.SwitchActivityAppData;
import com.process.clash.application.record.exception.exception.badrequest.InvalidActivitySwitchRequestException;
import com.process.clash.application.record.exception.exception.notfound.ActiveSessionNotFound;
import com.process.clash.application.record.policy.MonitoredAppPolicy;
import com.process.clash.application.record.port.in.SwitchActivityAppUseCase;
import com.process.clash.application.record.port.out.RecordActivityNotifierPort;
import com.process.clash.application.record.port.out.RecordSessionSegmentRepositoryPort;
import com.process.clash.application.record.port.out.RecordSessionRepositoryPort;
import com.process.clash.domain.record.entity.RecordSessionSegment;
import com.process.clash.domain.record.entity.RecordSession;
import com.process.clash.domain.record.enums.MonitoredApp;
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
    private final RecordActivityNotifierPort recordActivityNotifierPort;
    private final MonitoredAppPolicy monitoredAppPolicy;
    private final ZoneId recordZoneId;

    @Override
    public SwitchActivityAppData.Result execute(SwitchActivityAppData.Command command) {
        MonitoredApp nextAppId = normalizeAppId(command.appId());
        monitoredAppPolicy.validate(nextAppId);

        RecordSession activeSession = loadActiveActivitySession(command.actor().id());
        Instant switchedAt = Instant.now();

        if (nextAppId.equals(activeSession.appId())) {
            return toResult(activeSession, switchedAt);
        }

        Optional<RecordSessionSegment> openSegment = findOpenSegment(activeSession.id());
        if (!isSameAppAsOpenSegment(openSegment, nextAppId)) {
            closeOpenSegment(openSegment, switchedAt);
            createOpenSegment(activeSession.id(), nextAppId, switchedAt);
        }

        RecordSession switchedSession = syncSessionAppId(activeSession, nextAppId);
        recordActivityNotifierPort.notifySessionChanged(command.actor());
        return toResult(switchedSession, switchedAt);
    }

    private MonitoredApp normalizeAppId(MonitoredApp appId) {
        if (appId == null) {
            throw new InvalidActivitySwitchRequestException();
        }
        return appId;
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

    private boolean isSameAppAsOpenSegment(Optional<RecordSessionSegment> openSegment, MonitoredApp appId) {
        return openSegment.isPresent() && appId.equals(openSegment.get().appId());
    }

    private void closeOpenSegment(Optional<RecordSessionSegment> openSegment, Instant closedAt) {
        openSegment.ifPresent(segment ->
            recordSessionSegmentRepositoryPort.save(segment.changeEndedAt(closedAt))
        );
    }

    private void createOpenSegment(Long sessionId, MonitoredApp appId, Instant startedAt) {
        recordSessionSegmentRepositoryPort.save(
            RecordSessionSegment.start(sessionId, appId, startedAt)
        );
    }

    private RecordSession syncSessionAppId(RecordSession activeSession, MonitoredApp appId) {
        return recordSessionRepositoryPort.save(activeSession.changeActivityAppId(appId));
    }

    private SwitchActivityAppData.Result toResult(RecordSession session, Instant switchedAt) {
        return SwitchActivityAppData.Result.from(
            switchedAt,
            RecordSessionMapper.toSession(session, recordZoneId)
        );
    }
}
