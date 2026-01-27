package com.process.clash.adapter.web.user.controller;

import com.process.clash.adapter.web.common.ApiResponse;
import com.process.clash.adapter.web.user.docs.controller.UserControllerDocument;
import com.process.clash.adapter.web.user.dto.GetMyGitHubActivityDto;
import com.process.clash.adapter.web.user.dto.GetMyProfileDto;
import com.process.clash.adapter.web.security.AuthenticatedActor;
import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.profile.data.GetMyGitHubActivityData;
import com.process.clash.application.profile.data.GetMyProfileData;
import com.process.clash.application.profile.port.in.GetMyGitHubActivityUsecase;
import com.process.clash.application.profile.port.in.GetMyProfileUsecase;
import com.process.clash.domain.common.enums.PeriodCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController implements UserControllerDocument {

    private final GetMyProfileUsecase getMyProfileUsecase;
    private final GetMyGitHubActivityUsecase getMyGitHubActivityUsecase;

    @GetMapping("/me")
    public ApiResponse<GetMyProfileDto.Response> getMyProfile(
        @AuthenticatedActor Actor actor
    ) {
        GetMyProfileData.Command command = new GetMyProfileData.Command(actor);
        GetMyProfileData.Result result = getMyProfileUsecase.execute(command);
        GetMyProfileDto.Response response = GetMyProfileDto.Response.from(result);
        return ApiResponse.success(response, "내 프로필을 성공적으로 조회했습니다.");
    }

    @GetMapping("/github")
    public ApiResponse<GetMyGitHubActivityDto.Response> getMyGitHubActivity(
        @AuthenticatedActor Actor actor,
        @RequestParam PeriodCategory period
    ) {
        GetMyGitHubActivityData.Command command = new GetMyGitHubActivityData.Command(actor, period);
        GetMyGitHubActivityData.Result result = getMyGitHubActivityUsecase.execute(command);
        GetMyGitHubActivityDto.Response response = GetMyGitHubActivityDto.Response.from(result);
        return ApiResponse.success(response, "특정 기간 동안의 깃허브 활동을 성공적으로 조회했습니다.");
    }
}
