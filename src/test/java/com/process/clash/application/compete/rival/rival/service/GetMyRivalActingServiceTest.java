package com.process.clash.application.compete.rival.rival.service;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.compete.rival.rival.data.GetMyRivalActingData;
import com.process.clash.application.compete.rival.rival.port.out.RivalRepositoryPort;
import com.process.clash.application.realtime.data.UserActivityStatus;
import com.process.clash.application.realtime.port.out.UserPresencePort;
import com.process.clash.application.record.port.out.RecordSessionRepositoryPort;
import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import com.process.clash.domain.common.enums.Major;
import com.process.clash.domain.rival.rival.entity.Rival;
import com.process.clash.domain.rival.rival.enums.RivalLinkingStatus;
import com.process.clash.domain.user.user.entity.User;
import com.process.clash.domain.user.user.enums.Role;
import com.process.clash.domain.user.user.enums.UserStatus;
import com.process.clash.infrastructure.config.RecordProperties;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetMyRivalActingServiceTest {

    @Mock
    private RivalRepositoryPort rivalRepositoryPort;

    @Mock
    private RecordSessionRepositoryPort recordSessionRepositoryPort;

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @Mock
    private UserPresencePort userPresencePort;

    private GetMyRivalActingService getMyRivalActingService;

    @BeforeEach
    void setUp() {
        getMyRivalActingService = new GetMyRivalActingService(
            rivalRepositoryPort,
            recordSessionRepositoryPort,
            userRepositoryPort,
            userPresencePort,
            new RecordProperties("UTC", 0),
            ZoneId.of("UTC")
        );
    }

    @Test
    @DisplayName("내 라이벌 조회 시 각 라이벌의 실시간 활동 상태를 반환한다")
    void execute_returnsRivalsWithActivityStatus() {
        Actor actor = new Actor(1L);
        GetMyRivalActingData.Command command = new GetMyRivalActingData.Command(actor);

        Rival rivalA = new Rival(
            100L,
            Instant.now().minusSeconds(86_400),
            Instant.now(),
            RivalLinkingStatus.ACCEPTED,
            1L,
            2L
        );
        Rival rivalB = new Rival(
            101L,
            Instant.now().minusSeconds(86_400),
            Instant.now(),
            RivalLinkingStatus.ACCEPTED,
            3L,
            1L
        );

        User rivalUserA = createUser(2L, "rivalA", "Rival A");
        User rivalUserB = createUser(3L, "rivalB", "Rival B");

        when(rivalRepositoryPort.findAllByUserId(actor.id())).thenReturn(List.of(rivalA, rivalB));
        when(userRepositoryPort.findAllByIds(anyList()))
            .thenReturn(List.of(rivalUserA, rivalUserB, createUser(1L, "me", "Me")));
        when(recordSessionRepositoryPort.getTotalStudyTimeInSecondsByUserIds(
            anyList(),
            any(),
            any()
        )).thenReturn(Map.of(2L, 1800L, 3L, 600L));
        when(userPresencePort.getStatuses(anyList()))
            .thenReturn(Map.of(2L, UserActivityStatus.ONLINE, 3L, UserActivityStatus.AWAY, 1L, UserActivityStatus.ONLINE));

        GetMyRivalActingData.Result result = getMyRivalActingService.execute(command);

        assertThat(result.myRivals()).hasSize(2);
        assertThat(result.myRivals().get(0).rivalId()).isEqualTo(100L);
        assertThat(result.myRivals().get(1).rivalId()).isEqualTo(101L);
        assertThat(result.myRivals().get(0).status()).isEqualTo(UserActivityStatus.ONLINE);
        assertThat(result.myRivals().get(1).status()).isEqualTo(UserActivityStatus.AWAY);
        assertThat(result.myRivals().get(0).usingApp()).isEqualTo("CLASH");
        assertThat(result.myRivals().get(1).usingApp()).isEqualTo("CLASH");
    }

    private User createUser(Long id, String username, String name) {
        return new User(
            id,
            Instant.now().minusSeconds(86_400),
            Instant.now(),
            username,
            username + "@example.com",
            name,
            "encoded-password",
            Role.USER,
            "",
            0,
            0,
            0,
            Major.NONE,
            UserStatus.ACTIVE
        );
    }
}
