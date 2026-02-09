package com.process.clash.application.record.realtime;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.compete.rival.rival.port.out.RivalRepositoryPort;
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
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PublishToIncludedRivalsTest {

    @Mock
    private RivalRepositoryPort rivalRepositoryPort;

    @Mock
    private RefetchEventPublisher refetchEventPublisher;

    private PublishToIncludedRivals publishToIncludedRivals;

    @BeforeEach
    void setUp() {
        publishToIncludedRivals = new PublishToIncludedRivals(rivalRepositoryPort, refetchEventPublisher);
    }

    @Test
    @DisplayName("actor의 라이벌들에게 refetch를 publish한다")
    void publish_publishesToAllIncludedRivals() {
        Actor actor = new Actor(1L);
        List<Long> rivalUserIds = List.of(2L, 3L, 4L);

        when(rivalRepositoryPort.findOpponentIdByUserId(actor.id())).thenReturn(rivalUserIds);

        publishToIncludedRivals.publish(actor, ChangeType.ACTIVITY_STARTED);

        ArgumentCaptor<RefetchNotice> noticeCaptor = ArgumentCaptor.forClass(RefetchNotice.class);
        @SuppressWarnings("unchecked")
        ArgumentCaptor<Collection<Long>> userIdsCaptor = ArgumentCaptor.forClass(Collection.class);
        verify(refetchEventPublisher).publish(noticeCaptor.capture(), userIdsCaptor.capture());

        RefetchNotice notice = noticeCaptor.getValue();
        assertThat(notice.domain()).isEqualTo(ChangeDomain.COMPETE);
        assertThat(notice.type()).isEqualTo(ChangeType.ACTIVITY_STARTED);
        assertThat(userIdsCaptor.getValue()).containsExactlyElementsOf(rivalUserIds);
    }

    @Test
    @DisplayName("라이벌이 없으면 publish 하지 않는다")
    void publish_skipsWhenNoRivals() {
        Actor actor = new Actor(1L);
        when(rivalRepositoryPort.findOpponentIdByUserId(actor.id())).thenReturn(List.of());

        publishToIncludedRivals.publish(actor, ChangeType.ACTIVITY_STOPPED);

        verifyNoInteractions(refetchEventPublisher);
    }
}
