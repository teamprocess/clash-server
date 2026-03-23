package com.process.clash.application.realtime.service;

import com.process.clash.application.record.v2.port.out.RecordSessionV2RepositoryPort;
import com.process.clash.domain.record.enums.MonitoredApp;
import com.process.clash.domain.record.v2.entity.RecordSessionV2;
import com.process.clash.domain.record.v2.enums.RecordSessionTypeV2;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PresenceReconnectRecoveryServiceTest {

    @Mock
    private RecordSessionV2RepositoryPort recordSessionV2RepositoryPort;

    @Mock
    private UserPresenceService userPresenceService;

    @InjectMocks
    private PresenceReconnectRecoveryService presenceReconnectRecoveryService;

    @Test
    @DisplayName("startup recovery는 활성 세션 사용자들을 중복 없이 RECONNECTING으로 등록한다")
    void recoverActiveSessionUsers_registersDistinctUserIds() {
        when(recordSessionV2RepositoryPort.findAllActiveSessions()).thenReturn(List.of(
            new RecordSessionV2(
                1L,
                10L,
                RecordSessionTypeV2.DEVELOP,
                null,
                null,
                null,
                null,
                MonitoredApp.VSCODE,
                Instant.now(),
                null
            ),
            new RecordSessionV2(
                2L,
                10L,
                RecordSessionTypeV2.TASK,
                1L,
                "알고리즘",
                null,
                null,
                null,
                Instant.now(),
                null
            ),
            new RecordSessionV2(
                3L,
                20L,
                RecordSessionTypeV2.TASK,
                2L,
                "운영체제",
                null,
                null,
                null,
                Instant.now(),
                null
            )
        ));
        when(userPresenceService.registerReconnectingUsers(List.of(10L, 20L))).thenReturn(2);

        int recoveredCount = presenceReconnectRecoveryService.recoverActiveSessionUsers();

        assertThat(recoveredCount).isEqualTo(2);

        ArgumentCaptor<List<Long>> userIdsCaptor = ArgumentCaptor.forClass(List.class);
        verify(userPresenceService).registerReconnectingUsers(userIdsCaptor.capture());
        assertThat(userIdsCaptor.getValue()).containsExactly(10L, 20L);
    }
}
