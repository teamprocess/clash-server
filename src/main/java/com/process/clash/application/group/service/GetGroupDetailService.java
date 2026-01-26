package com.process.clash.application.group.service;

import com.process.clash.application.group.data.GetGroupDetailData;
import com.process.clash.application.group.exception.exception.notfound.GroupNotFoundException;
import com.process.clash.application.group.port.in.GetGroupDetailUseCase;
import com.process.clash.application.group.port.out.GroupRepositoryPort;
import com.process.clash.application.group.vo.GroupSummaryVo;
import com.process.clash.domain.group.entity.Group;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GetGroupDetailService implements GetGroupDetailUseCase {

    private final GroupRepositoryPort groupRepositoryPort;
    private final GroupSummaryAssembler groupSummaryAssembler;

    @Override
    public GetGroupDetailData.Result execute(GetGroupDetailData.Command command) {
        Group group = groupRepositoryPort.findById(command.groupId())
            .orElseThrow(GroupNotFoundException::new);
        GroupSummaryVo summary = groupSummaryAssembler.toSummary(group);
        boolean isMember = groupRepositoryPort.existsMember(group.id(), command.actor().id());

        return new GetGroupDetailData.Result(summary, isMember);
    }
}
