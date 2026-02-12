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
import java.time.LocalDateTime;
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

        StudySession activeSession = studySessionRepositoryPort.findActiveSessionByUserIdForUpdate(command.actor().id())
            .orElseThrow(ActiveSessionNotFound::new);

        if (activeSession.recordType() != RecordType.ACTIVITY) {
            throw new InvalidActivitySwitchRequestException();
        }

        LocalDateTime switchedAt = LocalDateTime.now(recordZoneId);

        Optional<RecordActivitySegment> openSegment = recordActivitySegmentRepositoryPort
            .findOpenSegmentBySessionIdForUpdate(activeSession.id());

        if (nextAppName.equals(activeSession.appName())) {
            return SwitchActivityAppData.Result.from(
                switchedAt.atZone(recordZoneId).toInstant(),
                RecordSessionMapper.toSession(activeSession, recordZoneId)
            );
        }

        if (openSegment.isPresent()) {
            RecordActivitySegment segment = openSegment.get();
            if (!nextAppName.equals(segment.appName())) {
                recordActivitySegmentRepositoryPort.save(segment.changeEndedAt(switchedAt));
            } else {
                StudySession syncedSession = studySessionRepositoryPort.save(
                    activeSession.changeActivityAppName(nextAppName)
                );
                return SwitchActivityAppData.Result.from(
                    switchedAt.atZone(recordZoneId).toInstant(),
                    RecordSessionMapper.toSession(syncedSession, recordZoneId)
                );
            }
        }

        recordActivitySegmentRepositoryPort.save(
            RecordActivitySegment.start(activeSession.id(), nextAppName, switchedAt)
        );

        StudySession switchedSession = studySessionRepositoryPort.save(
            activeSession.changeActivityAppName(nextAppName)
        );

        return SwitchActivityAppData.Result.from(
            switchedAt.atZone(recordZoneId).toInstant(),
            RecordSessionMapper.toSession(switchedSession, recordZoneId)
        );
    }

    private String normalizeAppName(String appName) {
        if (appName == null || appName.isBlank()) {
            throw new InvalidActivitySwitchRequestException();
        }
        return appName.trim();
    }
}
