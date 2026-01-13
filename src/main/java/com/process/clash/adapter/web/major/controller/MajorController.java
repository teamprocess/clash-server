package com.process.clash.adapter.web.major.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.process.clash.adapter.web.common.ApiResponse;
import com.process.clash.adapter.web.major.dto.GetMajorQuestionDto;
import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.major.data.GetMajorQuestionData;
import com.process.clash.application.major.port.in.GetMajorQuestionUseCase;
import com.process.clash.infrastructure.principle.AuthUser;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/major")
@RequiredArgsConstructor
public class MajorController {

    private final GetMajorQuestionUseCase getMajorQuestionUseCase;

    @GetMapping("/questions")
    public ApiResponse<GetMajorQuestionDto.Response> getMajorQuestion(@AuthenticationPrincipal AuthUser authUser) {
        Actor actor = authUser.toActor();
        GetMajorQuestionData.Command command = new GetMajorQuestionData.Command(actor);
        GetMajorQuestionData.Result result = getMajorQuestionUseCase.execute(command);
        GetMajorQuestionDto.Response response = GetMajorQuestionDto.Response.from(result);
        return ApiResponse.success(response);
    }

}
