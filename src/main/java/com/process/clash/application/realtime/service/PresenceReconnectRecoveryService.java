package com.process.clash.application.realtime.service;

import com.process.clash.application.record.v2.port.out.RecordSessionV2RepositoryPort;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PresenceReconnectRecoveryService {

    private final RecordSessionV2RepositoryPort recordSessionV2RepositoryPort;
    private final UserPresenceService userPresenceService;

    public int recoverActiveSessionUsers() {
        List<Long> activeSessionUserIds = recordSessionV2RepositoryPort.findAllActiveSessions().stream()
            .map(com.process.clash.domain.record.v2.entity.RecordSessionV2::userId)
            .filter(java.util.Objects::nonNull)
            .distinct()
            .toList();

        return userPresenceService.registerReconnectingUsers(activeSessionUserIds);
    }
}
