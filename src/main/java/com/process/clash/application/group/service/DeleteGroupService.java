package com.process.clash.application.group.service;

import com.process.clash.application.group.data.DeleteGroupData;
import com.process.clash.application.group.exception.exception.notfound.GroupNotFoundException;
import com.process.clash.application.group.policy.GroupPolicy;
import com.process.clash.application.group.port.in.DeleteGroupUseCase;
import com.process.clash.application.group.port.out.GroupRepositoryPort;
import com.process.clash.application.group.realtime.GroupRefetchNotifier;
import com.process.clash.domain.group.entity.Group;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class DeleteGroupService implements DeleteGroupUseCase {

    private final GroupRepositoryPort groupRepositoryPort;
    private final GroupPolicy groupPolicy;
    private final GroupRefetchNotifier groupRefetchNotifier;

    @Override
    public void execute(DeleteGroupData.Command command) {
        Group group = groupRepositoryPort.findById(command.groupId())
            .orElseThrow(GroupNotFoundException::new);

        groupPolicy.validateOwnership(command.actor(), group);
        List<Long> memberUserIds = groupRepositoryPort.findMemberUserIdsByGroupIds(List.of(group.id()));
        groupRepositoryPort.deleteById(group.id());
        groupRefetchNotifier.notifyGroupsChanged(memberUserIds);
    }
}
