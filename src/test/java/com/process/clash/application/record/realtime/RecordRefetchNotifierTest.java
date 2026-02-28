package com.process.clash.application.record.realtime;

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
class RecordRefetchNotifierTest {

    @Mock
    private RefetchEventPublisher refetchEventPublisher;

    private RecordRefetchNotifier recordRefetchNotifier;

    @BeforeEach
    void setUp() {
        recordRefetchNotifier = new RecordRefetchNotifier(refetchEventPublisher);
    }

    @Test
    @DisplayName("record 활동 시작 변경을 RECORD 도메인으로 publish한다")
    void notifyRecordActivityStarted_publishesRecordActivityStarted() {
        List<Long> userIds = List.of(1L);

        recordRefetchNotifier.notifyRecordActivityStarted(userIds);

        assertPublished(ChangeType.ACTIVITY_STARTED, userIds);
    }

    @Test
    @DisplayName("record 활동 종료 변경을 RECORD 도메인으로 publish한다")
    void notifyRecordActivityStopped_publishesRecordActivityStopped() {
        List<Long> userIds = List.of(1L);

        recordRefetchNotifier.notifyRecordActivityStopped(userIds);

        assertPublished(ChangeType.ACTIVITY_STOPPED, userIds);
    }

    @Test
    @DisplayName("record 세션 데이터 변경을 RECORD 도메인으로 publish한다")
    void notifyRecordSessionChanged_publishesRecordDataChanged() {
        List<Long> userIds = List.of(1L);

        recordRefetchNotifier.notifyRecordSessionChanged(userIds);

        assertPublished(ChangeType.DATA_CHANGED, userIds);
    }

    private void assertPublished(ChangeType expectedType, List<Long> expectedUserIds) {
        ArgumentCaptor<RefetchNotice> noticeCaptor = ArgumentCaptor.forClass(RefetchNotice.class);
        @SuppressWarnings("unchecked")
        ArgumentCaptor<Collection<Long>> userIdsCaptor = ArgumentCaptor.forClass(Collection.class);
        verify(refetchEventPublisher).publish(noticeCaptor.capture(), userIdsCaptor.capture());

        RefetchNotice notice = noticeCaptor.getValue();
        assertThat(notice.domain()).isEqualTo(ChangeDomain.RECORD);
        assertThat(notice.type()).isEqualTo(expectedType);
        assertThat(userIdsCaptor.getValue()).containsExactlyElementsOf(expectedUserIds);
    }
}
