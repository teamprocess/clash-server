package com.process.clash.application.group.service;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.group.data.JoinGroupData;
import com.process.clash.application.group.exception.exception.conflict.AlreadyInThisGroupException;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JoinGroupServiceTest {

    @Mock
    private GroupRepositoryPort groupRepositoryPort;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private GroupRefetchNotifier groupRefetchNotifier;

    private JoinGroupService joinGroupService;

    @BeforeEach
    void setUp() {
        joinGroupService = new JoinGroupService(
            groupRepositoryPort,
            passwordEncoder,
            new GroupPolicy(),
            groupRefetchNotifier
        );
    }

    @Test
    @DisplayName("그룹 참여 성공 시 해당 그룹 멤버들에게 refetch를 publish한다")
    void execute_notifiesGroupMembersOnJoin() {
        Long groupId = 10L;
        Actor actor = new Actor(2L);
        JoinGroupData.Command command = new JoinGroupData.Command(actor, groupId, null);
        Group group = createGroup(groupId, 1L, false);

        when(groupRepositoryPort.findById(groupId)).thenReturn(Optional.of(group));
        when(groupRepositoryPort.existsMember(groupId, actor.id())).thenReturn(false);
        when(groupRepositoryPort.countMembers(groupId)).thenReturn(1);
        when(groupRepositoryPort.findMemberUserIdsByGroupIds(List.of(groupId))).thenReturn(List.of(1L, 2L));

        joinGroupService.execute(command);

        verify(groupRepositoryPort).addMember(groupId, actor.id());
        verify(groupRefetchNotifier).notifyGroupsChanged(List.of(1L, 2L));
    }

    @Test
    @DisplayName("이미 멤버인 경우 refetch를 publish하지 않는다")
    void execute_skipsPublishWhenAlreadyMember() {
        Long groupId = 10L;
        Actor actor = new Actor(2L);
        JoinGroupData.Command command = new JoinGroupData.Command(actor, groupId, null);
        Group group = createGroup(groupId, 1L, false);

        when(groupRepositoryPort.findById(groupId)).thenReturn(Optional.of(group));
        when(groupRepositoryPort.existsMember(groupId, actor.id())).thenReturn(true);

        assertThatThrownBy(() -> joinGroupService.execute(command))
            .isInstanceOf(AlreadyInThisGroupException.class);

        verify(groupRepositoryPort, never()).addMember(any(), any());
        verify(groupRefetchNotifier, never()).notifyGroupsChanged(any());
    }

    private Group createGroup(Long groupId, Long ownerId, boolean passwordRequired) {
        return new Group(
            groupId,
            Instant.now(),
            Instant.now(),
            "group",
            "desc",
            10,
            passwordRequired ? "encoded" : "",
            passwordRequired,
            GroupCategory.TEAM,
            ownerId
        );
    }
}
