package com.process.clash.application.group.service;

import com.process.clash.application.common.pagination.Pagination;
import com.process.clash.application.group.data.GetMyGroupsData;
import com.process.clash.application.group.port.in.GetMyGroupsUseCase;
import com.process.clash.application.group.port.out.GroupRepositoryPort;
import com.process.clash.application.group.vo.GroupSummaryVo;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GetMyGroupsService implements GetMyGroupsUseCase {

    private static final int PAGE_SIZE = 6;

    private final GroupRepositoryPort groupRepositoryPort;
    private final GroupSummaryAssembler groupSummaryAssembler;

    @Override
    public GetMyGroupsData.Result execute(GetMyGroupsData.Command command) {
        GroupRepositoryPort.PageResult pageResult = command.category() == null
            ? groupRepositoryPort.findAllByMemberUserId(command.actor().id(), command.page(), PAGE_SIZE)
            : groupRepositoryPort.findAllByMemberUserIdAndCategory(
                command.actor().id(),
                command.page(),
                PAGE_SIZE,
                command.category()
            );
        List<GroupSummaryVo> groups = groupSummaryAssembler.toSummaries(pageResult.groups());
        Pagination pagination =
            Pagination.from(command.page(), PAGE_SIZE, pageResult.totalCount());

        return new GetMyGroupsData.Result(groups, pagination);
    }
}
