package com.process.clash.application.realtime.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PresenceReconnectTimeoutServiceTest {

    @Mock
    private UserPresenceService userPresenceService;

    @InjectMocks
    private PresenceReconnectTimeoutService presenceReconnectTimeoutService;

    @Test
    @DisplayName("timeout service는 presence service의 만료 처리 결과를 그대로 반환한다")
    void expireReconnectTimeouts_delegatesToPresenceService() {
        when(userPresenceService.expireReconnectingUsers()).thenReturn(2);

        int expiredCount = presenceReconnectTimeoutService.expireReconnectTimeouts();

        assertThat(expiredCount).isEqualTo(2);
        verify(userPresenceService).expireReconnectingUsers();
    }
}
