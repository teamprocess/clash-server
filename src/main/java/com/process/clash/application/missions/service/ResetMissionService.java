package com.process.clash.application.missions.service;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.missions.data.ResetMissionData;
import com.process.clash.application.missions.exception.exception.notfound.MissionNotFoundException;
import com.process.clash.application.missions.port.in.ResetMissionUseCase;
import com.process.clash.application.roadmap.port.out.MissionRepositoryPort;
import com.process.clash.application.roadmap.port.out.UserMissionHistoryRepositoryPort;
import com.process.clash.domain.roadmap.entity.UserMissionHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResetMissionService implements ResetMissionUseCase {

    private final UserMissionHistoryRepositoryPort userMissionHistoryRepositoryPort;
    private final MissionRepositoryPort missionRepositoryPort;

    @Override
    public void execute(ResetMissionData.Command command) {
        Actor actor = command.actor();
        Long missionId = command.missionId();

        // 미션 존재 확인
        missionRepositoryPort.findById(missionId)
                .orElseThrow(MissionNotFoundException::new);

        // 기존 히스토리 삭제 또는 초기화
        userMissionHistoryRepositoryPort.findByUserIdAndMissionId(actor.id(), missionId)
                .ifPresent(history -> {
                    UserMissionHistory resetHistory = new UserMissionHistory(history.getId(), history.getUserId(), history.getMissionId(), false, 0, history.getTotalCount(), 0);
                    userMissionHistoryRepositoryPort.save(resetHistory);
                });
    }
}