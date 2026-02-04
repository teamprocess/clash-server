package com.process.clash.adapter.web.roadmap.v2.choice.controller;

import com.process.clash.adapter.web.common.ApiResponse;
import com.process.clash.adapter.web.roadmap.v2.choice.docs.controller.AdminChoiceV2ControllerDocument;
import com.process.clash.adapter.web.roadmap.v2.choice.dto.CreateChoiceV2Dto;
import com.process.clash.adapter.web.roadmap.v2.choice.dto.UpdateChoiceV2Dto;
import com.process.clash.adapter.web.security.AuthenticatedActor;
import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.roadmap.v2.choice.data.CreateChoiceV2Data;
import com.process.clash.application.roadmap.v2.choice.data.DeleteChoiceV2Data;
import com.process.clash.application.roadmap.v2.choice.data.UpdateChoiceV2Data;
import com.process.clash.application.roadmap.v2.choice.port.in.CreateChoiceV2UseCase;
import com.process.clash.application.roadmap.v2.choice.port.in.DeleteChoiceV2UseCase;
import com.process.clash.application.roadmap.v2.choice.port.in.UpdateChoiceV2UseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/admin/choices")
@RequiredArgsConstructor
public class AdminChoiceV2Controller implements AdminChoiceV2ControllerDocument {

    private final CreateChoiceV2UseCase createChoiceV2UseCase;
    private final UpdateChoiceV2UseCase updateChoiceV2UseCase;
    private final DeleteChoiceV2UseCase deleteChoiceV2UseCase;

    @PostMapping
    public ApiResponse<CreateChoiceV2Dto.Response> createChoice(
            @AuthenticatedActor Actor actor,
            @Valid @RequestBody CreateChoiceV2Dto.Request request
    ) {
        CreateChoiceV2Data.Command command = request.toCommand(actor);
        CreateChoiceV2Data.Result result = createChoiceV2UseCase.execute(command);
        CreateChoiceV2Dto.Response response = CreateChoiceV2Dto.Response.from(result);
        return ApiResponse.created(response, "선택지 생성을 성공했습니다.");
    }

    @PatchMapping("/{choiceId}")
    public ApiResponse<UpdateChoiceV2Dto.Response> updateChoice(
            @AuthenticatedActor Actor actor,
            @PathVariable Long choiceId,
            @Valid @RequestBody UpdateChoiceV2Dto.Request request
    ) {
        UpdateChoiceV2Data.Command command = request.toCommand(actor, choiceId);
        UpdateChoiceV2Data.Result result = updateChoiceV2UseCase.execute(command);
        UpdateChoiceV2Dto.Response response = UpdateChoiceV2Dto.Response.from(result);
        return ApiResponse.success(response, "선택지 수정을 성공했습니다.");
    }

    @DeleteMapping("/{choiceId}")
    public ApiResponse<Void> deleteChoice(
            @AuthenticatedActor Actor actor,
            @PathVariable Long choiceId
    ) {
        DeleteChoiceV2Data.Command command = new DeleteChoiceV2Data.Command(actor, choiceId);
        deleteChoiceV2UseCase.execute(command);
        return ApiResponse.success("선택지 삭제를 성공했습니다.");
    }
}
