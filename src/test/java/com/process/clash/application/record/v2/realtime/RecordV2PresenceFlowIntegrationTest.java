package com.process.clash.application.record.v2.realtime;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.record.v2.data.StartRecordV2Data;
import com.process.clash.application.record.v2.exception.exception.badrequest.InvalidRecordV2StartRequestException;
import com.process.clash.application.record.v2.port.in.StartRecordV2UseCase;
import com.process.clash.application.record.v2.port.out.RecordSessionV2RepositoryPort;
import com.process.clash.application.realtime.port.in.ReportUserPresenceUseCase;
import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import com.process.clash.domain.record.enums.MonitoredApp;
import com.process.clash.domain.record.v2.enums.RecordSessionTypeV2;
import com.process.clash.domain.user.user.entity.User;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class RecordV2PresenceFlowIntegrationTest {

    @Autowired
    private UserRepositoryPort userRepositoryPort;

    @Autowired
    private StartRecordV2UseCase startRecordV2UseCase;

    @Autowired
    private ReportUserPresenceUseCase reportUserPresenceUseCase;

    @Autowired
    private RecordSessionV2RepositoryPort recordSessionV2RepositoryPort;

    @Test
    @DisplayName("presence:away 수신 시 진행 중인 DEVELOP 세션이 종료된다")
    void awayEvent_stopsDevelopSession() {
        User user = createActiveUser();
        String connectionId = "conn-away-v2-" + UUID.randomUUID();
        Actor actor = new Actor(user.id());

        reportUserPresenceUseCase.connected(connectionId, user.id());
        startRecordV2UseCase.execute(new StartRecordV2Data.Command(
            RecordSessionTypeV2.DEVELOP,
            null,
            null,
            MonitoredApp.VSCODE,
            actor
        ));
        assertThat(recordSessionV2RepositoryPort.findActiveSessionByUserId(user.id())).isPresent();

        reportUserPresenceUseCase.markedAway(connectionId);

        assertThat(recordSessionV2RepositoryPort.findActiveSessionByUserId(user.id())).isEmpty();
        reportUserPresenceUseCase.disconnected(connectionId);
    }

    @Test
    @DisplayName("소켓 연결 종료 시 진행 중인 DEVELOP 세션이 종료된다")
    void disconnect_stopsDevelopSession() {
        User user = createActiveUser();
        String connectionId = "conn-offline-v2-" + UUID.randomUUID();
        Actor actor = new Actor(user.id());

        reportUserPresenceUseCase.connected(connectionId, user.id());
        startRecordV2UseCase.execute(new StartRecordV2Data.Command(
            RecordSessionTypeV2.DEVELOP,
            null,
            null,
            MonitoredApp.INTELLIJ_IDEA,
            actor
        ));
        assertThat(recordSessionV2RepositoryPort.findActiveSessionByUserId(user.id())).isPresent();

        reportUserPresenceUseCase.disconnected(connectionId);

        assertThat(recordSessionV2RepositoryPort.findActiveSessionByUserId(user.id())).isEmpty();
    }

    @Test
    @DisplayName("이미 자리비움/오프라인이면 DEVELOP 세션을 시작할 수 없다")
    void cannotStartDevelopWhenNotOnline() {
        User user = createActiveUser();
        String connectionId = "conn-start-while-away-v2-" + UUID.randomUUID();
        Actor actor = new Actor(user.id());

        reportUserPresenceUseCase.connected(connectionId, user.id());
        reportUserPresenceUseCase.markedAway(connectionId);

        assertThatThrownBy(() -> startRecordV2UseCase.execute(new StartRecordV2Data.Command(
            RecordSessionTypeV2.DEVELOP,
            null,
            null,
            MonitoredApp.VSCODE,
            actor
        )))
            .isInstanceOf(InvalidRecordV2StartRequestException.class);

        assertThat(recordSessionV2RepositoryPort.findActiveSessionByUserId(user.id())).isEmpty();
        reportUserPresenceUseCase.disconnected(connectionId);
    }

    private User createActiveUser() {
        String suffix = UUID.randomUUID().toString().substring(0, 8);
        User defaultUser = User.createDefault(
            "presence_v2_user_" + suffix,
            "presence_v2_" + suffix + "@example.com",
            "presence-v2-user",
            "encoded-password"
        );
        return userRepositoryPort.save(defaultUser.active());
    }
}
