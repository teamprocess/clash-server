package com.process.clash.adapter.web.compete.rival.battle.controller;

import com.process.clash.adapter.web.common.ApiResponse;
import com.process.clash.adapter.web.compete.rival.battle.dto.ApplyBattleDto;
import com.process.clash.adapter.web.compete.rival.battle.dto.ModifyBattleDto;
import com.process.clash.adapter.web.security.AuthenticatedActor;
import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.compete.rival.battle.data.ApplyBattleData;
import com.process.clash.application.compete.rival.battle.data.ModifyBattleData;
import com.process.clash.application.compete.rival.battle.port.in.AcceptBattleUseCase;
import com.process.clash.application.compete.rival.battle.port.in.ApplyBattleUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/compete/rivals/battles")
@RequiredArgsConstructor
public class BattleController {

    private final ApplyBattleUseCase applyBattleUseCase;
    private final AcceptBattleUseCase acceptBattleUseCase;

    // 라이벌과의 경쟁 - 배틀 신청
    @PostMapping("/apply")
    public ApiResponse<Void> applyBattle(
            @AuthenticatedActor Actor actor,
            @Valid @RequestBody ApplyBattleDto.Request request
    ) {

        ApplyBattleData.Command command = request.toCommand(actor);
        applyBattleUseCase.execute(command);
        return ApiResponse.created("라이벌과의 배틀 신청을 성공적으로 생성하였습니다.");
    }

    // 라이벌과의 경쟁 - 배틀 승인
    @PostMapping("/apply")
    public ApiResponse<Void> acceptBattle(
            @AuthenticatedActor Actor actor,
            @Valid @RequestBody ModifyBattleDto.Request request
    ) {

        ModifyBattleData.Command command = request.toCommand(actor);
        acceptBattleUseCase.execute(command);
        return ApiResponse.success("라이벌과의 배틀 신청을 성공적으로 승인하였습니다.");
    }
}