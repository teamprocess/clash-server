package com.process.clash.application.group.service;

import com.process.clash.application.group.port.out.GroupRepositoryPort;
import com.process.clash.application.group.vo.GroupSummaryVo;
import com.process.clash.domain.group.entity.Group;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GroupSummaryAssembler {

    private final GroupRepositoryPort groupRepositoryPort;

    public List<GroupSummaryVo> toSummaries(List<Group> groups) {
        List<Long> groupIds = groups.stream()
            .map(Group::id)
            .toList();
        Map<Long, Integer> memberCounts = groupRepositoryPort.countMembersByGroupIds(groupIds);

        return groups.stream()
            .map(group -> GroupSummaryVo.from(group, memberCounts.getOrDefault(group.id(), 0)))
            .toList();
    }
}
