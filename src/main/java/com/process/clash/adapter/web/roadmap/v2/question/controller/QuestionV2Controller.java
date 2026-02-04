package com.process.clash.adapter.web.roadmap.v2.question.controller;

import com.process.clash.adapter.web.common.ApiResponse;
import com.process.clash.adapter.web.roadmap.v2.question.docs.controller.QuestionV2ControllerDocument;
import com.process.clash.adapter.web.roadmap.v2.question.dto.GetChapterV2ResultDto;
import com.process.clash.adapter.web.roadmap.v2.question.dto.SubmitQuestionV2AnswerDto;
import com.process.clash.adapter.web.security.AuthenticatedActor;
import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.roadmap.v2.question.data.GetChapterV2ResultData;
import com.process.clash.application.roadmap.v2.question.data.ResetChapterV2Data;
import com.process.clash.application.roadmap.v2.question.data.SubmitQuestionV2AnswerData;
import com.process.clash.application.roadmap.v2.question.port.in.GetChapterV2ResultUseCase;
import com.process.clash.application.roadmap.v2.question.port.in.ResetChapterV2UseCase;
import com.process.clash.application.roadmap.v2.question.port.in.SubmitQuestionV2AnswerUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2")
@RequiredArgsConstructor
public class QuestionV2Controller implements QuestionV2ControllerDocument {

    private final SubmitQuestionV2AnswerUseCase submitQuestionV2AnswerUseCase;
    private final GetChapterV2ResultUseCase getChapterV2ResultUseCase;
    private final ResetChapterV2UseCase resetChapterV2UseCase;

    @PostMapping("/questions/{questionId}/submit")
    public ApiResponse<SubmitQuestionV2AnswerDto.Response> submitAnswer(
            @AuthenticatedActor Actor actor,
            @PathVariable Long questionId,
            @Valid @RequestBody SubmitQuestionV2AnswerDto.Request request
    ) {
        SubmitQuestionV2AnswerData.Command command = request.toCommand(actor, questionId);
        SubmitQuestionV2AnswerData.Result result = submitQuestionV2AnswerUseCase.execute(command);
        SubmitQuestionV2AnswerDto.Response response = SubmitQuestionV2AnswerDto.Response.from(result);
        return ApiResponse.success(response, "답안 제출을 성공했습니다.");
    }

    @PostMapping("/chapters/{chapterId}/result")
    public ApiResponse<GetChapterV2ResultDto.Response> getChapterResult(
            @AuthenticatedActor Actor actor,
            @PathVariable Long chapterId
    ) {
        GetChapterV2ResultData.Command command = new GetChapterV2ResultData.Command(actor, chapterId);
        GetChapterV2ResultData.Result result = getChapterV2ResultUseCase.execute(command);
        GetChapterV2ResultDto.Response response = GetChapterV2ResultDto.Response.from(result);
        return ApiResponse.success(response, "챕터 결과 조회를 성공했습니다.");
    }

    @PostMapping("/chapters/{chapterId}/reset")
    public ApiResponse<Void> resetChapter(
            @AuthenticatedActor Actor actor,
            @PathVariable Long chapterId
    ) {
        ResetChapterV2Data.Command command = new ResetChapterV2Data.Command(actor, chapterId);
        resetChapterV2UseCase.execute(command);
        return ApiResponse.success("챕터 진행도 초기화를 성공했습니다.");
    }
}
