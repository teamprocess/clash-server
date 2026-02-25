package com.process.clash.application.group.service;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.group.data.QuitGroupData;
import com.process.clash.application.group.exception.exception.badrequest.GroupNotMemberException;
import com.process.clash.application.group.policy.GroupPolicy;
import com.process.clash.application.group.port.out.GroupRepositoryPort;
import com.process.clash.application.group.realtime.GroupRefetchNotifier;
import com.process.clash.domain.group.entity.Group;
import com.process.clash.domain.group.enums.GroupCategory;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QuitGroupServiceTest {

    @Mock
    private GroupRepositoryPort groupRepositoryPort;

    @Mock
    private GroupRefetchNotifier groupRefetchNotifier;

    private QuitGroupService quitGroupService;

    @BeforeEach
    void setUp() {
        quitGroupService = new QuitGroupService(
            groupRepositoryPort,
            new GroupPolicy(),
            groupRefetchNotifier
        );
    }

    @Test
    @DisplayName("그룹 탈퇴 성공 시 남은 멤버와 탈퇴자에게 refetch를 publish한다")
    void execute_notifiesRemainingMembersAndLeaverOnQuit() {
        Long groupId = 11L;
        Actor actor = new Actor(2L);
        QuitGroupData.Command command = QuitGroupData.Command.of(actor, groupId);
        Group group = createGroup(groupId, 1L);

        when(groupRepositoryPort.findById(groupId)).thenReturn(Optional.of(group));
        when(groupRepositoryPort.existsMember(groupId, actor.id())).thenReturn(true);
        when(groupRepositoryPort.findMemberUserIdsByGroupIds(List.of(groupId))).thenReturn(List.of(1L, 3L));

        quitGroupService.execute(command);

        verify(groupRepositoryPort).removeMember(groupId, actor.id());
        verify(groupRefetchNotifier).notifyGroupsChanged(List.of(1L, 3L, 2L));
    }

    @Test
    @DisplayName("그룹 멤버가 아니면 refetch를 publish하지 않는다")
    void execute_skipsPublishWhenUserIsNotMember() {
        Long groupId = 11L;
        Actor actor = new Actor(2L);
        QuitGroupData.Command command = QuitGroupData.Command.of(actor, groupId);
        Group group = createGroup(groupId, 1L);

        when(groupRepositoryPort.findById(groupId)).thenReturn(Optional.of(group));
        when(groupRepositoryPort.existsMember(groupId, actor.id())).thenReturn(false);

        assertThatThrownBy(() -> quitGroupService.execute(command))
            .isInstanceOf(GroupNotMemberException.class);

        verify(groupRepositoryPort, never()).removeMember(any(), any());
        verify(groupRefetchNotifier, never()).notifyGroupsChanged(any());
    }

    private Group createGroup(Long groupId, Long ownerId) {
        return new Group(
            groupId,
            Instant.now(),
            Instant.now(),
            "group",
            "desc",
            10,
            "",
            false,
            GroupCategory.TEAM,
            ownerId
        );
    }
}
