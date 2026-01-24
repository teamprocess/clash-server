package com.process.clash.application.group.service;

import com.process.clash.application.common.pagination.Pagination;
import com.process.clash.application.group.data.GetAllGroupsData;
import com.process.clash.application.group.port.in.GetAllGroupsUseCase;
import com.process.clash.application.group.port.out.GroupRepositoryPort;
import com.process.clash.application.group.vo.GroupSummaryVo;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetAllGroupsService implements GetAllGroupsUseCase {

    private static final int PAGE_SIZE = 10;

    private final GroupRepositoryPort groupRepositoryPort;

    @Override
    public GetAllGroupsData.Result execute(GetAllGroupsData.Command command) {
        GroupRepositoryPort.PageResult pageResult = groupRepositoryPort.findAllByPage(command.page(), PAGE_SIZE);
        List<GroupSummaryVo> groups = pageResult.groups().stream()
            .map(GroupSummaryVo::from)
            .toList();
        Pagination pagination =
            Pagination.from(command.page(), PAGE_SIZE, pageResult.totalCount());
        // Only check membership for the current page to avoid loading all group IDs.
        List<Long> pageGroupIds = groups.stream()
            .map(GroupSummaryVo::id)
            .toList();
        Set<Long> memberGroupIds = new HashSet<>(groupRepositoryPort
            .findGroupIdsByMemberUserIdAndGroupIds(command.actor().id(), pageGroupIds));

        return new GetAllGroupsData.Result(groups, pagination, memberGroupIds);
    }
}
