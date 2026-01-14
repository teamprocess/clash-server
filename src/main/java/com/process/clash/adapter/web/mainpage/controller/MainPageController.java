package com.process.clash.adapter.web.mainpage.controller;

import com.process.clash.adapter.web.common.ApiResponse;
import com.process.clash.adapter.web.mainpage.dto.GetCompareWithYesterdayDto;
import com.process.clash.adapter.web.mainpage.dto.GetUserProfileDto;
import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.mainpage.data.GetCompareWithYesterdayData;
import com.process.clash.application.mainpage.data.GetUserProfileData;
import com.process.clash.application.mainpage.port.in.GetCompareWithYesterdayUseCase;
import com.process.clash.application.mainpage.port.in.GetUserProfileUseCase;
import com.process.clash.infrastructure.principle.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/main")
@RequiredArgsConstructor
public class MainPageController {

    private final GetUserProfileUseCase getUserProfileUseCase;
    private final GetCompareWithYesterdayUseCase getCompareWithYesterdayUseCase;

    // 유저 정보 조회 - 상단바 프로필 정보
    @GetMapping("/user-info")
    public ApiResponse<GetUserProfileDto.Response> getUserProfile(
            @AuthenticationPrincipal AuthUser authUser
    ) {
        Actor actor = authUser.toActor();
        GetUserProfileData.Command command = GetUserProfileData.Command.from(actor);
        GetUserProfileData.Result result = getUserProfileUseCase.execute(command);
        GetUserProfileDto.Response response = GetUserProfileDto.Response.from(result);
        return ApiResponse.success(response, "유저 정보를 성공적으로 반환했습니다.");
    }

    // 어제와의 비교
    // TODO: 추가 구현 필요합니다
    @GetMapping("/compare")
    public ApiResponse<GetCompareWithYesterdayDto.Response> getCompareWithYesterday(
            @AuthenticationPrincipal AuthUser authUser
    ) {
        Actor actor = authUser.toActor();
        GetCompareWithYesterdayData.Command command = GetCompareWithYesterdayData.Command.from(actor);
        GetCompareWithYesterdayData.Result result = getCompareWithYesterdayUseCase.execute(command);
        GetCompareWithYesterdayDto.Response response = GetCompareWithYesterdayDto.Response.from(result);
        return ApiResponse.success(response, "어제와의 비교 정보를 성공적으로 반환했습니다.");
    }
}
