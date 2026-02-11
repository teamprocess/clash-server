package com.process.clash.application.compete.realtime;

import com.process.clash.application.compete.rival.rival.port.out.RivalRepositoryPort;
import com.process.clash.application.realtime.data.UserActivityStatus;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.anyList;

@ExtendWith(MockitoExtension.class)
class RivalPresenceStatusChangedNotifierTest {

    @Mock
    private RivalRepositoryPort rivalRepositoryPort;

    @Mock
    private CompeteRefetchNotifier competeRefetchNotifier;

    private RivalPresenceStatusChangedNotifier notifier;

    @BeforeEach
    void setUp() {
        notifier = new RivalPresenceStatusChangedNotifier(rivalRepositoryPort, competeRefetchNotifier);
    }

    @Test
    @DisplayName("유저 상태가 바뀌면 라이벌들에게 STATUS_CHANGED 이벤트를 보낸다")
    void notifyStatusChanged_publishesToRivals() {
        when(rivalRepositoryPort.findOpponentIdByUserId(1L)).thenReturn(List.of(2L, 3L));

        notifier.notifyStatusChanged(1L, UserActivityStatus.ONLINE, UserActivityStatus.AWAY);

        verify(competeRefetchNotifier).notifyRivalStatusChanged(List.of(2L, 3L));
    }

    @Test
    @DisplayName("유저 상태가 바뀌지 않으면 이벤트를 보내지 않는다")
    void notifyStatusChanged_skipsWhenNoChange() {
        notifier.notifyStatusChanged(1L, UserActivityStatus.ONLINE, UserActivityStatus.ONLINE);

        verify(rivalRepositoryPort, never()).findOpponentIdByUserId(1L);
        verify(competeRefetchNotifier, never()).notifyRivalStatusChanged(anyList());
    }
}
