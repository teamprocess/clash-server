package com.process.clash.application.roadmap.missions.service;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.roadmap.missions.data.GetMissionResultData;
import com.process.clash.application.roadmap.missions.exception.exception.notfound.MissionNotFoundException;
import com.process.clash.application.roadmap.missions.port.in.GetMissionResultUseCase;
import com.process.clash.application.roadmap.port.out.MissionRepositoryPort;
import com.process.clash.application.roadmap.port.out.UserMissionHistoryRepositoryPort;
import com.process.clash.domain.roadmap.entity.Mission;
import com.process.clash.domain.roadmap.entity.UserMissionHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GetMissionResultService implements GetMissionResultUseCase {

    private final UserMissionHistoryRepositoryPort userMissionHistoryRepositoryPort;
    private final MissionRepositoryPort missionRepositoryPort;

    @Override
    public GetMissionResultData.Result execute(GetMissionResultData.Command command) {
        Actor actor = command.actor();
        Long missionId = command.missionId();

        // 미션 조회
        Mission mission = missionRepositoryPort.findByIdWithQuestions(missionId)
                .orElseThrow(MissionNotFoundException::new);

        // 사용자 미션 히스토리 조회
        Optional<UserMissionHistory> historyOpt = userMissionHistoryRepositoryPort.findByUserIdAndMissionId(actor.id(), missionId);

        UserMissionHistory history = historyOpt.orElseGet(() -> UserMissionHistory.create(
                actor.id(), 
                missionId, 
                Optional.ofNullable(mission.getQuestions()).map(List::size).orElse(0)
        ));

        // 다음 미션 ID 계산 (임시로 null)
        Long nextMissionId = null; // TODO: 로직 추가

        // 다음 스텝 ID 계산 (임시로 null)
        Long nextStepId = null; // TODO: 로직 추가

        return new GetMissionResultData.Result(
                missionId,
                history.isCleared(),
                history.getCorrectCount(),
                history.getTotalCount(),
                nextMissionId,
                nextStepId
        );
    }
}