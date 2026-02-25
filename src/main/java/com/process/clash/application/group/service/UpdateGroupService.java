package com.process.clash.application.group.service;

import com.process.clash.application.group.data.UpdateGroupData;
import com.process.clash.application.group.exception.exception.notfound.GroupNotFoundException;
import com.process.clash.application.group.policy.GroupPolicy;
import com.process.clash.application.group.port.in.UpdateGroupUseCase;
import com.process.clash.application.group.port.out.GroupRepositoryPort;
import com.process.clash.application.group.realtime.GroupRefetchNotifier;
import com.process.clash.domain.group.entity.Group;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdateGroupService implements UpdateGroupUseCase {

    private final GroupRepositoryPort groupRepositoryPort;
    private final GroupPolicy policy;
    private final PasswordEncoder passwordEncoder;
    private final GroupRefetchNotifier groupRefetchNotifier;

    @Override
    public void execute(UpdateGroupData.Command command) {
        Group group = groupRepositoryPort.findById(command.groupId())
            .orElseThrow(GroupNotFoundException::new);

        int currentMemberCount = groupRepositoryPort.countMembers(command.groupId());
        policy.validateOwnership(command.actor(), group);
        policy.validateMaxMembers(command.maxMembers());
        policy.validateMemberLimit(group.maxMembers(), currentMemberCount);

        String password = resolvePassword(command.passwordRequired(), command.password(), group.password());

        Group updatedGroup = new Group(
            group.id(),
            group.createdAt(),
            Instant.now(),
            command.name(),
            command.description(),
            command.maxMembers(),
            password,
            command.passwordRequired(),
            command.category(),
            group.ownerId()
        );

        groupRepositoryPort.save(updatedGroup);
        List<Long> targetUserIds = new ArrayList<>(groupRepositoryPort.findMemberUserIdsByGroupIds(List.of(group.id())));
        targetUserIds.add(command.actor().id());
        groupRefetchNotifier.notifyGroupsChanged(targetUserIds);
    }

    private String resolvePassword(Boolean passwordRequired, String password, String currentPassword) {
        if (!passwordRequired) {
            return "";
        }

        if (password == null || password.isBlank()) {
            return currentPassword;
        }

        return passwordEncoder.encode(password);
    }
}
