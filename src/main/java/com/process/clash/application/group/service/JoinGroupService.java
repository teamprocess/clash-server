package com.process.clash.application.group.service;

import com.process.clash.application.group.data.JoinGroupData;
import com.process.clash.application.group.exception.exception.badrequest.GroupPasswordMismatchException;
import com.process.clash.application.group.exception.exception.badrequest.GroupPasswordRequiredException;
import com.process.clash.application.group.exception.exception.conflict.AlreadyInThisGroupException;
import com.process.clash.application.group.exception.exception.conflict.GroupMemberLimitReachedException;
import com.process.clash.application.group.exception.exception.notfound.GroupNotFoundException;
import com.process.clash.application.group.policy.GroupPolicy;
import com.process.clash.application.group.port.in.JoinGroupUseCase;
import com.process.clash.application.group.port.out.GroupRepositoryPort;
import com.process.clash.application.group.realtime.GroupRefetchNotifier;
import com.process.clash.domain.group.entity.Group;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class JoinGroupService implements JoinGroupUseCase {

    private final GroupRepositoryPort groupRepositoryPort;
    private final PasswordEncoder passwordEncoder;
    private final GroupPolicy policy;
    private final GroupRefetchNotifier groupRefetchNotifier;

    @Override
    public void execute(JoinGroupData.Command command) {
        Group group = groupRepositoryPort.findById(command.groupId())
            .orElseThrow(GroupNotFoundException::new);

        if (groupRepositoryPort.existsMember(command.groupId(), command.actor().id())) {
            throw new AlreadyInThisGroupException();
        }

        int currentMemberCount = groupRepositoryPort.countMembers(command.groupId());
        policy.validateMemberLimit(group.maxMembers(), currentMemberCount);

        validatePassword(group, command.password());

        groupRepositoryPort.addMember(command.groupId(), command.actor().id());
        List<Long> memberUserIds = groupRepositoryPort.findMemberUserIdsByGroupIds(List.of(command.groupId()));
        groupRefetchNotifier.notifyGroupsChanged(memberUserIds);
    }

    private void validatePassword(Group group, String password) {
        if (!group.passwordRequired()) {
            return;
        }

        if (password == null || password.isBlank()) {
            throw new GroupPasswordRequiredException();
        }

        if (!passwordEncoder.matches(password, group.password())) {
            throw new GroupPasswordMismatchException();
        }
    }
}
