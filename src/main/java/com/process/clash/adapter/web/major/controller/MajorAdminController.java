package com.process.clash.adapter.web.major.controller;

import com.process.clash.adapter.web.common.ApiResponse;
import com.process.clash.adapter.web.major.dto.PostMajorQuestionDto;
import com.process.clash.adapter.web.security.AuthenticatedActor;
import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.major.data.PostMajorQuestionData;
import com.process.clash.application.major.port.in.PostMajorQuestionUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/major")
@RequiredArgsConstructor
public class MajorAdminController {

    private final PostMajorQuestionUseCase postMajorQuestionUseCase;

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
}
