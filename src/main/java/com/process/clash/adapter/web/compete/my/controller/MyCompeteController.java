package com.process.clash.adapter.web.compete.my.controller;

import com.process.clash.adapter.web.common.ApiResponse;
import com.process.clash.adapter.web.mainpage.dto.compare.CompareGitHubDto;
import com.process.clash.adapter.web.mainpage.dto.mainpage.AnalyzeMyActivityDto;
import com.process.clash.adapter.web.mainpage.dto.mainpage.GetCompareWithYesterdayDto;
import com.process.clash.adapter.web.security.AuthenticatedActor;
import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.mainpage.data.compare.CompareGitHubData;
import com.process.clash.application.mainpage.data.mainpage.AnalyzeMyActivityData;
import com.process.clash.application.mainpage.data.mainpage.GetCompareWithYesterdayData;
import com.process.clash.application.mainpage.port.in.compare.CompareGitHubUseCase;
import com.process.clash.application.mainpage.port.in.mainpage.AnalyzeMyActivityUseCase;
import com.process.clash.application.mainpage.port.in.mainpage.GetCompareWithYesterdayUseCase;
import com.process.clash.domain.common.enums.TargetCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/compete/my")
@RequiredArgsConstructor
public class MyCompeteController {

    private final GetCompareWithYesterdayUseCase getCompareWithYesterdayUseCase;
    private final AnalyzeMyActivityUseCase analyzeMyActivityUseCase;
    private final CompareGitHubUseCase compareGitHubUseCase;

    // 어제와의 비교
    // TODO: 추가 구현 필요합니다
    @GetMapping
    public ApiResponse<GetCompareWithYesterdayDto.Response> getCompareWithYesterday(
            @AuthenticatedActor Actor actor
    ) {

        GetCompareWithYesterdayData.Command command = GetCompareWithYesterdayData.Command.from(actor);
        GetCompareWithYesterdayData.Result result = getCompareWithYesterdayUseCase.execute(command);
        GetCompareWithYesterdayDto.Response response = GetCompareWithYesterdayDto.Response.from(result);
        return ApiResponse.success(response, "어제와의 비교 정보를 성공적으로 반환했습니다.");
    }

    // 내 활동 분석
    // TODO: 추가 구현 필요합니다
    @GetMapping("/analyze-my-activity/category/{category}")
    public ApiResponse<AnalyzeMyActivityDto.Response> analyzeMyActivity(
            @AuthenticatedActor Actor actor,
            @PathVariable TargetCategory category
    ) {

        AnalyzeMyActivityData.Command command = AnalyzeMyActivityData.Command.from(actor, category);
        AnalyzeMyActivityData.Result result = analyzeMyActivityUseCase.execute(command);
        AnalyzeMyActivityDto.Response response = AnalyzeMyActivityDto.Response.from(result);
        return ApiResponse.success(response, "내 활동 분석 결과를 성공적으로 반환했습니다.");
    }

    // 어제와 비교 - GitHub
    @GetMapping("/compare/github")
    public ApiResponse<CompareGitHubDto.Response> compareGitHub(
            @AuthenticatedActor Actor actor
    ) {

        CompareGitHubData.Command command = CompareGitHubData.Command.from(actor);
        CompareGitHubData.Result result = compareGitHubUseCase.execute(command);
        CompareGitHubDto.Response response = CompareGitHubDto.Response.from(result);
        return ApiResponse.success(response, "어제와 비교한 유저의 깃허브 정보 조회를 성공적으로 완료 했습니다.");
    }
}
