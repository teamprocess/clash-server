package com.process.clash.adapter.web.missions.controller;

import com.process.clash.adapter.web.common.ApiResponse;
import com.process.clash.adapter.web.missions.docs.controller.MissionControllerDocument;
import com.process.clash.adapter.web.missions.dto.MissionSubmitDto;
import com.process.clash.adapter.web.security.AuthenticatedActor;
import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.missions.port.in.SubmitMissionAnswerUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/missions")
@RequiredArgsConstructor
public class MissionController implements MissionControllerDocument {

    private final SubmitMissionAnswerUseCase submitMissionAnswerUseCase;

    @PostMapping("/{missionId}/questions/{questionId}/submit")
    public ApiResponse<MissionSubmitDto.Response> submitAnswer(
            @AuthenticatedActor Actor actor,
            @PathVariable Long missionId,
            @PathVariable Long questionId,
            @RequestBody MissionSubmitDto.Request request
    ) {
        SubmitMissionAnswerUseCase.Command command = new SubmitMissionAnswerUseCase.Command(
                actor,
                missionId,
                questionId,
                request.submittedChoiceId()
        );

        SubmitMissionAnswerUseCase.Result result = submitMissionAnswerUseCase.execute(command);

        MissionSubmitDto.Response response = new MissionSubmitDto.Response(
                result.isCorrect(),
                result.explanation(),
                result.currentProgress(),
                result.totalQuestion(),
                result.correctChoiceId()
        );

        return ApiResponse.success(response, "정답 제출을 성공했습니다.");
    }
}