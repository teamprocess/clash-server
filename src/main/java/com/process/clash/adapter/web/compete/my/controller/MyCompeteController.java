package com.process.clash.adapter.web.compete.my.controller;

import com.process.clash.adapter.web.common.ApiResponse;
import com.process.clash.adapter.web.compete.my.docs.controller.MyCompeteControllerDocument;
import com.process.clash.adapter.web.compete.my.dto.*;
import com.process.clash.adapter.web.security.AuthenticatedActor;
import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.compete.my.data.*;
import com.process.clash.application.compete.my.port.in.*;
import com.process.clash.domain.common.enums.TargetCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/compete/my")
@RequiredArgsConstructor
public class MyCompeteController implements MyCompeteControllerDocument {

    private final GetCompareWithYesterdayUseCase getCompareWithYesterdayUseCase;
    private final AnalyzeMyActivityUseCase analyzeMyActivityUseCase;
    private final CompareGitHubUseCase compareGitHubUseCase;
    private final CompareMyActivityUseCase compareMyActivityUseCase;
    private final GetMyGrowthRateUseCase getMyGrowthRateUseCase;

    // 어제와의 비교
    @GetMapping("/compare/yesterday")
    public ApiResponse<GetCompareWithYesterdayDto.Response> getCompareWithYesterday(
            @AuthenticatedActor Actor actor
    ) {

        GetCompareWithYesterdayData.Command command = GetCompareWithYesterdayData.Command.from(actor);
        GetCompareWithYesterdayData.Result result = getCompareWithYesterdayUseCase.execute(command);
        GetCompareWithYesterdayDto.Response response = GetCompareWithYesterdayDto.Response.from(result);
        return ApiResponse.success(response, "어제와의 비교 정보를 성공적으로 반환했습니다.");
    }

    // 어제와 비교 - GitHub
    @GetMapping("/compare/yesterday/github")
    public ApiResponse<CompareGitHubDto.Response> compareGitHub(
            @AuthenticatedActor Actor actor
    ) {

        CompareGitHubData.Command command = CompareGitHubData.Command.from(actor);
        CompareGitHubData.Result result = compareGitHubUseCase.execute(command);
        CompareGitHubDto.Response response = CompareGitHubDto.Response.from(result);
        return ApiResponse.success(response, "어제와 비교한 유저의 깃허브 정보 조회를 성공적으로 완료 했습니다.");
    }

    // 내 활동 분석
    @GetMapping("/analyze/category/{category}")
    public ApiResponse<AnalyzeMyActivityDto.Response> analyzeMyActivity(
            @AuthenticatedActor Actor actor,
            @PathVariable TargetCategory category
    ) {

        AnalyzeMyActivityData.Command command = AnalyzeMyActivityData.Command.of(actor, category);
        AnalyzeMyActivityData.Result result = analyzeMyActivityUseCase.execute(command);
        AnalyzeMyActivityDto.Response response = AnalyzeMyActivityDto.Response.from(result);
        return ApiResponse.success(response, "내 활동 분석 결과를 성공적으로 반환했습니다.");
    }

    // 나와의 경쟁 - 내 성장도 분석
    @GetMapping("/growth-rate")
    public ApiResponse<GetMyGrowthRateDto.Response> getMyGrowthRate(
            @AuthenticatedActor Actor actor,
            @RequestParam String standard // 서비스에서 예외처리 합니다
    ) {

        GetMyGrowthRateData.Command command = GetMyGrowthRateData.Command.of(actor, standard);
        GetMyGrowthRateData.Result result = getMyGrowthRateUseCase.execute(command);
        GetMyGrowthRateDto.Response response = GetMyGrowthRateDto.Response.from(result);
        return ApiResponse.success(response, "내 기록을 성공적으로 반환했습니다.");
    }

    // 나와의 경쟁 - 내 기록 비교
    @GetMapping("/compare")
    public ApiResponse<CompareMyActivityDto.Response> compareMyActivity(
            @AuthenticatedActor Actor actor,
            @RequestParam String standard // 서비스에서 예외처리 합니다
    ) {

        CompareMyActivityData.Command command = CompareMyActivityData.Command.of(actor, standard);
        CompareMyActivityData.Result result = compareMyActivityUseCase.execute(command);
        CompareMyActivityDto.Response response = CompareMyActivityDto.Response.from(result);
        return ApiResponse.success(response, "내 기록 비교 정보를 성공적으로 반환했습니다.");
    }
}
