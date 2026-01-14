package com.process.clash.adapter.web.mainpage.controller;

import com.process.clash.adapter.web.common.ApiResponse;
import com.process.clash.adapter.web.mainpage.dto.mainpage.*;
import com.process.clash.adapter.web.security.AuthenticatedActor;
import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.mainpage.data.mainpage.*;
import com.process.clash.application.mainpage.port.in.mainpage.*;
import com.process.clash.domain.common.enums.PeriodCategory;
import com.process.clash.domain.common.enums.TargetCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/main")
@RequiredArgsConstructor
public class MainPageController {

    private final GetUserProfileUseCase getUserProfileUseCase;
    private final GetCompareWithYesterdayUseCase getCompareWithYesterdayUseCase;
    private final GetMyRivalActingUseCase getMyRivalActingUseCase;
    private final AnalyzeMyActivityUseCase analyzeMyActivityUseCase;
    private final GetRankingUseCase getRankingUseCase;

    // 유저 정보 조회 - 상단바 프로필 정보
    @GetMapping("/user-info")
    public ApiResponse<GetUserProfileDto.Response> getUserProfile(
            @AuthenticatedActor Actor actor
    ) {

        GetUserProfileData.Command command = GetUserProfileData.Command.from(actor);
        GetUserProfileData.Result result = getUserProfileUseCase.execute(command);
        GetUserProfileDto.Response response = GetUserProfileDto.Response.from(result);
        return ApiResponse.success(response, "유저 정보를 성공적으로 반환했습니다.");
    }

    // 어제와의 비교
    // TODO: 추가 구현 필요합니다
    @GetMapping("/compare")
    public ApiResponse<GetCompareWithYesterdayDto.Response> getCompareWithYesterday(
            @AuthenticatedActor Actor actor
    ) {

        GetCompareWithYesterdayData.Command command = GetCompareWithYesterdayData.Command.from(actor);
        GetCompareWithYesterdayData.Result result = getCompareWithYesterdayUseCase.execute(command);
        GetCompareWithYesterdayDto.Response response = GetCompareWithYesterdayDto.Response.from(result);
        return ApiResponse.success(response, "어제와의 비교 정보를 성공적으로 반환했습니다.");
    }

    // 내 라이벌 정보 조회
    // TODO: 추가 구현 필요합니다
    @GetMapping("/my-rivals")
    public ApiResponse<GetMyRivalActingDto.Response> getMyRivalActing(
            @AuthenticatedActor Actor actor
    ) {

        GetMyRivalActingData.Command command = GetMyRivalActingData.Command.from(actor);
        GetMyRivalActingData.Result result = getMyRivalActingUseCase.execute(command);
        GetMyRivalActingDto.Response response = GetMyRivalActingDto.Response.from(result);
        return ApiResponse.success(response, "라이벌의 현재 상태 정보를 성공적으로 반환했습니다.");
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

    // 대소고 랭킹
    // TODO: 추가 구현 필요합니다
    @GetMapping("/ranking/category/{category}/period/{period}")
    public ApiResponse<GetRankingDto.Response> getRanking(
            @AuthenticatedActor Actor actor,
            @PathVariable TargetCategory category,
            @PathVariable PeriodCategory period
    ) {

        GetRankingData.Command command = GetRankingData.Command.from(actor, category, period);
        GetRankingData.Result result = getRankingUseCase.execute(command);
        GetRankingDto.Response response = GetRankingDto.Response.from(result);
        return ApiResponse.success(response, "내 EXP 획득 정보 분석 결과를 성공적으로 반환했습니다.");
    }
}
