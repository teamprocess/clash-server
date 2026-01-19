package com.process.clash.adapter.web.roadmap.section.controller;

import com.process.clash.adapter.web.common.ApiResponse;
import com.process.clash.adapter.web.roadmap.section.docs.controller.AdminSectionControllerDocument;
import com.process.clash.adapter.web.roadmap.section.dto.CreateSectionDto;
import com.process.clash.adapter.web.roadmap.section.dto.UpdateSectionDto;
import com.process.clash.adapter.web.security.AuthenticatedActor;
import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.roadmap.section.data.CreateSectionData;
import com.process.clash.application.roadmap.section.data.DeleteSectionData;
import com.process.clash.application.roadmap.section.data.UpdateSectionData;
import com.process.clash.application.roadmap.section.port.in.CreateSectionUseCase;
import com.process.clash.application.roadmap.section.port.in.DeleteSectionUseCase;
import com.process.clash.application.roadmap.section.port.in.UpdateSectionUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/sections")
@RequiredArgsConstructor
public class AdminSectionController implements AdminSectionControllerDocument {

    private final CreateSectionUseCase createSectionUseCase;
    private final UpdateSectionUseCase updateSectionUseCase;
    private final DeleteSectionUseCase deleteSectionUseCase;

    @PostMapping
    public ApiResponse<CreateSectionDto.Response> createSection(
            @AuthenticatedActor Actor actor,
            @Valid @RequestBody CreateSectionDto.Request request
    ) {
        CreateSectionData.Command command = request.toCommand(actor);
        CreateSectionData.Result result = createSectionUseCase.execute(command);
        CreateSectionDto.Response response = CreateSectionDto.Response.from(result);
        return ApiResponse.created(response, "로드맵 생성을 성공했습니다.");
    }

    @PatchMapping("/{sectionId}")
    public ApiResponse<UpdateSectionDto.Response> updateSection(
            @AuthenticatedActor Actor actor,
            @PathVariable Long sectionId,
            @Valid @RequestBody UpdateSectionDto.Request request
    ) {
        UpdateSectionData.Command command = request.toCommand(actor, sectionId);
        UpdateSectionData.Result result = updateSectionUseCase.execute(command);
        UpdateSectionDto.Response response = UpdateSectionDto.Response.from(result);
        return ApiResponse.success(response, "로드맵 수정을 성공했습니다.");
    }

    @DeleteMapping("/{sectionId}")
    public ApiResponse<Void> deleteSection(
            @AuthenticatedActor Actor actor,
            @PathVariable Long sectionId
    ) {
        DeleteSectionData.Command command = new DeleteSectionData.Command(actor, sectionId);
        deleteSectionUseCase.execute(command);
        return ApiResponse.success("로드맵 삭제를 성공했습니다. (연관된 챕터, 미션, 질문도 함께 삭제됩니다)");
    }
}
