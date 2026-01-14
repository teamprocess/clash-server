package com.process.clash.adapter.web.shop.season.controller;

import com.process.clash.adapter.web.common.ApiResponse;
import com.process.clash.adapter.web.shop.season.dto.CreateSeasonDto;
import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.shop.season.data.CreateSeasonData;
import com.process.clash.application.shop.season.port.in.CreateSeasonUseCase;
import com.process.clash.infrastructure.principle.AuthUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/shop/season")
@RequiredArgsConstructor
public class SeasonAdminController {

    private final CreateSeasonUseCase createSeasonUseCase;

    @PostMapping
    public ApiResponse<Void> createSeason(
            @AuthenticationPrincipal AuthUser authUser,
            @Valid @RequestBody CreateSeasonDto.Request request
    ) {
        Actor actor = authUser.toActor();
        CreateSeasonData.Command command = CreateSeasonData.Command.from(request, actor);
        createSeasonUseCase.execute(command);
        return ApiResponse.success("시즌 생성에 성공했습니다.");
    }
}
