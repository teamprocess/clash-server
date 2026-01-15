package com.process.clash.adapter.web.major.controller;

import com.process.clash.adapter.web.common.ApiResponse;
import com.process.clash.adapter.web.major.dto.GetMajorQuestionDto;
import com.process.clash.adapter.web.major.dto.MajorTestSubmitDto;
import com.process.clash.adapter.web.security.AuthenticatedActor;
import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.major.data.GetMajorQuestionData;
import com.process.clash.application.major.data.MajorSubmitData;
import com.process.clash.application.major.port.in.GetMajorQuestionUseCase;
import com.process.clash.application.major.port.in.MajorTestSubmitUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/major")
@RequiredArgsConstructor
public class MajorController {

    private final GetMajorQuestionUseCase getMajorQuestionUseCase;
    private final MajorTestSubmitUseCase majorTestSubmitUseCase;

    @GetMapping("/questions")
    public ApiResponse<GetMajorQuestionDto.Response> getMajorQuestion(@AuthenticatedActor Actor actor) {
        GetMajorQuestionData.Command command = new GetMajorQuestionData.Command(actor);
        GetMajorQuestionData.Result result = getMajorQuestionUseCase.execute(command);
        GetMajorQuestionDto.Response response = GetMajorQuestionDto.Response.from(result);
        return ApiResponse.success(response, "전공 성향 검사 조회를 성공했습니다.");
    }

    @PostMapping("/test/submit")
    public ApiResponse<Void> testResultSubmit(@AuthenticatedActor Actor actor, @RequestBody MajorTestSubmitDto.Request request) {
        MajorSubmitData.Command command = new MajorSubmitData.Command(actor, request.major());
        majorTestSubmitUseCase.execute(command);
        return ApiResponse.success("전공 성향 검사 결과 저장을 성공했습니다.");
    }
}
