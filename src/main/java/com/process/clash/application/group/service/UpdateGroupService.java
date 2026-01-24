package com.process.clash.application.group.service;

import com.process.clash.application.common.exception.exception.ValidationException;
import com.process.clash.application.group.data.UpdateGroupData;
import com.process.clash.application.group.exception.exception.badrequest.GroupMemberLimitTooSmallException;
import com.process.clash.application.group.exception.exception.badrequest.GroupPasswordRequiredException;
import com.process.clash.application.group.exception.exception.notfound.GroupNotFoundException;
import com.process.clash.application.group.policy.GroupPolicy;
import com.process.clash.application.group.port.in.UpdateGroupUseCase;
import com.process.clash.application.group.port.out.GroupRepositoryPort;
import com.process.clash.domain.group.entity.Group;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdateGroupService implements UpdateGroupUseCase {

    private final GroupRepositoryPort groupRepositoryPort;
    private final GroupPolicy groupPolicy;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void execute(UpdateGroupData.Command command) {
        Group group = groupRepositoryPort.findById(command.groupId())
            .orElseThrow(GroupNotFoundException::new);

        groupPolicy.validateOwnership(command.actor(), group);

        if (command.maxMembers() == null || command.maxMembers() < 1) {
            throw new ValidationException();
        }

        if (command.passwordRequired() == null) {
            throw new ValidationException();
        }

        int currentMemberCount = group.currentMemberCount() == null ? 0 : group.currentMemberCount();
        if (command.maxMembers() < currentMemberCount) {
            throw new GroupMemberLimitTooSmallException();
        }

        String password = resolvePassword(command.passwordRequired(), command.password(), group.password());

        Group updatedGroup = new Group(
            group.id(),
            group.createdAt(),
            LocalDateTime.now(),
            command.name(),
            command.description(),
            command.maxMembers(),
            currentMemberCount,
            password,
            command.passwordRequired(),
            command.category(),
            group.owner()
        );

        groupRepositoryPort.save(updatedGroup);
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
