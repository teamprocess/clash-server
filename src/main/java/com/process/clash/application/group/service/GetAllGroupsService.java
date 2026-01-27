package com.process.clash.application.group.service;

import com.process.clash.application.common.pagination.Pagination;
import com.process.clash.application.group.data.GetAllGroupsData;
import com.process.clash.application.group.port.in.GetAllGroupsUseCase;
import com.process.clash.application.group.port.out.GroupRepositoryPort;
import com.process.clash.application.group.vo.GroupSummaryVo;
import com.process.clash.domain.group.entity.Group;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GetAllGroupsService implements GetAllGroupsUseCase {

    private static final int PAGE_SIZE = 6;

    private final GroupRepositoryPort groupRepositoryPort;
    private final GroupSummaryAssembler groupSummaryAssembler;

    @Override
    public GetAllGroupsData.Result execute(GetAllGroupsData.Command command) {
        GroupRepositoryPort.PageResult pageResult = groupRepositoryPort.findAllByPage(command.page(), PAGE_SIZE);
        List<Long> pageGroupIds = pageResult.groups().stream()
            .map(Group::id)
            .toList();
        List<GroupSummaryVo> groups = groupSummaryAssembler.toSummaries(pageResult.groups());
        Pagination pagination =
            Pagination.from(command.page(), PAGE_SIZE, pageResult.totalCount());
        Set<Long> memberGroupIds = new HashSet<>(groupRepositoryPort
            .findGroupIdsByMemberUserIdAndGroupIds(command.actor().id(), pageGroupIds));

        return new GetAllGroupsData.Result(groups, pagination, memberGroupIds);
    }
}
