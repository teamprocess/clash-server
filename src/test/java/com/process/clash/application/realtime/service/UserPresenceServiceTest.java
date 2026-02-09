package com.process.clash.application.realtime.service;

import com.process.clash.application.realtime.data.UserActivityStatus;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserPresenceServiceTest {

    private final UserPresenceService userPresenceService = new UserPresenceService();

    @Test
    @DisplayName("연결, 자리비움, 복귀, 연결해제 흐름에 따라 상태를 갱신한다")
    void presenceStatusTransitions() {
        userPresenceService.connected("conn-1", 1L);
        assertThat(userPresenceService.getStatus(1L)).isEqualTo(UserActivityStatus.ONLINE);

        userPresenceService.markedAway("conn-1");
        assertThat(userPresenceService.getStatus(1L)).isEqualTo(UserActivityStatus.AWAY);

        userPresenceService.markedOnline("conn-1");
        assertThat(userPresenceService.getStatus(1L)).isEqualTo(UserActivityStatus.ONLINE);

        userPresenceService.disconnected("conn-1");
        assertThat(userPresenceService.getStatus(1L)).isEqualTo(UserActivityStatus.OFFLINE);
    }

    @Test
    @DisplayName("다중 연결에서는 하나라도 active면 ONLINE, 모두 away면 AWAY이다")
    void multiConnectionStatusRule() {
        userPresenceService.connected("conn-1", 1L);
        userPresenceService.connected("conn-2", 1L);

        userPresenceService.markedAway("conn-1");
        assertThat(userPresenceService.getStatus(1L)).isEqualTo(UserActivityStatus.ONLINE);

        userPresenceService.markedAway("conn-2");
        assertThat(userPresenceService.getStatus(1L)).isEqualTo(UserActivityStatus.AWAY);

        userPresenceService.disconnected("conn-2");
        assertThat(userPresenceService.getStatus(1L)).isEqualTo(UserActivityStatus.AWAY);

        userPresenceService.markedOnline("conn-1");
        assertThat(userPresenceService.getStatus(1L)).isEqualTo(UserActivityStatus.ONLINE);
    }

    @Test
    @DisplayName("여러 사용자 상태를 일괄 조회한다")
    void getStatuses_returnsStatusesByUserId() {
        userPresenceService.connected("u1-1", 1L);
        userPresenceService.connected("u2-1", 2L);
        userPresenceService.markedAway("u2-1");

        Map<Long, UserActivityStatus> statuses = userPresenceService.getStatuses(
            java.util.List.of(1L, 2L, 3L)
        );

        assertThat(statuses.get(1L)).isEqualTo(UserActivityStatus.ONLINE);
        assertThat(statuses.get(2L)).isEqualTo(UserActivityStatus.AWAY);
        assertThat(statuses.get(3L)).isEqualTo(UserActivityStatus.OFFLINE);
    }
}
