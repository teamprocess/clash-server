package com.process.clash.adapter.web.realtime.controller;

import com.process.clash.adapter.web.common.ApiResponse;
import com.process.clash.adapter.web.realtime.dto.IssueSocketTokenDto;
import com.process.clash.adapter.web.security.AuthenticatedActor;
import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.realtime.data.IssueSocketTokenData;
import com.process.clash.application.realtime.port.in.IssueSocketTokenUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/realtime/socket")
@RequiredArgsConstructor
public class SocketTokenController {

    private final IssueSocketTokenUseCase issueSocketTokenUseCase;

    @PostMapping("/token")
    public ApiResponse<IssueSocketTokenDto.Response> issueToken(
        @AuthenticatedActor Actor actor
    ) {
        IssueSocketTokenData.Result result = issueSocketTokenUseCase.issueToken(actor.id());
        IssueSocketTokenDto.Response response = IssueSocketTokenDto.Response.from(result);
        return ApiResponse.success(response, "소켓 연결 토큰 발급을 성공했습니다.");
    }
}
