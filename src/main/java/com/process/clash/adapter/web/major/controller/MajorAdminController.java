package com.process.clash.adapter.web.major.controller;

import com.process.clash.adapter.web.common.ApiResponse;
import com.process.clash.adapter.web.major.dto.PostMajorQuestionDto;
import com.process.clash.adapter.web.major.dto.UpdateMajorQuestionDto;
import com.process.clash.adapter.web.security.AuthenticatedActor;
import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.major.data.DeleteMajorQuestionData;
import com.process.clash.application.major.data.PostMajorQuestionData;
import com.process.clash.application.major.data.UpdateMajorQuestionData;
import com.process.clash.application.major.port.in.DeleteMajorQuestionUseCase;
import com.process.clash.application.major.port.in.PostMajorQuestionUseCase;
import com.process.clash.application.major.port.in.UpdateMajorQuestionUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/admin/major")
@RequiredArgsConstructor
public class MajorAdminController {

    private final PostMajorQuestionUseCase postMajorQuestionUseCase;
    private final UpdateMajorQuestionUseCase updateMajorQuestionUseCase;
    private final DeleteMajorQuestionUseCase deleteMajorQuestionUseCase;

    @PostMapping("/questions")
    public ApiResponse<PostMajorQuestionDto.Response> postMajorQuestion(
            @AuthenticatedActor Actor actor,
            @Valid @RequestBody PostMajorQuestionDto.Request request
    ) {
        PostMajorQuestionData.Command command = request.toCommand(actor);
        PostMajorQuestionData.Result result = postMajorQuestionUseCase.execute(command);
        PostMajorQuestionDto.Response response = PostMajorQuestionDto.Response.from(result);

        return ApiResponse.created(response, "전공 질문이 성공적으로 생성되었습니다.");
    }

    @PatchMapping("/questions/{questionId}")
    public ApiResponse<UpdateMajorQuestionDto.Response> updateMajorQuestion(
            @AuthenticatedActor Actor actor,
            @PathVariable Long questionId,
            @RequestBody UpdateMajorQuestionDto.Request request
    ) {
        UpdateMajorQuestionData.Command command = request.toCommand(actor, questionId);
        UpdateMajorQuestionData.Result result = updateMajorQuestionUseCase.execute(command);
        UpdateMajorQuestionDto.Response response = UpdateMajorQuestionDto.Response.from(result);

        return ApiResponse.success(response, "전공 성향 검사 질문 수정을 성공했습니다.");
    }

    @DeleteMapping("/questions/{questionId}")
    public ApiResponse<Void> deleteMajorQuestion(
            @AuthenticatedActor Actor actor,
            @PathVariable Long questionId
    ) {
        DeleteMajorQuestionData.Command command = new DeleteMajorQuestionData.Command(actor, questionId);
        deleteMajorQuestionUseCase.execute(command);

        return ApiResponse.success("전공 성향 검사 질문 삭제를 성공했습니다.");
    }
}
