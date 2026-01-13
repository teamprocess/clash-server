package com.process.clash.adapter.web.mainpage.controller;

import com.process.clash.adapter.web.common.ApiResponse;
import com.process.clash.adapter.web.mainpage.dto.GetUserProfileDto;
import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.mainpage.data.GetUserProfileData;
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

    @GetMapping("/user-info")
    public ApiResponse<GetUserProfileDto.Response> getUserInfo(
            @AuthenticationPrincipal AuthUser authUser
    ) {
        Actor actor = authUser.toActor();
        GetUserProfileData.Command command = GetUserProfileData.Command.builder()
                .actor(actor)
                .build();
        return null;
    }
}
