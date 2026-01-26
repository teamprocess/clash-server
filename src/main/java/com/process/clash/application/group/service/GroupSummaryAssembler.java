package com.process.clash.application.group.service;

import com.process.clash.application.group.port.out.GroupRepositoryPort;
import com.process.clash.application.group.vo.GroupSummaryVo;
import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import com.process.clash.domain.group.entity.Group;
import com.process.clash.domain.user.user.entity.User;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GroupSummaryAssembler {

    private final GroupRepositoryPort groupRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;

    public List<GroupSummaryVo> toSummaries(List<Group> groups) {
        List<Long> groupIds = groups.stream()
            .map(Group::id)
            .toList();
        Map<Long, Integer> memberCounts = groupRepositoryPort.countMembersByGroupIds(groupIds);
        Map<Long, User> groupOwners = userRepositoryPort.findAllByIds(
            groups.stream()
                .map(Group::ownerId)
                .toList()
        ).stream().collect(Collectors.toMap(User::id, user -> user));

        return groups.stream()
            .map(group -> GroupSummaryVo.from(group, groupOwners.getOrDefault(group.ownerId(), null), memberCounts.getOrDefault(group.id(), 0)))
            .toList();
    }
}
