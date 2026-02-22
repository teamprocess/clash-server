package com.process.clash.application.record.v2.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.record.v2.data.GetCurrentRecordV2Data;
import com.process.clash.application.record.v2.port.out.RecordSessionV2RepositoryPort;
import com.process.clash.domain.record.enums.MonitoredApp;
import com.process.clash.domain.record.v2.entity.RecordSessionV2;
import com.process.clash.domain.record.v2.enums.RecordSessionTypeV2;
import java.time.Instant;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetCurrentRecordV2ServiceTest {

    @Mock
    private RecordSessionV2RepositoryPort recordSessionV2RepositoryPort;

    private GetCurrentRecordV2Service getCurrentRecordV2Service;

    @BeforeEach
    void setUp() {
        getCurrentRecordV2Service = new GetCurrentRecordV2Service(recordSessionV2RepositoryPort);
    }

    @Test
    @DisplayName("활성 개발 세션이 있으면 반환한다")
    void execute_returnsActiveSession() {
        Actor actor = new Actor(1L);
        RecordSessionV2 activeSession = new RecordSessionV2(
            100L,
            1L,
            RecordSessionTypeV2.DEVELOP,
            null,
            null,
            null,
            null,
            MonitoredApp.VSCODE,
            Instant.now().minusSeconds(300),
            null
        );
        when(recordSessionV2RepositoryPort.findActiveSessionByUserId(actor.id()))
            .thenReturn(Optional.of(activeSession));

        GetCurrentRecordV2Data.Result result = getCurrentRecordV2Service.execute(
            new GetCurrentRecordV2Data.Command(actor)
        );

        assertThat(result.session()).isNotNull();
        assertThat(result.session().sessionType()).isEqualTo(RecordSessionTypeV2.DEVELOP);
        assertThat(result.session().develop().appId()).isEqualTo(MonitoredApp.VSCODE);
    }

    @Test
    @DisplayName("활성 세션이 없으면 null을 반환한다")
    void execute_returnsNullWhenNoActiveSession() {
        Actor actor = new Actor(1L);
        when(recordSessionV2RepositoryPort.findActiveSessionByUserId(actor.id()))
            .thenReturn(Optional.empty());

        GetCurrentRecordV2Data.Result result = getCurrentRecordV2Service.execute(
            new GetCurrentRecordV2Data.Command(actor)
        );

        assertThat(result.session()).isNull();
    }
}
