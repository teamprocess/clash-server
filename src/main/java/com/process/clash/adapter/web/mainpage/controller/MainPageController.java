package com.process.clash.adapter.web.mainpage.controller;

import com.process.clash.adapter.web.common.ApiResponse;
import com.process.clash.adapter.web.mainpage.dto.GetUserProfileDto;
import com.process.clash.adapter.web.security.AuthenticatedActor;
import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.mainpage.data.mainpage.*;
import com.process.clash.application.mainpage.port.in.mainpage.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/main")
@RequiredArgsConstructor
public class MainPageController {

    private final GetUserProfileUseCase getUserProfileUseCase;

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


}
