package com.process.clash.application.record.v2.service;

import com.process.clash.application.record.policy.MonitoredAppPolicy;
import com.process.clash.application.record.v2.data.SwitchDevelopAppV2Data;
import com.process.clash.application.record.v2.exception.exception.badrequest.InvalidDevelopAppSwitchRequestException;
import com.process.clash.application.record.v2.exception.exception.notfound.ActiveSessionV2NotFoundException;
import com.process.clash.application.record.v2.port.in.SwitchDevelopAppV2UseCase;
import com.process.clash.application.record.v2.port.out.RecordDevelopSessionSegmentV2RepositoryPort;
import com.process.clash.application.record.v2.port.out.RecordSessionV2RepositoryPort;
import com.process.clash.domain.record.enums.MonitoredApp;
import com.process.clash.domain.record.v2.entity.RecordDevelopSessionSegmentV2;
import com.process.clash.domain.record.v2.entity.RecordSessionV2;
import com.process.clash.domain.record.v2.enums.RecordSessionTypeV2;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class SwitchDevelopAppV2Service implements SwitchDevelopAppV2UseCase {

    private final RecordSessionV2RepositoryPort recordSessionV2RepositoryPort;
    private final RecordDevelopSessionSegmentV2RepositoryPort recordDevelopSessionSegmentV2RepositoryPort;
    private final MonitoredAppPolicy monitoredAppPolicy;
    private final ZoneId recordZoneId;

    @Override
    public SwitchDevelopAppV2Data.Result execute(SwitchDevelopAppV2Data.Command command) {
        MonitoredApp nextAppId = normalizeAppId(command.appId());
        monitoredAppPolicy.validate(nextAppId);

        RecordSessionV2 activeSession = loadActiveDevelopSession(command.actor().id());
        Instant switchedAt = Instant.now();

        // 현재 앱과 동일하면 세그먼트를 만들지 않고 그대로 반환
        if (nextAppId.equals(activeSession.appId())) {
            return toResult(activeSession, switchedAt);
        }

        Optional<RecordDevelopSessionSegmentV2> openSegment =
            recordDevelopSessionSegmentV2RepositoryPort.findOpenSegmentBySessionIdForUpdate(activeSession.id());

        // 세션 appId만 꼬인 상태를 보정: 열린 세그먼트와 같으면 세그먼트 생성 없이 동기화만 수행
        if (openSegment.isPresent() && nextAppId.equals(openSegment.get().appId())) {
            RecordSessionV2 syncedSession = recordSessionV2RepositoryPort.save(activeSession.changeDevelopAppId(nextAppId));
            return toResult(syncedSession, switchedAt);
        }

        // 기존 세그먼트를 닫고 새 앱 세그먼트를 시작해 앱 전환 이력을 분리 저장
        openSegment.ifPresent(segment ->
            recordDevelopSessionSegmentV2RepositoryPort.save(segment.changeEndedAt(switchedAt))
        );
        recordDevelopSessionSegmentV2RepositoryPort.save(
            RecordDevelopSessionSegmentV2.start(activeSession.id(), nextAppId, switchedAt)
        );

        RecordSessionV2 switchedSession = recordSessionV2RepositoryPort.save(activeSession.changeDevelopAppId(nextAppId));
        return toResult(switchedSession, switchedAt);
    }

    private MonitoredApp normalizeAppId(MonitoredApp appId) {
        if (appId == null) {
            throw new InvalidDevelopAppSwitchRequestException();
        }
        return appId;
    }

    private RecordSessionV2 loadActiveDevelopSession(Long userId) {
        RecordSessionV2 activeSession = recordSessionV2RepositoryPort.findActiveSessionByUserIdForUpdate(userId)
            .orElseThrow(ActiveSessionV2NotFoundException::new);

        if (activeSession.sessionType() != RecordSessionTypeV2.DEVELOP) {
            throw new InvalidDevelopAppSwitchRequestException();
        }
        return activeSession;
    }

    private SwitchDevelopAppV2Data.Result toResult(RecordSessionV2 session, Instant switchedAt) {
        return SwitchDevelopAppV2Data.Result.from(
            switchedAt,
            RecordSessionV2Mapper.toSession(session, recordZoneId)
        );
    }
}
