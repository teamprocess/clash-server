package com.process.clash.application.record.realtime;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.group.port.out.GroupRepositoryPort;
import com.process.clash.application.realtime.data.ChangeDomain;
import com.process.clash.application.realtime.data.ChangeType;
import com.process.clash.application.realtime.data.RefetchNotice;
import com.process.clash.application.realtime.service.RefetchEventPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PublishToIncludedGroupsTest {

    @Mock
    private GroupRepositoryPort groupRepositoryPort;

    @Mock
    private RefetchEventPublisher refetchEventPublisher;

    private PublishToIncludedGroups publishToIncludedGroups;

    @BeforeEach
    void setUp() {
        publishToIncludedGroups = new PublishToIncludedGroups(groupRepositoryPort, refetchEventPublisher);
    }

    @Test
    @DisplayName("actor가 포함된 그룹 멤버들에게 refetch를 publish한다")
    void publish_publishesToAllIncludedGroupMembers() {
        Actor actor = new Actor(1L);
        List<Long> groupIds = List.of(10L, 20L);
        List<Long> memberIds = List.of(1L, 2L, 3L);

        when(groupRepositoryPort.findGroupIdsByMemberUserId(actor.id())).thenReturn(groupIds);
        when(groupRepositoryPort.findMemberUserIdsByGroupIds(groupIds)).thenReturn(memberIds);

        publishToIncludedGroups.publish(actor, ChangeType.ACTIVITY_STARTED);

        ArgumentCaptor<RefetchNotice> noticeCaptor = ArgumentCaptor.forClass(RefetchNotice.class);
        @SuppressWarnings("unchecked")
        ArgumentCaptor<Collection<Long>> userIdsCaptor = ArgumentCaptor.forClass(Collection.class);
        verify(refetchEventPublisher).publish(noticeCaptor.capture(), userIdsCaptor.capture());

        RefetchNotice notice = noticeCaptor.getValue();
        assertThat(notice.domain()).isEqualTo(ChangeDomain.GROUP);
        assertThat(notice.type()).isEqualTo(ChangeType.ACTIVITY_STARTED);
        assertThat(userIdsCaptor.getValue()).containsExactlyElementsOf(memberIds);
    }

    @Test
    @DisplayName("포함된 그룹이 없으면 publish 하지 않는다")
    void publish_skipsWhenNoIncludedGroups() {
        Actor actor = new Actor(1L);
        when(groupRepositoryPort.findGroupIdsByMemberUserId(actor.id())).thenReturn(List.of());

        publishToIncludedGroups.publish(actor, ChangeType.ACTIVITY_STOPPED);

        verify(groupRepositoryPort, never()).findMemberUserIdsByGroupIds(anyList());
        verifyNoInteractions(refetchEventPublisher);
    }
}

