package com.process.clash.adapter.web.missions.controller;

import com.process.clash.adapter.web.common.ApiResponse;
import com.process.clash.adapter.web.missions.docs.controller.MissionControllerDocument;
import com.process.clash.adapter.web.missions.dto.MissionResultDto;
import com.process.clash.adapter.web.missions.dto.MissionSubmitDto;
import com.process.clash.adapter.web.security.AuthenticatedActor;
import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.missions.data.GetMissionResultData;
import com.process.clash.application.missions.data.ResetMissionData;
import com.process.clash.application.missions.data.SubmitMissionAnswerData;
import com.process.clash.application.missions.port.in.GetMissionResultUseCase;
import com.process.clash.application.missions.port.in.ResetMissionUseCase;
import com.process.clash.application.missions.port.in.SubmitMissionAnswerUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/missions")
@RequiredArgsConstructor
public class MissionController implements MissionControllerDocument {

    private final SubmitMissionAnswerUseCase submitMissionAnswerUseCase;
    private final GetMissionResultUseCase getMissionResultUseCase;
    private final ResetMissionUseCase resetMissionUseCase;

    @PostMapping("/{missionId}/questions/{questionId}/submit")
    public ApiResponse<MissionSubmitDto.Response> submitAnswer(
            @AuthenticatedActor Actor actor,
            @PathVariable Long missionId,
            @PathVariable Long questionId,
            @RequestBody MissionSubmitDto.Request request
    ) {
        SubmitMissionAnswerData.Command command = new SubmitMissionAnswerData.Command(
                actor,
                missionId,
                questionId,
                request.submittedChoiceId()
        );

        SubmitMissionAnswerData.Result result = submitMissionAnswerUseCase.execute(command);

        MissionSubmitDto.Response response = MissionSubmitDto.Response.fromResult(result);

        return ApiResponse.success(response, "정답 제출을 성공했습니다.");
    }

    @PostMapping("/{missionId}/result")
    public ApiResponse<MissionResultDto.Response> getResult(
            @AuthenticatedActor Actor actor,
            @PathVariable Long missionId
    ) {
        GetMissionResultData.Command command = new GetMissionResultData.Command(actor, missionId);
        GetMissionResultData.Result result = getMissionResultUseCase.execute(command);

        MissionResultDto.Response response = MissionResultDto.Response.fromResult(result);

        return ApiResponse.success(response, "미션 결과 보기를 성공했습니다.");
    }

    @PostMapping("/{missionId}/reset")
    public ApiResponse<Void> resetMission(
            @AuthenticatedActor Actor actor,
            @PathVariable Long missionId
    ) {
        ResetMissionData.Command command = new ResetMissionData.Command(actor, missionId);
        resetMissionUseCase.execute(command);

        return ApiResponse.success("미션 진행 상황이 초기화되었습니다. 다시 시작합니다.");
    }
}