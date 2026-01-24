package com.process.clash.application.group.service;

import com.process.clash.application.common.pagination.Pagination;
import com.process.clash.application.group.data.GetMyGroupsData;
import com.process.clash.application.group.port.in.GetMyGroupsUseCase;
import com.process.clash.application.group.port.out.GroupRepositoryPort;
import com.process.clash.application.group.vo.GroupSummaryVo;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetMyGroupsService implements GetMyGroupsUseCase {

    private static final int PAGE_SIZE = 10;

    private final GroupRepositoryPort groupRepositoryPort;

    @Override
    public GetMyGroupsData.Result execute(GetMyGroupsData.Command command) {
        GroupRepositoryPort.PageResult pageResult =
            groupRepositoryPort.findAllByMemberUserId(command.actor().id(), command.page(), PAGE_SIZE);
        List<GroupSummaryVo> groups = pageResult.groups().stream()
            .map(GroupSummaryVo::from)
            .toList();
        Pagination pagination =
            Pagination.from(command.page(), PAGE_SIZE, pageResult.totalCount());

        return new GetMyGroupsData.Result(groups, pagination);
    }
}
