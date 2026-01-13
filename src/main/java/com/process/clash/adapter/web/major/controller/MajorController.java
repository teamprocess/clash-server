package com.process.clash.adapter.web.major.controller;

import com.process.clash.adapter.web.common.ApiResponse;
import com.process.clash.adapter.web.major.dto.GetMajorQuestionDto;
import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.major.data.GetMajorQuestionCommand;
import com.process.clash.application.major.data.GetMajorQuestionResult;
import com.process.clash.application.major.port.in.GetMajorQuestionUseCase;
import com.process.clash.infrastructure.principle.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/major")
@RequiredArgsConstructor
public class MajorController {

    private final GetMajorQuestionUseCase getMajorQuestionUseCase;

    @GetMapping("/questions")
    public ApiResponse<GetMajorQuestionDto.Response> getMajorQuestion(@AuthenticationPrincipal AuthUser authUser) {
        Actor actor = authUser.toActor();
        GetMajorQuestionCommand command = new GetMajorQuestionCommand(actor);
        GetMajorQuestionResult Result = getMajorQuestionUseCase.findAll(command);
        GetMajorQuestionDto.Response response = GetMajorQuestionDto.Response.from(Result);
        return ApiResponse.success(response);
    }
}
