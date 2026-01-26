package com.process.clash.application.group.service;

import com.process.clash.application.group.data.QuitGroupData;
import com.process.clash.application.group.exception.exception.badrequest.GroupNotMemberException;
import com.process.clash.application.group.exception.exception.conflict.GroupOwnerCannotQuitException;
import com.process.clash.application.group.exception.exception.notfound.GroupNotFoundException;
import com.process.clash.application.group.port.in.QuitGroupUseCase;
import com.process.clash.application.group.port.out.GroupRepositoryPort;
import com.process.clash.domain.group.entity.Group;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class QuitGroupService implements QuitGroupUseCase {

    private final GroupRepositoryPort groupRepositoryPort;

    @Override
    public void execute(QuitGroupData.Command command) {
        Group group = groupRepositoryPort.findById(command.groupId())
            .orElseThrow(GroupNotFoundException::new);

        if (group.ownerId().equals(command.actor().id())) {
            throw new GroupOwnerCannotQuitException();
        }

        if (!groupRepositoryPort.existsMember(command.groupId(), command.actor().id())) {
            throw new GroupNotMemberException();
        }

        groupRepositoryPort.removeMember(command.groupId(), command.actor().id());
    }
}
