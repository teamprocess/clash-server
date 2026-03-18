package com.process.clash.application.group.service;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.group.data.CreateGroupData;
import com.process.clash.application.group.exception.exception.badrequest.GroupPasswordRequiredException;
import com.process.clash.application.group.exception.exception.conflict.GroupDuplicateNameException;
import com.process.clash.application.group.policy.GroupPolicy;
import com.process.clash.application.group.port.out.GroupRepositoryPort;
import com.process.clash.application.group.realtime.GroupRefetchNotifier;
import com.process.clash.application.user.user.exception.exception.notfound.UserNotFoundException;
import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import com.process.clash.domain.common.enums.Major;
import com.process.clash.domain.group.entity.Group;
import com.process.clash.domain.group.enums.GroupCategory;
import com.process.clash.domain.user.user.entity.User;
import com.process.clash.domain.user.user.enums.Role;
import com.process.clash.domain.user.user.enums.UserStatus;
import com.process.clash.domain.user.userrankhistory.enums.ExpTier;
import com.process.clash.domain.user.userrankhistory.enums.RankTier;
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
class CreateGroupServiceTest {

    @Mock
    private GroupRepositoryPort groupRepositoryPort;

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private GroupRefetchNotifier groupRefetchNotifier;

    private CreateGroupService createGroupService;

    @BeforeEach
    void setUp() {
        createGroupService = new CreateGroupService(
            groupRepositoryPort,
            userRepositoryPort,
            passwordEncoder,
            new GroupPolicy(),
            groupRefetchNotifier
        );
    }

    @Test
    @DisplayName("그룹 생성 성공 시 그룹을 저장하고 오너를 멤버로 추가한 뒤 refetch를 publish한다")
    void execute_savesGroupAndNotifiesOwner() {
        Long userId = 1L;
        Actor actor = new Actor(userId);
        CreateGroupData.Command command = command(actor, "새 그룹", false, null);
        User owner = createUser(userId);
        Group savedGroup = createGroup(100L, userId);

        when(userRepositoryPort.findById(userId)).thenReturn(Optional.of(owner));
        when(groupRepositoryPort.existsByName("새 그룹")).thenReturn(false);
        when(groupRepositoryPort.save(any(Group.class))).thenReturn(savedGroup);

        createGroupService.execute(command);

        verify(groupRepositoryPort).save(any(Group.class));
        verify(groupRepositoryPort).addMember(100L, userId);
        verify(groupRefetchNotifier).notifyMyGroupsChanged(List.of(userId));
    }

    @Test
    @DisplayName("비밀번호가 필요한 그룹 생성 시 비밀번호를 인코딩하여 저장한다")
    void execute_encodesPasswordWhenPasswordRequired() {
        Long userId = 1L;
        Actor actor = new Actor(userId);
        CreateGroupData.Command command = command(actor, "새 그룹", true, "secret");
        User owner = createUser(userId);
        Group savedGroup = createGroup(100L, userId);

        when(userRepositoryPort.findById(userId)).thenReturn(Optional.of(owner));
        when(groupRepositoryPort.existsByName("새 그룹")).thenReturn(false);
        when(passwordEncoder.encode("secret")).thenReturn("encoded");
        when(groupRepositoryPort.save(any(Group.class))).thenReturn(savedGroup);

        createGroupService.execute(command);

        verify(passwordEncoder).encode("secret");
        verify(groupRepositoryPort).save(any(Group.class));
    }

    @Test
    @DisplayName("이미 존재하는 그룹 이름이면 GroupDuplicateNameException이 발생한다")
    void execute_throwsWhenGroupNameDuplicated() {
        Long userId = 1L;
        Actor actor = new Actor(userId);
        CreateGroupData.Command command = command(actor, "중복 이름", false, null);

        when(userRepositoryPort.findById(userId)).thenReturn(Optional.of(createUser(userId)));
        when(groupRepositoryPort.existsByName("중복 이름")).thenReturn(true);

        assertThatThrownBy(() -> createGroupService.execute(command))
            .isInstanceOf(GroupDuplicateNameException.class);

        verify(groupRepositoryPort, never()).save(any());
        verify(groupRefetchNotifier, never()).notifyMyGroupsChanged(any());
    }

    @Test
    @DisplayName("존재하지 않는 유저면 UserNotFoundException이 발생한다")
    void execute_throwsWhenUserNotFound() {
        Actor actor = new Actor(1L);
        CreateGroupData.Command command = command(actor, "새 그룹", false, null);

        when(userRepositoryPort.findById(actor.id())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> createGroupService.execute(command))
            .isInstanceOf(UserNotFoundException.class);

        verify(groupRepositoryPort, never()).save(any());
    }

    @Test
    @DisplayName("비밀번호 필요 그룹에 비밀번호를 입력하지 않으면 GroupPasswordRequiredException이 발생한다")
    void execute_throwsWhenPasswordRequiredButNotProvided() {
        Long userId = 1L;
        Actor actor = new Actor(userId);
        CreateGroupData.Command command = command(actor, "새 그룹", true, null);

        when(userRepositoryPort.findById(userId)).thenReturn(Optional.of(createUser(userId)));
        when(groupRepositoryPort.existsByName("새 그룹")).thenReturn(false);

        assertThatThrownBy(() -> createGroupService.execute(command))
            .isInstanceOf(GroupPasswordRequiredException.class);

        verify(groupRepositoryPort, never()).save(any());
    }

    private CreateGroupData.Command command(Actor actor, String name, boolean passwordRequired, String password) {
        return new CreateGroupData.Command(actor, name, "설명", 10, GroupCategory.TEAM, passwordRequired, password);
    }

    private Group createGroup(Long id, Long ownerId) {
        return new Group(id, Instant.now(), Instant.now(), "새 그룹", "설명", 10, "", false, GroupCategory.TEAM, ownerId);
    }

    private User createUser(Long id) {
        return new User(
            id, Instant.now(), Instant.now(),
            "testuser", "test@example.com", "테스트유저", "password",
            Role.USER, "", 0, 0, Major.NONE, UserStatus.ACTIVE, null,
            RankTier.NONE, ExpTier.UNRANKED
        );
    }
}