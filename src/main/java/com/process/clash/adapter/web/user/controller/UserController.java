package com.process.clash.adapter.web.user.controller;

import com.process.clash.adapter.web.common.ApiResponse;
import com.process.clash.adapter.web.user.docs.controller.UserControllerDocument;
import com.process.clash.adapter.web.user.dto.GetMyGitHubActivityDetailDto;
import com.process.clash.adapter.web.user.dto.GetMyGitHubActivityDto;
import com.process.clash.adapter.web.user.dto.GetMyGitHubLinkStatusDto;
import com.process.clash.adapter.web.user.dto.GetMyActivityCalendarDto;
import com.process.clash.adapter.web.user.dto.GetMyItemsDto;
import com.process.clash.adapter.web.user.dto.GetMyProfileDto;
import com.process.clash.adapter.web.user.dto.LinkGitHubOAuthDto;
import com.process.clash.adapter.web.security.AuthenticatedActor;
import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.profile.data.GetMyGitHubActivityDetailData;
import com.process.clash.application.profile.data.GetMyGitHubActivityData;
import com.process.clash.application.profile.data.GetMyActivityCalendarData;
import com.process.clash.application.profile.data.GetMyItemsData;
import com.process.clash.application.profile.data.GetMyProfileData;
import com.process.clash.application.profile.port.in.GetMyGitHubActivityDetailUsecase;
import com.process.clash.application.profile.port.in.GetMyGitHubActivityUsecase;
import com.process.clash.application.profile.port.in.GetMyActivityCalendarUsecase;
import com.process.clash.application.profile.port.in.GetMyItemsUsecase;
import com.process.clash.application.profile.port.in.GetMyProfileUsecase;
import com.process.clash.application.user.usergithub.data.GetMyGitHubLinkStatusData;
import com.process.clash.application.user.usergithub.data.LinkGitHubOAuthData;
import com.process.clash.application.user.usergithub.port.in.GetMyGitHubLinkStatusUsecase;
import com.process.clash.application.user.usergithub.port.in.LinkGitHubOAuthUsecase;
import com.process.clash.domain.common.enums.PeriodCategory;
import com.process.clash.domain.common.enums.UserItemCategory;
import java.time.LocalDate;
import java.time.YearMonth;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController implements UserControllerDocument {

    private final GetMyProfileUsecase getMyProfileUsecase;
    private final GetMyGitHubActivityUsecase getMyGitHubActivityUsecase;
    private final GetMyGitHubActivityDetailUsecase getMyGitHubActivityDetailUsecase;
    private final GetMyItemsUsecase getMyItemsUsecase;
    private final GetMyActivityCalendarUsecase getMyActivityCalendarUsecase;
    private final LinkGitHubOAuthUsecase linkGitHubOAuthUsecase;
    private final GetMyGitHubLinkStatusUsecase getMyGitHubLinkStatusUsecase;

    @GetMapping("/me")
    public ApiResponse<GetMyProfileDto.Response> getMyProfile(
        @AuthenticatedActor Actor actor
    ) {
        GetMyProfileData.Command command = new GetMyProfileData.Command(actor);
        GetMyProfileData.Result result = getMyProfileUsecase.execute(command);
        GetMyProfileDto.Response response = GetMyProfileDto.Response.from(result);
        return ApiResponse.success(response, "내 프로필을 성공적으로 조회했습니다.");
    }

    @GetMapping("/me/github")
    public ApiResponse<GetMyGitHubActivityDto.Response> getMyGitHubActivity(
        @AuthenticatedActor Actor actor,
        @RequestParam PeriodCategory period
    ) {
        GetMyGitHubActivityData.Command command = new GetMyGitHubActivityData.Command(actor, period);
        GetMyGitHubActivityData.Result result = getMyGitHubActivityUsecase.execute(command);
        GetMyGitHubActivityDto.Response response = GetMyGitHubActivityDto.Response.from(result);
        return ApiResponse.success(response, "특정 기간 동안의 깃허브 활동을 성공적으로 조회했습니다.");
    }

    @GetMapping("/me/github/{date}")
    public ApiResponse<GetMyGitHubActivityDetailDto.Response> getMyGitHubActivityDetail(
        @AuthenticatedActor Actor actor,
        @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        GetMyGitHubActivityDetailData.Command command = new GetMyGitHubActivityDetailData.Command(actor, date);
        GetMyGitHubActivityDetailData.Result result = getMyGitHubActivityDetailUsecase.execute(command);
        GetMyGitHubActivityDetailDto.Response response = GetMyGitHubActivityDetailDto.Response.from(result);
        return ApiResponse.success(response, "특정 날짜의 깃허브 활동을 성공적으로 조회했습니다.");
    }

    @PostMapping("/me/github/link")
    public ApiResponse<LinkGitHubOAuthDto.Response> linkGitHubOAuth(
        @AuthenticatedActor Actor actor,
        @RequestBody LinkGitHubOAuthDto.Request request
    ) {
        LinkGitHubOAuthData.Command command = request.toCommand(actor);
        LinkGitHubOAuthData.Result result = linkGitHubOAuthUsecase.execute(command);
        LinkGitHubOAuthDto.Response response = LinkGitHubOAuthDto.Response.from(result);
        return ApiResponse.success(response, "GitHub 계정 연동을 완료했습니다.");
    }

    @GetMapping("/me/github/link")
    public ApiResponse<GetMyGitHubLinkStatusDto.Response> getMyGitHubLinkStatus(
        @AuthenticatedActor Actor actor
    ) {
        GetMyGitHubLinkStatusData.Command command = new GetMyGitHubLinkStatusData.Command(actor);
        GetMyGitHubLinkStatusData.Result result = getMyGitHubLinkStatusUsecase.execute(command);
        GetMyGitHubLinkStatusDto.Response response = GetMyGitHubLinkStatusDto.Response.from(result);
        return ApiResponse.success(response, "GitHub 연동 상태를 성공적으로 조회했습니다.");
    }

    @GetMapping("/me/items")
    public ApiResponse<GetMyItemsDto.Response> getMyItems(
        @AuthenticatedActor Actor actor,
        @RequestParam(defaultValue = "ALL") UserItemCategory category
    ) {
        GetMyItemsData.Command command = new GetMyItemsData.Command(actor, category);
        GetMyItemsData.Result result = getMyItemsUsecase.execute(command);
        GetMyItemsDto.Response response = GetMyItemsDto.Response.from(result);
        return ApiResponse.success(response, "보유한 아이템 목록을 성공적으로 조회했습니다.");
    }

    @GetMapping("/me/calendar")
    public ApiResponse<GetMyActivityCalendarDto.Response> getMyActivityCalendar(
        @AuthenticatedActor Actor actor,
        @RequestParam("month") @DateTimeFormat(pattern = "yyyy-MM") YearMonth month
    ) {
        GetMyActivityCalendarData.Command command = new GetMyActivityCalendarData.Command(actor, month);
        GetMyActivityCalendarData.Result result = getMyActivityCalendarUsecase.execute(command);
        GetMyActivityCalendarDto.Response response = GetMyActivityCalendarDto.Response.from(result);
        return ApiResponse.success(response, "활동 캘린더 정보를 성공적으로 조회했습니다.");
    }
}
