package com.process.clash.adapter.web.roadmap.v2.question.controller;

import com.process.clash.adapter.web.common.ApiResponse;
import com.process.clash.adapter.web.roadmap.v2.question.dto.CreateQuestionV2Dto;
import com.process.clash.adapter.web.roadmap.v2.question.dto.UpdateQuestionV2Dto;
import com.process.clash.adapter.web.security.AuthenticatedActor;
import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.roadmap.v2.question.data.CreateQuestionV2Data;
import com.process.clash.application.roadmap.v2.question.data.DeleteQuestionV2Data;
import com.process.clash.application.roadmap.v2.question.data.UpdateQuestionV2Data;
import com.process.clash.application.roadmap.v2.question.port.in.CreateQuestionV2UseCase;
import com.process.clash.application.roadmap.v2.question.port.in.DeleteQuestionV2UseCase;
import com.process.clash.application.roadmap.v2.question.port.in.UpdateQuestionV2UseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/admin/questions")
@RequiredArgsConstructor
public class AdminQuestionV2Controller {

    private final CreateQuestionV2UseCase createQuestionV2UseCase;
    private final UpdateQuestionV2UseCase updateQuestionV2UseCase;
    private final DeleteQuestionV2UseCase deleteQuestionV2UseCase;

    @PostMapping
    public ApiResponse<CreateQuestionV2Dto.Response> createQuestion(
            @AuthenticatedActor Actor actor,
            @Valid @RequestBody CreateQuestionV2Dto.Request request
    ) {
        CreateQuestionV2Data.Command command = request.toCommand(actor);
        CreateQuestionV2Data.Result result = createQuestionV2UseCase.execute(command);
        CreateQuestionV2Dto.Response response = CreateQuestionV2Dto.Response.from(result);
        return ApiResponse.created(response, "문제 생성을 성공했습니다.");
    }

    @PatchMapping("/{questionId}")
    public ApiResponse<UpdateQuestionV2Dto.Response> updateQuestion(
            @AuthenticatedActor Actor actor,
            @PathVariable Long questionId,
            @Valid @RequestBody UpdateQuestionV2Dto.Request request
    ) {
        UpdateQuestionV2Data.Command command = request.toCommand(actor, questionId);
        UpdateQuestionV2Data.Result result = updateQuestionV2UseCase.execute(command);
        UpdateQuestionV2Dto.Response response = UpdateQuestionV2Dto.Response.from(result);
        return ApiResponse.success(response, "문제 수정을 성공했습니다.");
    }

    @DeleteMapping("/{questionId}")
    public ApiResponse<Void> deleteQuestion(
            @AuthenticatedActor Actor actor,
            @PathVariable Long questionId
    ) {
        DeleteQuestionV2Data.Command command = new DeleteQuestionV2Data.Command(actor, questionId);
        deleteQuestionV2UseCase.execute(command);
        return ApiResponse.success("문제 삭제를 성공했습니다. (연관된 선택지도 함께 삭제됩니다)");
    }
}
