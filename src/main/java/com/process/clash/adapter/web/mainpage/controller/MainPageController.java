package com.process.clash.adapter.web.mainpage.controller;

import com.process.clash.adapter.web.common.ApiResponse;
import com.process.clash.adapter.web.mainpage.dto.compare.CompareGitHubDto;
import com.process.clash.adapter.web.mainpage.dto.mainpage.*;
import com.process.clash.adapter.web.mainpage.dto.rival.AddRivalDto;
import com.process.clash.adapter.web.mainpage.dto.rival.GetAllAbleRivalsDto;
import com.process.clash.adapter.web.mainpage.dto.rival.SearchRivalByKeywordDto;
import com.process.clash.adapter.web.security.AuthenticatedActor;
import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.mainpage.data.compare.CompareGitHubData;
import com.process.clash.application.mainpage.data.mainpage.*;
import com.process.clash.application.mainpage.data.rival.AddRivalData;
import com.process.clash.application.mainpage.data.rival.GetAllAbleRivalsData;
import com.process.clash.application.mainpage.data.rival.SearchRivalByKeywordData;
import com.process.clash.application.mainpage.port.in.compare.CompareGitHubUseCase;
import com.process.clash.application.mainpage.port.in.mainpage.*;
import com.process.clash.application.mainpage.port.in.rival.AddRivalUseCase;
import com.process.clash.application.mainpage.port.in.rival.GetAllAbleRivalsUseCase;
import com.process.clash.application.mainpage.port.in.rival.SearchRivalByKeywordUseCase;
import com.process.clash.domain.common.enums.PeriodCategory;
import com.process.clash.domain.common.enums.TargetCategory;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/main")
@RequiredArgsConstructor
public class MainPageController {

    private final GetUserProfileUseCase getUserProfileUseCase;
    private final GetCompareWithYesterdayUseCase getCompareWithYesterdayUseCase;
    private final GetMyRivalActingUseCase getMyRivalActingUseCase;
    private final AnalyzeMyActivityUseCase analyzeMyActivityUseCase;
    private final GetRankingUseCase getRankingUseCase;

    private final CompareGitHubUseCase compareGitHubUseCase;

    private final GetAllAbleRivalsUseCase getAllAbleRivalsUseCase;
    private final SearchRivalByKeywordUseCase searchRivalByKeywordUseCase;
    private final AddRivalUseCase addRivalUseCase;

    /* MainPage 부분 */
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

    /* Compare 부분 */
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

    /* Rival 부분 */
    // 라이벌 - 전체 유저 조회(내 라이벌 제외)
    @GetMapping("/rival/fresh-user")
    public ApiResponse<GetAllAbleRivalsDto.Response> getAllAbleRivals(
            @AuthenticatedActor Actor actor
    ) {

        GetAllAbleRivalsData.Command command = GetAllAbleRivalsData.Command.from(actor);
        GetAllAbleRivalsData.Result result = getAllAbleRivalsUseCase.execute(command);
        GetAllAbleRivalsDto.Response response = GetAllAbleRivalsDto.Response.from(result);
        return ApiResponse.success(response, "라이벌 등록 가능한 유저 목록을 성공적으로 조회했습니다.");
    }

    // 라이벌 - 키워드 검색
    @GetMapping("/rival/keyword/{keyword}")
    public ApiResponse<SearchRivalByKeywordDto.Response> searchRivalByKeyword(
            @AuthenticatedActor Actor actor,
            @PathVariable String keyword
    ) {

        SearchRivalByKeywordData.Command command = SearchRivalByKeywordData.Command.from(actor, keyword);
        SearchRivalByKeywordData.Result result = searchRivalByKeywordUseCase.execute(command);
        SearchRivalByKeywordDto.Response response = SearchRivalByKeywordDto.Response.from(result);
        return ApiResponse.success(response, "키워드를 이용하여 라이벌 등록가능한 유저를 성공적으로 조회했습니다.");
    }

    // 라이벌 - 라이벌 추가
    @GetMapping("/rival")
    public ApiResponse<Void> addRival(
            @AuthenticatedActor Actor actor,
            @Valid @RequestBody AddRivalDto.Request request
    ) {

        AddRivalData.Command command = AddRivalData.Command.from(actor, request);
        addRivalUseCase.execute(command);
        return ApiResponse.created("라이벌을 성공적으로 추가했습니다.");
    }
}
