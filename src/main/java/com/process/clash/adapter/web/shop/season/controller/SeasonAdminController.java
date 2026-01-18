package com.process.clash.adapter.web.shop.season.controller;

import com.process.clash.adapter.web.common.ApiResponse;
import com.process.clash.adapter.web.shop.season.docs.controller.SeasonAdminControllerDocument;
import com.process.clash.adapter.web.security.AuthenticatedActor;
import com.process.clash.adapter.web.shop.season.dto.CreateSeasonDto;
import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.shop.season.data.CreateSeasonData;
import com.process.clash.application.shop.season.port.in.CreateSeasonUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/shop/season")
@RequiredArgsConstructor
public class SeasonAdminController implements SeasonAdminControllerDocument {

    private final CreateSeasonUseCase createSeasonUseCase;

    // 시즌 생성
    @PostMapping
    public ApiResponse<Void> createSeason(
            @AuthenticatedActor Actor actor,
            @Valid @RequestBody CreateSeasonDto.Request request
    ) {
        CreateSeasonData.Command command = request.toCommand(actor);
        createSeasonUseCase.execute(command);
        return ApiResponse.success("시즌 생성에 성공했습니다.");
    }
}
