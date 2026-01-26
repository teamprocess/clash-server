package com.process.clash.application.group.service;

import com.process.clash.application.common.exception.exception.ValidationException;
import com.process.clash.application.group.data.CreateGroupData;
import com.process.clash.application.group.exception.exception.badrequest.GroupPasswordRequiredException;
import com.process.clash.application.group.policy.GroupPolicy;
import com.process.clash.application.group.port.in.CreateGroupUseCase;
import com.process.clash.application.group.port.out.GroupRepositoryPort;
import com.process.clash.application.user.user.exception.exception.notfound.UserNotFoundException;
import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import com.process.clash.domain.group.entity.Group;
import com.process.clash.domain.user.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class CreateGroupService implements CreateGroupUseCase {

    private final GroupRepositoryPort groupRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoder passwordEncoder;
    private final GroupPolicy policy;

    @Override
    public void execute(CreateGroupData.Command command) {
        User owner = userRepositoryPort.findById(command.actor().id())
            .orElseThrow(UserNotFoundException::new);

        policy.validateMaxMembers(command.maxMembers());

        String password = resolvePassword(command.passwordRequired(), command.password());

        Group group = new Group(
            null,
            null,
            null,
            command.name(),
            command.description(),
            command.maxMembers(),
            password,
            command.passwordRequired(),
            command.category(),
            owner.id()
        );

        Group savedGroup = groupRepositoryPort.save(group);
        groupRepositoryPort.addMember(savedGroup.id(), owner.id());
    }

    private String resolvePassword(Boolean passwordRequired, String password) {
        if (!passwordRequired) {
            return "";
        }

        if (password == null || password.isBlank()) {
            throw new GroupPasswordRequiredException();
        }

        return passwordEncoder.encode(password);
    }
}
