package com.process.clash.adapter.web.user.controller;

import com.process.clash.adapter.web.common.ApiResponse;
import com.process.clash.adapter.web.security.AuthenticatedActor;
import com.process.clash.adapter.web.user.docs.controller.UserNoticeControllerDocument;
import com.process.clash.adapter.web.user.dto.GetMyUserNoticesDto;
import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.user.usernotice.data.GetMyUserNoticesData;
import com.process.clash.application.user.usernotice.data.ReadUserNoticeData;
import com.process.clash.application.user.usernotice.port.in.GetAllMyUserNoticesUseCase;
import com.process.clash.application.user.usernotice.port.in.GetMyUserNoticesUseCase;
import com.process.clash.application.user.usernotice.port.in.GetNoticesUseCase;
import com.process.clash.application.user.usernotice.port.in.ReadUserNoticeUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users/me/notices")
@RequiredArgsConstructor
public class UserNoticeController implements UserNoticeControllerDocument {

    private final GetMyUserNoticesUseCase getMyUserNoticesUseCase;
    private final GetAllMyUserNoticesUseCase getAllMyUserNoticesUseCase;
    private final ReadUserNoticeUseCase readUserNoticeUseCase;

    @GetMapping
    public ApiResponse<GetMyUserNoticesDto.Response> getMyNotices(
            @AuthenticatedActor Actor actor
    ) {
        return executeGetNotices(actor, getMyUserNoticesUseCase, "알림 목록을 성공적으로 조회했습니다.");
    }

    @GetMapping("/all")
    public ApiResponse<GetMyUserNoticesDto.Response> getAllMyNotices(
            @AuthenticatedActor Actor actor
    ) {
        return executeGetNotices(actor, getAllMyUserNoticesUseCase, "전체 알림 목록을 성공적으로 조회했습니다.");
    }

    private ApiResponse<GetMyUserNoticesDto.Response> executeGetNotices(
            Actor actor, GetNoticesUseCase useCase, String message
    ) {
        GetMyUserNoticesData.Command command = GetMyUserNoticesData.Command.from(actor);
        GetMyUserNoticesData.Result result = useCase.execute(command);
        GetMyUserNoticesDto.Response response = GetMyUserNoticesDto.Response.from(result);
        return ApiResponse.success(response, message);
    }

    @PatchMapping("/{id}/read")
    public ApiResponse<Void> readNotice(
            @AuthenticatedActor Actor actor,
            @PathVariable Long id
    ) {

        ReadUserNoticeData.Command command = ReadUserNoticeData.Command.of(actor, id);
        readUserNoticeUseCase.execute(command);
        return ApiResponse.success("알림을 읽음 처리했습니다.");
    }
}
