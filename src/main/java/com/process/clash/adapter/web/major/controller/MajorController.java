package com.process.clash.adapter.web.major.controller;

import com.process.clash.adapter.web.common.ApiResponse;
import com.process.clash.adapter.web.major.dto.GetMajorQuestionDto;
import com.process.clash.adapter.web.major.dto.MajorTestSubmitDto;
import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.major.data.GetMajorQuestionData;
import com.process.clash.application.major.data.MajorTestSubmitData;
import com.process.clash.application.major.port.in.GetMajorQuestionUseCase;
import com.process.clash.application.major.port.in.MajorTestSubmitUseCase;
import com.process.clash.infrastructure.principle.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/major")
@RequiredArgsConstructor
public class MajorController {

    private final GetMajorQuestionUseCase getMajorQuestionUseCase;
    private final MajorTestSubmitUseCase majorTestSubmitUseCase;

    @GetMapping("/questions")
    public ApiResponse<GetMajorQuestionDto.Response> getMajorQuestion(@AuthenticationPrincipal AuthUser authUser) {
        Actor actor = authUser.toActor();
        GetMajorQuestionData.Command command = new GetMajorQuestionData.Command(actor);
        GetMajorQuestionData.Result result = getMajorQuestionUseCase.execute(command);
        GetMajorQuestionDto.Response response = GetMajorQuestionDto.Response.from(result);
        return ApiResponse.success(response, "전공 성향 검사 조회를 성공했습니다.");
    }

    @PostMapping("/test/submit")
    public ApiResponse<Void> testResultSubmit(@AuthenticationPrincipal AuthUser authUser, @RequestBody MajorTestSubmitDto.Request request) {
        Actor actor = authUser.toActor();
        MajorTestSubmitData.Command command = new MajorTestSubmitData.Command(actor, request.major());
        majorTestSubmitUseCase.execute(command);
        return ApiResponse.success("전공 성향 검사 결과 저장을 성공했습니다.");
    }
}
