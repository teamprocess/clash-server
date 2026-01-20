package com.process.clash.adapter.web.compete.rival.battle.controller;

import com.process.clash.adapter.web.common.ApiResponse;
import com.process.clash.adapter.web.compete.rival.battle.dto.*;
import com.process.clash.adapter.web.security.AuthenticatedActor;
import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.compete.rival.battle.data.*;
import com.process.clash.application.compete.rival.battle.port.in.*;
import com.process.clash.domain.common.enums.TargetCategory;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/compete/rivals/battles")
@RequiredArgsConstructor
public class BattleController {

    private final ApplyBattleUseCase applyBattleUseCase;
    private final AcceptBattleUseCase acceptBattleUseCase;
    private final RejectBattleUseCase rejectBattleUseCase;
    private final FindAbleRivalsUseCase findAbleRivalsUseCase;
    private final FindDetailedBattleInfoUseCase findDetailedBattleInfoUseCase;
    private final FindAllBattleInfoUseCase findAllBattleInfoUseCase;
    private final AnalyzeBattleInfoUseCase analyzeBattleInfoUseCase;

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
    @PostMapping("/accept")
    public ApiResponse<Void> acceptBattle(
            @AuthenticatedActor Actor actor,
            @Valid @RequestBody ModifyBattleDto.Request request
    ) {

        ModifyBattleData.Command command = request.toCommand(actor);
        acceptBattleUseCase.execute(command);
        return ApiResponse.success("라이벌과의 배틀 신청을 성공적으로 승인하였습니다.");
    }

    // 라이벌과의 경쟁 - 배틀 거절
    @PostMapping("/reject")
    public ApiResponse<Void> rejectBattle(
            @AuthenticatedActor Actor actor,
            @Valid @RequestBody ModifyBattleDto.Request request
    ) {

        ModifyBattleData.Command command = request.toCommand(actor);
        rejectBattleUseCase.execute(command);
        return ApiResponse.success("라이벌과의 배틀 신청을 성공적으로 거절하였습니다.");
    }

    // 라이벌과의 경쟁 - 라이벌 조회
    @GetMapping("/rivals")
    public ApiResponse<FindAbleRivalsDto.Response> findAbleRivals(
            @AuthenticatedActor Actor actor
    ) {
        FindAbleRivalsData.Command command = FindAbleRivalsData.Command.from(actor);
        FindAbleRivalsData.Result result = findAbleRivalsUseCase.execute(command);
        FindAbleRivalsDto.Response response = FindAbleRivalsDto.Response.from(result);
        return ApiResponse.success(response, "배틀을 신청할 라이벌 목록을 성공적으로 반환하였습니다.");
    }

    // 라이벌과의 경쟁 - 배틀 상세 정보
    @GetMapping("/{id}")
    public ApiResponse<FindDetailedBattleInfoDto.Response> findDetailedCertainBattleInfo(
            @AuthenticatedActor Actor actor,
            @PathVariable Long id
    ) {

        FindDetailedBattleInfoData.Command command = FindDetailedBattleInfoData.Command.from(actor, id);
        FindDetailedBattleInfoData.Result result = findDetailedBattleInfoUseCase.execute(command);
        FindDetailedBattleInfoDto.Response response = FindDetailedBattleInfoDto.Response.from(result);
        return ApiResponse.success(response, "라이벌과의 배틀 상세 정보를 성공적으로 반환했습니다.");
    }

    // 라이벌과의 경쟁 - 배틀
    @GetMapping
    public ApiResponse<FindAllBattleInfoDto.Response> findAllBattleInfo(
            @AuthenticatedActor Actor actor
    ) {

        FindAllBattleInfoData.Command command = FindAllBattleInfoData.Command.from(actor);
        FindAllBattleInfoData.Result result = findAllBattleInfoUseCase.execute(command);
        FindAllBattleInfoDto.Response response = FindAllBattleInfoDto.Response.from(result);
        return ApiResponse.success(response, "라이벌과의 배틀 정보를 성공적으로 반환했습니다.");
    }

    // 라이벌과의 경쟁 - 배틀 정보 분석
    @GetMapping("/{id}/analyze/category/{category}")
    public ApiResponse<AnalyzeBattleInfoDto.Response> analyzeBattleInfo(
            @AuthenticatedActor Actor actor,
            @PathVariable TargetCategory category
    ) {

        AnalyzeBattleInfoData.Command command = AnalyzeBattleInfoData.Command.from(actor, category);
        AnalyzeBattleInfoData.Result result = analyzeBattleInfoUseCase.execute(command);
        AnalyzeBattleInfoDto.Response response = AnalyzeBattleInfoDto.Response.from(result);
        return ApiResponse.success(response, "라이벌과의 배틀 정보 분석을 성공적으로 반환했습니다.");
    }
}