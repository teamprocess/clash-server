package com.process.clash.application.record.realtime;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.compete.realtime.CompeteRefetchNotifier;
import com.process.clash.application.compete.rival.rival.port.out.RivalRepositoryPort;
import com.process.clash.application.group.port.out.GroupRepositoryPort;
import com.process.clash.application.group.realtime.GroupRefetchNotifier;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecordActivityRefetchNotifierTest {

    @Mock
    private GroupRepositoryPort groupRepositoryPort;

    @Mock
    private RivalRepositoryPort rivalRepositoryPort;

    @Mock
    private GroupRefetchNotifier groupRefetchNotifier;

    @Mock
    private CompeteRefetchNotifier competeRefetchNotifier;

    private RecordActivityRefetchNotifier recordActivityRefetchNotifier;

    @BeforeEach
    void setUp() {
        recordActivityRefetchNotifier = new RecordActivityRefetchNotifier(
            groupRepositoryPort,
            rivalRepositoryPort,
            groupRefetchNotifier,
            competeRefetchNotifier
        );
    }

    @Test
    @DisplayName("활동 시작 시 포함 그룹/라이벌에게 각각 refetch를 notify한다")
    void notifyActivityStarted_notifiesGroupAndRival() {
        Actor actor = new Actor(1L);
        List<Long> groupIds = List.of(10L, 20L);
        List<Long> memberUserIds = List.of(1L, 2L, 3L);
        List<Long> rivalUserIds = List.of(4L, 5L);

        when(groupRepositoryPort.findGroupIdsByMemberUserId(actor.id())).thenReturn(groupIds);
        when(groupRepositoryPort.findMemberUserIdsByGroupIds(groupIds)).thenReturn(memberUserIds);
        when(rivalRepositoryPort.findOpponentIdByUserId(actor.id())).thenReturn(rivalUserIds);

        recordActivityRefetchNotifier.notifyActivityStarted(actor);

        verify(groupRefetchNotifier).notifyGroupActivityStarted(memberUserIds);
        verify(competeRefetchNotifier).notifyRivalActivityStarted(rivalUserIds);
    }

    @Test
    @DisplayName("활동 종료 시 대상이 없으면 refetch를 notify하지 않는다")
    void notifyActivityStopped_skipsWhenNoTargets() {
        Actor actor = new Actor(1L);

        when(groupRepositoryPort.findGroupIdsByMemberUserId(actor.id())).thenReturn(List.of());
        when(rivalRepositoryPort.findOpponentIdByUserId(actor.id())).thenReturn(List.of());

        recordActivityRefetchNotifier.notifyActivityStopped(actor);

        verify(groupRepositoryPort, never()).findMemberUserIdsByGroupIds(anyList());
        verifyNoInteractions(groupRefetchNotifier, competeRefetchNotifier);
    }
}
