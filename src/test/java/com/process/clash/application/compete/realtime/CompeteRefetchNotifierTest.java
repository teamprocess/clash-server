package com.process.clash.application.compete.realtime;

import com.process.clash.application.realtime.data.ChangeDomain;
import com.process.clash.application.realtime.data.ChangeType;
import com.process.clash.application.realtime.data.RefetchNotice;
import com.process.clash.application.realtime.service.RefetchEventPublisher;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CompeteRefetchNotifierTest {

    @Mock
    private RefetchEventPublisher refetchEventPublisher;

    private CompeteRefetchNotifier competeRefetchNotifier;

    @BeforeEach
    void setUp() {
        competeRefetchNotifier = new CompeteRefetchNotifier(refetchEventPublisher);
    }

    @Test
    @DisplayName("라이벌 활동 시작 변경을 COMPETE 도메인으로 publish한다")
    void notifyRivalActivityStarted_publishesCompeteActivityStarted() {
        List<Long> userIds = List.of(2L, 3L);

        competeRefetchNotifier.notifyRivalActivityStarted(userIds);

        ArgumentCaptor<RefetchNotice> noticeCaptor = ArgumentCaptor.forClass(RefetchNotice.class);
        @SuppressWarnings("unchecked")
        ArgumentCaptor<Collection<Long>> userIdsCaptor = ArgumentCaptor.forClass(Collection.class);
        verify(refetchEventPublisher).publish(noticeCaptor.capture(), userIdsCaptor.capture());

        RefetchNotice notice = noticeCaptor.getValue();
        assertThat(notice.domain()).isEqualTo(ChangeDomain.COMPETE);
        assertThat(notice.type()).isEqualTo(ChangeType.ACTIVITY_STARTED);
        assertThat(userIdsCaptor.getValue()).containsExactlyElementsOf(userIds);
    }

    @Test
    @DisplayName("라이벌 활동 종료 변경을 COMPETE 도메인으로 publish한다")
    void notifyRivalActivityStopped_publishesCompeteActivityStopped() {
        List<Long> userIds = List.of(2L, 3L);

        competeRefetchNotifier.notifyRivalActivityStopped(userIds);

        ArgumentCaptor<RefetchNotice> noticeCaptor = ArgumentCaptor.forClass(RefetchNotice.class);
        @SuppressWarnings("unchecked")
        ArgumentCaptor<Collection<Long>> userIdsCaptor = ArgumentCaptor.forClass(Collection.class);
        verify(refetchEventPublisher).publish(noticeCaptor.capture(), userIdsCaptor.capture());

        RefetchNotice notice = noticeCaptor.getValue();
        assertThat(notice.domain()).isEqualTo(ChangeDomain.COMPETE);
        assertThat(notice.type()).isEqualTo(ChangeType.ACTIVITY_STOPPED);
        assertThat(userIdsCaptor.getValue()).containsExactlyElementsOf(userIds);
    }
}
